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

#define NONE           -1
#define FALSE           0
#define TRUE            1

/*app constants*/

#define REQUEST         2
#define RESPONSE        3
#define WAITCACHE       4
#define SERVER_PORT     3001
#define CONNECTIONS     1024
#define ENTRIESNUM      1024
#define MESSAGESNUM     CONNECTIONS
#define STARTBUFFERSIZE 1048576

/*app data types*/

typedef struct{
  int   request_fd;
  char* buffer;
  int   size;
  int   max_size;
  int   entry_num;
} message;

typedef struct{
  char* hostname;
  int   hostname_size;
  char* content;
  int   content_size;
  int   complete;
  int   max_size;
} proxy_entry;

typedef struct{
  int listen_sd;
  proxy_entry * entries;
  int nentries;
} proxy_server;

/*app functions*/

int  create_server(proxy_server*);
int  close_server(proxy_server*);
int  accept_connection(proxy_server*);
int  close_connection(message*, int);
int  get_request(message*, int);
int  get_response(message*, int, proxy_server*);
int  parse_request(message*, char*, char*);
int  create_connection(char*);
int  transfer_response(message*, message*);
int  find_in_cache(char*, int, proxy_server*);
int  is_complete_entry(int, proxy_server*);
int  get_from_cache(int, message*, proxy_server*);
int  cache_entry_name(char*, int, proxy_server*, message*);

#endif
