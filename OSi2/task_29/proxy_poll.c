#include "proxy.h"

int end_server = FALSE;

void sighandler(int signum){
  end_server = TRUE;
}

int timeout;
int main(){
  proxy_server server;
  int compress  = FALSE;
  int close_con = FALSE;
  int status;

  signal(SIGINT, sighandler);

  timeout = (3 * 60 * 1000);

  if(create_server(&server) == -1){
    perror("\tcreate_server() failed");
    return -1;
  }

  do{
    printf("Waiting on poll()...%d\n", server.nfds);

    status = poll(server.fds, server.nfds, timeout);

    if(status < 0){
      perror("\tpoll() failed");
      end_server = TRUE;
    }
    else if(status == 0){
      printf("\tserver timeout\n");
      end_server = TRUE;
    }
    else{
      server.current_size = server.nfds;

      for(int i = 0; i < server.current_size; i++){
        close_con = FALSE;

        if(server.fds[i].revents == 0){
          continue;
        }
        if(server.fds[i].fd == server.listen_sd){
          printf("  Listening socket is readable\n");

          if(accept_connections(&server) == -1){
            perror(NULL);
            end_server = TRUE;
            break;
          }
          continue;
        }
        if(server.fds[i].revents == POLLIN){
          printf("  Descriptor %d is readable\n", server.fds[i].fd);
          printf("%d\n", server.messages[i].type);

          switch(server.messages[i].type){
            case REQUEST:
              status = get_request(&server.messages[i], server.fds[i].fd);
              if(status == -1){
                perror("\tget_request() failed");
                close_con = TRUE;
              }
              else if(status == 0){
                close_con = TRUE;
              }
              else if(status == 1){
                continue;
              }
              else if(status == 2){
                printf("REQUEST GOTTEN\n");

                // getchar();

                char* hostname = (char*)malloc(1024);
                char* method = (char*)malloc(20);
                char* version = (char*)malloc(20);

                if(parse_request(&server.messages[i], hostname, method, version) == -1){
                  printf("Wrong request format!\n");
                  close_con = TRUE;
                }
                else{
                  printf("\nHOSTNAME: %s %lu\n", hostname, strlen(hostname));
                  printf("METHOD: %s %lu\n", method, strlen(method));
                  printf("VERSION: %s %lu\n\n", version, strlen(version));

                  if(create_connection(&server, hostname, i) == -1){
                    perror("\tcreate connection() failed");
                    close_con = TRUE;
                  }
                  // getchar();

                  free(hostname);
                  free(method);
                  free(version);
                }

              }

              break;
            case RESPONSE:
              status = get_response(&server.messages[i], server.fds[i].fd);
              if(status == -1){
                perror("\tget_response() failed");
                close_con = TRUE;
              }
              else if(status == 0){
                // getchar();
                close_con = TRUE;
                transfer_response(&server, i);
              }
              else if(status == 1){
                continue;
              }
              break;
          }

          if(close_con){
            close_con = FALSE;
            if(close_connection(&server, i) == -1){
              perror("\tclose connection() failed");
            }
            compress = TRUE;
          }
        }
        else if(server.fds[i].revents == POLLOUT){
          printf("\tDescriptor %d is writable\n", server.fds[i].fd);

          status = send(server.fds[i].fd, server.messages[i].buffer, server.messages[i].size, 0);
          printf("%d / %d\n", status, server.messages[i].size);
          if(status < 0){
            perror("\tsend() failed");
            close_con = TRUE;
          }
          else{
            if(server.messages[i].request_fd == -1){
              printf("SEND RESPONSE! %d\n", server.fds[i].fd);
            }
            else{
              printf("SEND REQUEST! %d\n", server.fds[i].fd);

            }
            server.fds[i].events = POLLIN;
            memset(server.messages[i].buffer, 0, server.messages[i].size);
            server.messages[i].size = 0;
          }

          if(close_con){
            close_con = FALSE;
            if(close_connection(&server, i) == -1){
              perror("\tclose connection() failed");
            }
            compress = TRUE;
          }
        }
        else{
          printf("not supp event from: %d\n", server.fds[i].fd);
          perror("Not supported event");

          close_con = TRUE;
          if(close_con){
            close_con = FALSE;
            if(close_connection(&server, i) == -1){
              perror("\tclose connection() failed");
            }
            compress = TRUE;
          }
        }
      }
    }

    if(compress){
      compress = FALSE;
      compress_array(&server);
    }
  }
  while(end_server == FALSE);

  if(close_server(&server) == -1){
    perror("\tclose_server() failed");
    return -1;
  }

  return 0;
}