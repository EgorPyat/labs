#include "proxy.h"
#include <pthread.h>

volatile int end_server = FALSE;
proxy_server server;
pthread_mutex_t synchronizer=PTHREAD_MUTEX_INITIALIZER;

void sighandler(int signum){
  end_server = TRUE;
}

void* connection(void* arg){
  int on = 0;
  int cached = FALSE;
  int* sock = (int*)arg;
  int socket = *sock;
  int conn;
  int status = -1;
  message reqmsg;
  printf("sock %d\n", socket);
  reqmsg.request_fd = -1;
  reqmsg.buffer = (char*)calloc(STARTBUFFERSIZE, sizeof(char));
  reqmsg.size = 0;
  reqmsg.max_size = STARTBUFFERSIZE;

  while(TRUE){
    status = get_request(&reqmsg, socket);
    if(status == -1){
      perror("\tget_request() failed");
      close_connection(&reqmsg, socket);
      pthread_exit(NULL);
    }
    else if(status == 0){
      close_connection(&reqmsg, socket);
      pthread_exit(NULL);
    }
    else if(status == 1){
      continue;
    }
    else if(status == 2){
      printf("REQUEST GOTTEN\n");
      break;
    }
  }

  char* hostname = (char*)malloc(4096);
  char* request_head = (char*)malloc(4096);

  if(parse_request(&reqmsg, hostname, request_head) == -1){
    printf("Wrong request format!\n");
    close_connection(&reqmsg, socket);
    pthread_exit(NULL);
  }
  else{
    // printf("\t\t\t\t%lu\n", strlen(request_head));
    pthread_mutex_lock(&synchronizer);
    status = find_in_cache(request_head, strlen(request_head), &server);
    pthread_mutex_unlock(&synchronizer);
    if(status >= 0 && is_complete_entry(status, &server) == 1){
      pthread_mutex_lock(&synchronizer);
      get_from_cache(status, &reqmsg, &server);
      pthread_mutex_unlock(&synchronizer);
      cached = TRUE;
    }
    else{
      if(status == -1){
        pthread_mutex_lock(&synchronizer);
        cache_entry_name(request_head, strlen(request_head), &server, &reqmsg);
        pthread_mutex_unlock(&synchronizer);
      }
      conn = create_connection(hostname);
      if(conn == -1){
        perror("\tcreate connection() failed");
        close_connection(&reqmsg, socket);
        pthread_exit(NULL);
      }
      printf("created!\n");
    }
  }
  free(hostname);
  free(request_head);

  status = ioctl(socket, FIONBIO, (char *)&on);
  if(status < 0){
    close_connection(&reqmsg, socket);
    pthread_exit(NULL);
  }

  if(cached == FALSE){
    printf("\tDescriptor %d is writable\n\tsocket %d\n", conn, socket);

    printf("conn %d\n", conn);
    status = send(conn, reqmsg.buffer, reqmsg.size, 0);
    printf("%d / %d\n", status, reqmsg.size);
    if(status < 0){
      perror("\tsend() failed");
      close_connection(&reqmsg, conn);
      pthread_exit(NULL);
    }

    message resmsg;
    resmsg.request_fd = socket;
    resmsg.buffer = (char*)calloc(STARTBUFFERSIZE, sizeof(char));
    resmsg.size = 0;
    resmsg.max_size = STARTBUFFERSIZE;
    resmsg.entry_num = reqmsg.entry_num;

    while(TRUE){
      status = get_response(&resmsg, conn, &server);
      if(status == -1){
        perror("\tget_response() failed");
        close_connection(&resmsg, conn);
        pthread_exit(NULL);
      }
      else if(status == 0){
        transfer_response(&reqmsg, &resmsg);
        printf("transfer_response%d\n", conn);
        close_connection(&resmsg, conn);
      }
      else if(status == 1){
        continue;
      }
      break;
    }
  }

  status = send(socket, reqmsg.buffer, reqmsg.size, 0);
  printf("%d / %d\n", status, reqmsg.size);
  if(status < 0){
    perror("\tsend() failed");
    close_connection(&reqmsg, socket);
    pthread_exit(NULL);
  }
  close_connection(&reqmsg, socket);
  free(sock);
}

int main(){
  int status;

  signal(SIGINT, sighandler);

  if(create_server(&server) == -1){
    perror("\tcreate_server() failed");
    return -1;
  }

  while(end_server == FALSE){
    int client = accept_connection(&server);
    if(client == -1){
      continue;
    }
    int* c = (int*)malloc(sizeof(int));
    memcpy(c, &client, sizeof(int));
    pthread_t con;
    printf("acc %d\n", *c);
    if(0 != pthread_create(&con, NULL, connection, (void*)c)){
      printf("Err\n");
    }
  }

  if(close_server(&server) == -1){
    perror("\tclose_server() failed");
    return -1;
  }

  return 0;
}
