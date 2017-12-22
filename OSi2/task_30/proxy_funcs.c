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
  server->entries       = (proxy_entry*)malloc(sizeof(proxy_entry) * ENTRIESNUM);
  server->nentries      = 0;

  return 0;
}

int close_server(proxy_server* server){
  int status = 0;

  for(int i = 0; i < server->nentries; i++){
    free(server->entries[i].hostname);
    free(server->entries[i].content);
  }

  free(server->entries);

  return status;
}

int accept_connection(proxy_server* server){
  int new_sd;
  int rc;
  int on = 1;

  new_sd = accept(server->listen_sd, NULL, NULL);
  if(new_sd < 0){
    return -1;
  }
  rc = ioctl(new_sd, FIONBIO, (char*)&on);
  if(rc < 0){
    close(new_sd);
    return -1;
  }

  printf("  New incoming connection - %d\n", new_sd);

  return new_sd;
}

int close_connection(message* msg, int fd){
  printf("close_connection()\n");
  int status = 0;

  if(close(fd) == -1){
    status = -1;
  }

  free(msg->buffer);

  return status;
}

int get_request(message* request, int fd){
  int rc;
  do{
    rc = recv(fd, request->buffer + request->size, 1024, 0);

    request->size += rc;

    if(request->size + 1024 >= request->max_size){
      request->max_size *= 2;
      // printf("%d\n", request->max_size);
      printf("realloc get request buffer %d\n", request->max_size);

      request->buffer = (char*)realloc(request->buffer, request->max_size * sizeof(char));
    }

    if(rc < 0){
      request->size += 1;
      if(errno != EWOULDBLOCK || errno != EAGAIN || request->size == 0){
        if(request->size != 0) perror("req  recv() failed");
        return -1;
      }
      else if(errno == EWOULDBLOCK || errno == EAGAIN){
        char* end = strstr(request->buffer, "\r\n\r\n");
        if(end == NULL){
          printf("not ended %d %d\n", request->size, fd);
          return 1;
        }
        char* status_line_end = strchr(request->buffer, '\n');
        int len = status_line_end - request->buffer;
        request->buffer[len - 2] = '0';
        char* con = strstr(request->buffer, "Connection: ");
        if(con != NULL){
          con += 12;
          con[0] = 'c';
          con[1] = 'l';
          con[2] = 'o';
          con[3] = 's';
          con[4] = 'e';
          con += 5;
          for(int i = con - request->buffer; i < request->size - 5; i++){
            request->buffer[i] = request->buffer[i + 5];
          }
          request->size -= 5;
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

int parse_request(message* request, char* hostname, char* request_head){
  char* request_body = strchr(request->buffer, '\n');

  if(request_body != NULL){
    int length = request_body - request->buffer;
    char* requesth = (char*)malloc(length + 1);
    strncpy(request_head, request->buffer, length);
    strncpy(requesth, request->buffer, length);

    request_head[length] = '\0';
    requesth[length] = '\0';

    char *meth = strtok(requesth, " ");
    char *url = strtok(NULL, " ");
    char *vers = strtok(NULL, "\n\0");
    char *name = strstr(url, "://");

    if(name == NULL){
      length = vers - url;
      strncpy(hostname, url, length);
      hostname[length] = '\0';
    }
    else{
      char *name_end = strchr(name + 3, '/');

      if(name_end == NULL){
        length = strlen(name + 3);
      }
      else{
        length = name_end - (name + 3);
      }

      strncpy(hostname, name + 3, length);
      hostname[length] = '\0';
    }

    printf("%s\n %lu", request_head, strlen(request_head));
    // getchar();
    free(requesth);
  }
  else{
    return -1;
  }

  return 0;
}

int create_connection(char* hostname){
  int rc;
  int on = 1;
  printf("%s\n", hostname);
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

  // rc = ioctl(destnation, FIONBIO, (char *)&on);
  // if(rc < 0){
  //   close(destnation);
  //   return -1;
  // }

  int con = connect(destnation, (struct sockaddr *)&destinationAddress, sizeof(destinationAddress));

  if(con < 0){
    if(errno != EINPROGRESS){
      close(destnation);
      return -1;
    }
  }

  return destnation;
}

int get_response(message* response, int fd, proxy_server* server){
  int rc;
  do{
    rc = recv(fd, response->buffer + response->size, 1024, 0);

    response->size += rc;

    if(response->size + 1024 >= response->max_size){
      printf("realloc response buffer %d %d\n", response->size, response->max_size);
      response->max_size *= 2;

      response->buffer = (char*)realloc(response->buffer, response->max_size * sizeof(char));
    }

    int len = response->size - server->entries[response->entry_num].content_size;
    if(len > 0){
      if(response->size > server->entries[response->entry_num].max_size){
        server->entries[response->entry_num].max_size *= 2;
        printf("realloc entry buffer %d\n", server->entries[response->entry_num].max_size);

        server->entries[response->entry_num].content = (char*)realloc(server->entries[response->entry_num].content, server->entries[response->entry_num].max_size);
      }
      memcpy(server->entries[response->entry_num].content + server->entries[response->entry_num].content_size, response->buffer + response->size - len, len);
      server->entries[response->entry_num].content_size = response->size;
    }

    if(rc < 0){
      response->size += 1;
      if(errno != EWOULDBLOCK || errno != EAGAIN){
        perror("  recv() failed");
        return -1;
      }
      else if(errno == EWOULDBLOCK || errno == EAGAIN){
        printf("not ended %d %d\n", response->size, fd);
        return 1;
      }
    }
    else if(rc == 0){
      printf("  Connection closed\n");
      printf("%d %d\n", response->size, server->entries[response->entry_num].content_size);
      if(is_complete_entry(response->entry_num, server) == 0){
        response->buffer[7] = '0';
        server->entries[response->entry_num].content[7] = '0';
        server->entries[response->entry_num].complete = 1;
      }
      // for(int i = 0; i < server->entries[response->entry_num].content_size; i++){
      //   printf("%c", server->entries[response->entry_num].content[i]);
      // }
      // printf("\n");
      return 0;
    }
  }
  while(TRUE);
}

int transfer_response(message* request, message* response){
  printf("transfer_response\n");

  request->request_fd = -1;
  request->size = response->size;
  if(request->max_size < response->max_size){
    printf("realloc request buffer %d\n", request->max_size);

    request->buffer = (char*)realloc(request->buffer, response->max_size);
  }
  request->max_size = response->max_size;
  memcpy(request->buffer, response->buffer, response->size * sizeof(char));

  return 0;
}

int find_in_cache(char* name, int size, proxy_server* server){
  printf("\t\tfind_in_cache()\n");
  int i;
  int find = 0;

  for(i = 0; i < server->nentries; i++){
    // printf("\t%s\n\t\t%s\n", name, server->entries[i].hostname);
    if(strncmp(name, server->entries[i].hostname, size) == 0){
      find = 1;
      break;
    }
  }
  if(find == 1){
    return i;
  }

  return -1;
}

int is_complete_entry(int entry_num, proxy_server* server){
  if(server->entries[entry_num].complete == 1){
    return 1;
  }
  else{
    return 0;
  }
}

int get_from_cache(int entry_num, message* msg, proxy_server* server){
  printf("\t\tget_from_cache()\n");

  memset(msg->buffer, 0, msg->max_size);
  if(msg->max_size < server->entries[entry_num].content_size){
    printf("realloc req in cache buffer %d\n", msg->max_size);

    msg->buffer = (char*)realloc(msg->buffer, server->entries[entry_num].content_size);
  }
  memcpy(msg->buffer, server->entries[entry_num].content, server->entries[entry_num].content_size);
  msg->size = server->entries[entry_num].content_size;
  msg->max_size = server->entries[entry_num].content_size;

  return 0;
}

int cache_entry_name(char* name, int size, proxy_server* server, message* msg){
  printf("\t\tcache_entry_name()\n");

  server->entries[server->nentries].hostname = (char*)malloc(size);
  server->entries[server->nentries].hostname_size = size;
  server->entries[server->nentries].content = (char*)malloc(STARTBUFFERSIZE);
  server->entries[server->nentries].content_size = 0;
  server->entries[server->nentries].complete = 0;
  server->entries[server->nentries].max_size = STARTBUFFERSIZE;
  memcpy(server->entries[server->nentries].hostname, name, size);
  msg->entry_num = server->nentries;
  server->nentries += 1;

  return 0;
}
