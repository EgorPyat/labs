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

#define SERVER_PORT  3001

#define TRUE  1
#define FALSE 0
#define ENTRIESNUM 10

typedef struct{
  char* name;
  char* content;
} proxy_entry;

int main(int argc, char *argv[]){
  int    len, rc, on = 1;
  int    cache_entry_num = 0;
  int    listen_sd = -1, new_sd = -1;
  int    desc_ready, end_server = FALSE, compress_array = FALSE;
  int    close_conn;
  char   buffer[4096];
  struct sockaddr_in   addr;
  int    timeout;
  struct pollfd fds[200];
  int    nfds = 1, current_size = 0, i, j;

  proxy_entry* cache = (proxy_entry*)malloc(sizeof(proxy_entry) * ENTRIESNUM);

  listen_sd = socket(AF_INET, SOCK_STREAM, 0);
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

    if(rc == 0){
      printf("  poll() timed out.  End program.\n");
      break;
    }

    current_size = nfds;

    for(i = 0; i < current_size; i++){
      if(fds[i].revents == 0)
        continue;

      if(fds[i].revents != POLLIN){
        printf("  Error! revents = %d\n", fds[i].revents);
        end_server = TRUE;
        break;
      }

      if(fds[i].fd == listen_sd){
        printf("  Listening socket is readable\n");

        do{
          new_sd = accept(listen_sd, NULL, NULL);
          if(new_sd < 0){
            if(errno != EWOULDBLOCK){
              perror("  accept() failed");
              end_server = TRUE;
            }
            errno = 0;
            break;
          }
          rc = ioctl(new_sd, FIONBIO, (char *)&on);
          if(rc < 0){
            perror("ioctl() failed");
            close(new_sd);
            // exit(-1);
          }
          else{
            printf("  New incoming connection - %d\n", new_sd);
            fds[nfds].fd = new_sd;
            fds[nfds].events = POLLIN;
            nfds++;
          }
        }
        while(new_sd != -1);
      }
      else{
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
              printf("%s\n", "would block");
              char* request_body = strchr(buffer, '\n');
              if(request_body != NULL){
                int length = request_body - buffer;
                char* request_head = (char*)malloc(length + 1);
                strncpy(request_head, buffer, length);
                request_head[length] = '\0';

                char *method = strtok(request_head, " ");
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
                printf("SITENAME: %s\n\n", host_name);

                free(request_head);
                free(host_name);
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

        // for(int i = 0; i < sum; i++) {
        //   printf("%c", buffer[i]);
        // }
        /*GET*/
        /*Cache*/
        /*Send*/

        if(close_conn){
          close(fds[i].fd);
          fds[i].fd = -1;
          compress_array = TRUE;
        }
      }
    }

    if(compress_array){
      compress_array = FALSE;
      for(i = 0; i < nfds; i++){
        if (fds[i].fd == -1){
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
