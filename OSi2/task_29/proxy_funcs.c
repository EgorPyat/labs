#include "proxy.h"

/*app funtions*/

int create_server(proxy_server* server){
  int on = 1;
  int rc;
  struct sockaddr_in addr;

  int listen_sd = socket(AF_INET, SOCK_STREAM, 0);
  if(listen_sd < 0){
    return -1;
  }

  rc = setsockopt(listen_sd, SOL_SOCKET,  SO_REUSEADDR, (char *)&on, sizeof(on));
  if(rc < 0){
    close(listen_sd);
    return -1;
  }

  rc = ioctl(listen_sd, FIONBIO, (char *)&on);
  if(rc < 0){
    close(listen_sd);
    return -1;
  }

  memset(&addr, 0, sizeof(addr));
  addr.sin_family      = AF_INET;
  addr.sin_addr.s_addr = htonl(INADDR_ANY);
  addr.sin_port        = htons(SERVER_PORT);
  rc = bind(listen_sd, (struct sockaddr *)&addr, sizeof(addr));
  if(rc < 0){
    close(listen_sd);
    return -1;
  }

  rc = listen(listen_sd, 32);
  if(rc < 0){
    close(listen_sd);
    return -1;
  }

  server->listen_sd     = listen_sd;
  server->fds           = (struct pollfd*)malloc(sizeof(struct pollfd) * CONNECTIONS);
  memset(server->fds, 0, sizeof(server->fds) * CONNECTIONS);
  server->fds[0].fd     = listen_sd;
  server->fds[0].events = POLLIN;
  server->nfds          = 1;
  server->entries       = (proxy_entry*)malloc(sizeof(proxy_entry) * ENTRIESNUM);
  server->nentries      = ENTRIESNUM;
  server->messages      = (message*)malloc(sizeof(message) * MESSAGESNUM);
  server->nmsg          = MESSAGESNUM;

  return 0;
}

int close_server(proxy_server* server){
  int status = 0;

  for(int i = 0; i < server->nfds; i++){
    if(server->fds[i].fd >= 0){
      if(close(server->fds[i].fd) == -1){
        status = -1;
      }
    }
  }

  for(int i = 0; i < server->nmsg; i++){
    free(server->messages[i].buffer);
  }

  free(server->messages);
  free(server->entries);
  free(server->fds);

  return status;
}

int accept_connections(proxy_server* server){
  int new_sd;
  int rc;
  int on = 1;

  do{
    new_sd = accept(server->listen_sd, NULL, NULL);
    if(new_sd < 0){
      if(errno != EWOULDBLOCK){
        return -1;
      }
      break;
    }
    rc = ioctl(new_sd, FIONBIO, (char*)&on);
    if(rc < 0){
      close(new_sd);
      continue;
    }
    else{
      printf("  New incoming connection - %d\n", new_sd);
      server->fds[server->nfds].fd = new_sd;
      server->fds[server->nfds].events = POLLIN;
      server->messages[server->nfds].request_fd = -1;
      server->messages[server->nfds].buffer = (char*)calloc(STARTBUFFERSIZE, sizeof(char));
      server->messages[server->nfds].size = 0;
      server->messages[server->nfds].max_size = STARTBUFFERSIZE;
      server->nfds += 1;
    }
  }
  while(new_sd != -1);

  return 0;
}

int close_connection(proxy_server* server, int num){
  int status = 0;

  if(close(server->fds[num].fd) == -1){
    status = -1;
  }

  server->fds[num].fd == -1;
  server->fds[num].events = 0;
  free(server->messages[num].buffer);

  return status;
}

void compress_array(proxy_server* server){
  int i;
  int j;

  for(i = 0; i < server->nfds; i++){
    if(server->fds[i].fd == -1){
      for(j = i; j < server->nfds; j++){
        if(i < server->messages[j].request_fd){
          server->messages[j].request_fd -= 1;
        }
        server->fds[j].fd = server->fds[j+1].fd;
      }
      server->nfds -= 1;
    }
  }
}
