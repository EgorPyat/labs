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
      server->messages[server->nfds].type = REQUEST;
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

  server->fds[num].fd = -1;
  server->fds[num].events = 0;
  free(server->messages[num].buffer);
  server->messages[num].buffer = NULL;

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

int get_request(message* request, int fd){
  int rc;
  do{
    rc = recv(fd, request->buffer + request->size, 1024, 0);

    request->size += rc;

    if(request->size + 1 >= request->max_size){
      request->max_size *= 2;
      request->buffer = (char*)realloc(request->buffer, request->max_size);
    }

    if(rc < 0){
      request->size += 1;
      if(errno != EWOULDBLOCK){
        perror("  recv() failed");
        return -1;
      }
      else if(errno == EWOULDBLOCK){
        char* end = strstr(request->buffer, "\r\n\r\n");
        if(end == NULL){
          printf("not ended %d %d\n", request->size, fd);
          return 1;
        }
        for(int i = 0; i < request->size; i++){
          printf("%c", request->buffer[i]);
        }
        return 2;
      }
    }
    else if(rc == 0){
      printf("  Connection closed\n");
      return 0;
    }
  }
  while(TRUE);
}

int parse_request(message* request, char* hostname, char* method, char* version){
  char* request_body = strchr(request->buffer, '\n');

  if(request_body != NULL){
    int length = request_body - request->buffer;
    char* request_head = (char*)malloc(length + 1);
    strncpy(request_head, request->buffer, length);
    request_head[length] = '\0';

    char *meth = strtok(request_head, " ");
    meth[strlen(meth)] = '\0';
    strncpy(method, meth, strlen(meth));
    char *url = strtok(NULL, " ");
    char *vers = strtok(NULL, "\n\0");
    vers[strlen(vers) - 1] = '\0';
    strncpy(version, vers, strlen(vers));

    char *name = strstr(url, "://");
    char *name_end = strchr(name + 3, '/');

    length = 0;

    if(name_end == NULL){
      length = strlen(name + 3);
    }
    else{
      length = name_end - (name + 3);
    }

    strncpy(hostname, name + 3, length);
    hostname[length] = '\0';

    free(request_head);
  }
  else{
    return -1;
  }

  return 0;
}

int create_connection(proxy_server* server, char* hostname, int fd_num){
  int rc;
  int on = 1;

  struct hostent * host_info = gethostbyname(hostname);

  if(host_info == NULL){
    printf("  gethostbyname() failed\n");
    return -1;
  }

  struct sockaddr_in destinationAddress;

  destinationAddress.sin_family = AF_INET;
  destinationAddress.sin_port = htons(80);
  memcpy(&destinationAddress.sin_addr, host_info->h_addr, host_info->h_length);

  int destnation = socket(AF_INET, SOCK_STREAM, 0);
  if(destnation < 0){
    return -1;
  }

  rc = ioctl(destnation, FIONBIO, (char *)&on);
  if(rc < 0){
    close(destnation);
    return -1;
  }

  int con = connect(destnation, (struct sockaddr *)&destinationAddress, sizeof(destinationAddress));

  if(con < 0){
    if(errno != EINPROGRESS){
      close(destnation);
      return -1;
    }
  }
  server->fds[server->nfds].fd = destnation;
  server->fds[server->nfds].events = POLLOUT;
  server->messages[server->nfds].request_fd = fd_num;
  server->messages[server->nfds].buffer = (char*)calloc(STARTBUFFERSIZE, sizeof(char));
  server->messages[server->nfds].size = server->messages[fd_num].size;
  server->messages[server->nfds].max_size = server->messages[fd_num].max_size;
  server->messages[server->nfds].type = RESPONSE;
  memcpy(server->messages[server->nfds].buffer, server->messages[fd_num].buffer, server->messages[fd_num].size);
  memset(server->messages[fd_num].buffer, 0, server->messages[fd_num].size);
  server->messages[fd_num].size = 0;
  server->nfds += 1;

  printf("%d\n", server->messages[server->nfds - 1].size);
  for(int i = 0; i < server->messages[server->nfds - 1].size; i++){
    printf("%c", server->messages[server->nfds - 1].buffer[i]);
  }

  return 0;
}

int get_response(message* response, int fd){
  int rc;
  do{
    rc = recv(fd, response->buffer + response->size, 1024, 0);

    response->size += rc;

    if(response->size + 1 >= response->max_size){
      response->max_size *= 2;
      response->buffer = (char*)realloc(response->buffer, response->max_size);
    }

    if(rc < 0){
      response->size += 1;
      if(errno != EWOULDBLOCK){
        perror("  recv() failed");
        return -1;
      }
      else if(errno == EWOULDBLOCK){
        printf("not ended %d %d\n", response->size, fd);
        return 1;
      }
    }
    else if(rc == 0){
      printf("  Connection closed\n");
      for(int i = 0; i < response->size; i++){
        printf("%c", response->buffer[i]);
      }
      return 0;
    }
  }
  while(TRUE);
}

int transfer_response(proxy_server* server, int fd_num){
  return 0;
}
