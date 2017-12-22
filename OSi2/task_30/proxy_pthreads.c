#include "proxy.h"
#include <pthread.h>

volatile int end_server = FALSE;
proxy_server server;

void sighandler(int signum){
  end_server = TRUE;
}

void* connection(void* arg){
  int socket = *(int*)arg;
  int conn;
  int status = -1;
  message reqmsg;

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
    status = find_in_cache(request_head, strlen(request_head), &server);
    if(status >= 0 && is_complete_entry(status, &server) == 1){
      get_from_cache(status, &reqmsg, &server);
    }
    else{
      if(status == -1){
        cache_entry_name(request_head, strlen(request_head), &server, &reqmsg);
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

  printf("\tDescriptor %d is writable\n", conn);

  message resmsg;
  resmsg.request_fd = socket;
  resmsg.buffer = (char*)calloc(STARTBUFFERSIZE, sizeof(char));
  resmsg.size = 0;
  resmsg.max_size = STARTBUFFERSIZE;

  status = send(conn, reqmsg.buffer, reqmsg.size, 0);
  printf("%d / %d\n", status, reqmsg.size);
  if(status < 0){
    perror("\tsend() failed");
    close_connection(&reqmsg, conn);
    pthread_exit(NULL);
  }

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

  status = send(socket, reqmsg.buffer, reqmsg.size, 0);
  printf("%d / %d\n", status, reqmsg.size);
  if(status < 0){
    perror("\tsend() failed");
    close_connection(&reqmsg, socket);
    pthread_exit(NULL);
  }
  close_connection(&reqmsg, socket);
}

int main(){
  int status;

  signal(SIGINT, sighandler);

  if(create_server(&server) == -1){
    perror("\tcreate_server() failed");
    return -1;
  }

  while(end_server){
    int* client;
    *client = accept_connection(&server);
    if(*client == -1){
      continue;
    }
    pthread_t con;
    if(0 != pthread_create(&con, NULL, connection, (void*)client)){
      printf("Err\n");
    }
  }

  if(close_server(&server) == -1){
    perror("\tclose_server() failed");
    return -1;
  }

  return 0;
}
