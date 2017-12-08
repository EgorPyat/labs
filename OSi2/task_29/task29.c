#include <stdio.h>
#include <stdlib.h>
#include <sys/ioctl.h>
#include <sys/poll.h>
#include <sys/socket.h>
#include <sys/time.h>
#include <netinet/in.h>
#include <errno.h>
#include <string.h>
#include <unistd.h>
#include <netdb.h>
#include <arpa/inet.h>

#define SERVER_PORT  3001

#define TRUE  1
#define FALSE 0
#define ENTRIESNUM 10
#define REQUESTSNUM 10

typedef struct{
  int fd;
  char* buffer;
  int size;
} request;

typedef struct{
  char* name;
  char* content;
} proxy_entry;

int create_server(){
  int on = 1;
  int rc;
  struct sockaddr_in   addr;

  int listen_sd = socket(AF_INET, SOCK_STREAM, 0);
  if(listen_sd < 0){
    perror("socket() failed");
    exit(-1);
  }

  rc = setsockopt(listen_sd, SOL_SOCKET,  SO_REUSEADDR, (char *)&on, sizeof(on));
  if(rc < 0){
    perror("setsockopt() failed");
    close(listen_sd);
    exit(-1);
  }

  rc = ioctl(listen_sd, FIONBIO, (char *)&on);
  if(rc < 0){
    perror("ioctl() failed");
    close(listen_sd);
    exit(-1);
  }

  memset(&addr, 0, sizeof(addr));
  addr.sin_family      = AF_INET;
  addr.sin_addr.s_addr = htonl(INADDR_ANY);
  addr.sin_port        = htons(SERVER_PORT);
  rc = bind(listen_sd, (struct sockaddr *)&addr, sizeof(addr));
  if(rc < 0){
    perror("bind() failed");
    close(listen_sd);
    exit(-1);
  }

  rc = listen(listen_sd, 32);
  if(rc < 0){
    perror("listen() failed");
    close(listen_sd);
    exit(-1);
  }
  return listen_sd;
}

void accept_connections(int* listen_sd, struct pollfd * fds, int* nfds, int* end_server){
  int new_sd = -1;
  int rc;
  int on = 1;

  do{
    new_sd = accept(*listen_sd, NULL, NULL);
    if(new_sd < 0){
      if(errno != EWOULDBLOCK){
        perror("  accept() failed");
        *end_server = TRUE;
      }
      break;
    }
    rc = ioctl(new_sd, FIONBIO, (char*)&on);
    if(rc < 0){
      perror("  ioctl() failed");
      close(new_sd);
      break;
    }
    else{
      printf("  New incoming connection - %d\n", new_sd);
      fds[*nfds].fd = new_sd;
      fds[*nfds].events = POLLIN;
      (*nfds)++;
    }
  }
  while(new_sd != -1);
}

int main(int argc, char *argv[]){
  int    len, rc, on = 1;
  int    cache_entry_num = 0;
  int    listen_sd = -1, new_sd = -1;
  int    desc_ready, end_server = FALSE, compress_array = FALSE;
  int    close_conn;
  char   buffer[16384];
  struct sockaddr_in   addr;
  int    timeout;
  struct pollfd fds[200];
  int    nfds = 1, current_size = 0, i, j;

  proxy_entry* cache = (proxy_entry*)malloc(sizeof(proxy_entry) * ENTRIESNUM);
  request* requests = (request*)malloc(sizeof(request) * REQUESTSNUM);

  listen_sd = create_server();

  memset(fds, 0, sizeof(fds));

  fds[0].fd = listen_sd;
  fds[0].events = POLLIN;

  timeout = (3 * 60 * 1000);

  do{
    printf("Waiting on poll()...\n");
    rc = poll(fds, nfds, timeout);

    if(rc < 0){
      perror("  poll() failed");
      break;
    }
    else if(rc == 0){
      printf("  poll() timed out.  End program.\n");
      break;
    }

    current_size = nfds;

    for(i = 0; i < current_size; i++){
      if(fds[i].revents == 0)
        continue;

      if(fds[i].fd == listen_sd){
        printf("  Listening socket is readable\n");

        accept_connections(&listen_sd, fds, &nfds, &end_server);
      }
/*_________________________POLLIN_________________________*/
      else if(fds[i].revents == POLLIN){
        printf("  Descriptor %d is readable\n", fds[i].fd);
        close_conn = FALSE;
        int sum = 0;
        do{
          rc = recv(fds[i].fd, buffer + sum, sizeof(buffer) - sum, 0);
          if(rc < 0){
            if(errno != EWOULDBLOCK){
              perror("  recv() failed");
              close_conn = TRUE;
            }
            else if(errno == EWOULDBLOCK){
              char* end = strstr(buffer, "\r\n\r\n");
              if(end == NULL){
                break;
              }
              printf("%s\n", "would block");
              char* request_body = strchr(buffer, '\n');
              if(request_body != NULL){
                int length = request_body - buffer;
                char* request_head = (char*)malloc(length + 1);
                strncpy(request_head, buffer, length);
                request_head[length] = '\0';

                char *method = strtok(request_head, " ");

                if(strcmp(method, "GET") == 0){
                  // printf("Method not implemented!\n");
                  // close_conn = TRUE;
                  // free(request_head);
                  // break;
                  char *url = strtok(NULL, " ");
                  char *version = strtok(NULL, "\n\0");
                  char *name = strstr(url, "://");
                  char *name_end = strchr(name + 3, '/');

                  length = 0;

                  if (name_end == NULL){
                    length = strlen(name + 3);
                  }
                  else{
                    length = name_end - (name + 3);
                  }

                  char * host_name = (char*)malloc(length + 1);
                  strncpy(host_name, name + 3, length);
                  host_name[length] = '\0';
                  printf("%lu\n", strlen(host_name));
                  printf("\nMETHOD: %s\n", method);
                  printf("URL: %s\n", url);
                  printf("VERSION: %s\n", version);
                  printf("HOSTNAME: %s\n\n", host_name);

                  struct hostent * host_info = gethostbyname(host_name);

                  free(request_head);
                  free(host_name);

                  if(host_info == NULL){
                    printf("  gethostbyname() failed\n");
                    break;
                  }

                  struct sockaddr_in destinationAddress;

                  destinationAddress.sin_family = AF_INET;
                  destinationAddress.sin_port = htons(80);
                  memcpy(&destinationAddress.sin_addr, host_info->h_addr, host_info->h_length);

                  int destnation = socket(AF_INET, SOCK_STREAM, 0);
                  if(destnation < 0){
                    perror(" socket() failed");
                    close_conn = TRUE;
                    break;
                  }

                  rc = ioctl(destnation, FIONBIO, (char *)&on);
                  if(rc < 0){
                    perror("ioctl() failed");
                    close(destnation);
                    close_conn = TRUE;
                    break;
                  }

                  int con = connect(destnation, (struct sockaddr *)&destinationAddress, sizeof(destinationAddress));

                  if(con < 0){
                    if(errno != EINPROGRESS){
                      perror("  connect() failed");
                      close(destnation);
                      close_conn = TRUE;
                      break;
                    }
                    else{
                      fds[nfds].fd = destnation;
                      fds[nfds].events = POLLOUT | POLLIN;
                      requests[nfds].fd = i;
                      requests[nfds].buffer = (char*)malloc(sum);
                      requests[nfds].size = sum;
                      memcpy(requests[nfds].buffer, buffer, sum);
                      nfds++;
                      printf("Connection in progress\n");
                    }
                  }
                  else{
                    fds[nfds].fd = destnation;
                    fds[nfds].events = POLLOUT;
                    requests[nfds].fd = i;
                    requests[nfds].buffer = (char*)malloc(sum);
                    requests[nfds].size = sum;
                    memcpy(requests[nfds].buffer, buffer, sum);
                    nfds++;
                    printf("connected to destnation point\n");
                  }
                }
                else if(strstr(method, "HTTP") != NULL){
                  printf("get response\n");
                  requests[requests[i].fd].fd = -1;
                  requests[requests[i].fd].buffer = (char*)malloc(sum);
                  requests[requests[i].fd].size = sum;
                  memcpy(requests[requests[i].fd].buffer, buffer, sum);
                  fds[requests[i].fd].events = POLLOUT;
                  requests[i].fd = -1;
                  free(requests[i].buffer);
                  requests[i].size = -1;
                  close_conn = TRUE;
                  break;
                }
                else{
                  printf("Method not implemented!\n");
                  close_conn = TRUE;
                  free(request_head);
                  break;
                }

              }
            }
            break;
          }

          if(rc == 0){
            printf("  Connection closed\n");
            close_conn = TRUE;
            break;
          }

          sum += rc;
        }
        while(TRUE);

        if(close_conn){
          close(fds[i].fd);
          fds[i].fd = -1;
          compress_array = TRUE;
        }
      }
      /*________________________________________________________*/
      else if(fds[i].revents == POLLOUT){
        printf("gonna send\n");
        rc = send(fds[i].fd, requests[i].buffer, requests[i].size, 0);
        if(rc < 0){
          perror("  send() failed");
          close_conn = TRUE;
        }
        else{
          if(requests[i].fd == -1){
            close_conn = TRUE;
          }
          else{
            fds[i].events = POLLIN;
          }
        }
        if(close_conn){
          close(fds[i].fd);
          fds[i].fd == -1;
          compress_array = TRUE;
        }
        getchar();
      }
    }

    if(compress_array){
      compress_array = FALSE;
      for(i = 0; i < nfds; i++){
        if(fds[i].fd == -1){
          for(j = i; j < nfds; j++){
            fds[j].fd = fds[j+1].fd;
          }
          nfds--;
        }
      }
    }

  }
  while(end_server == FALSE);

  for(i = 0; i < nfds; i++){
    if(fds[i].fd >= 0)
      close(fds[i].fd);
  }
}
