#include "proxy.h"

int end_server = FALSE;

void sighandler(int signum){
  end_server = TRUE;
}

int timeout;
int main(){
  proxy_server server;
  int compress         = FALSE;
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
        if(server.fds[i].revents == POLLIN){
          printf("  Descriptor %d is readable\n", server.fds[i].fd);

          if(get_header(server.messages[i].buffer, server.fds[i].fd) == -1){
            perror("\tget_header() failed");
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
