#ifndef PROXY_H
#define PROXY_H

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
#include <signal.h>

/*auxiliary constants*/

#define FALSE           0
#define TRUE            1

/*app constants*/

#define REQUEST         2
#define RESPONSE        3
#define SERVER_PORT     3001
#define CONNECTIONS     100
#define ENTRIESNUM      100
#define MESSAGESNUM     CONNECTIONS
#define STARTBUFFERSIZE 16384

/*app data types*/

typedef struct{
  int   request_fd;
  char* buffer;
  int   size;
  int   max_size;
  int   type;
} message;

typedef struct{
  char* hostname;
  char* content;
  int   size;
} proxy_entry;

typedef struct{
  int listen_sd;
  struct pollfd* fds;
  int nfds;
  proxy_entry * entries;
  int nentries;
  message* messages;
  int nmsg;
  int current_size;
} proxy_server;

/*app functions*/

int create_server(proxy_server*);
int close_server(proxy_server*);
int accept_connections(proxy_server*);
int close_connection(proxy_server*, int);
void compress_array(proxy_server*);
int get_request(message*, int);
int get_response(message*, int);
int parse_request(message*, char*, char*, char*);
int create_connection(proxy_server*, char*, int);
int transfer_response(proxy_server*, int);

#endif
