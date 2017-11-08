#include <stdio.h>
#include <unistd.h>
#include <semaphore.h>
#include <pthread.h>
#include <signal.h>

sem_t semA, semB, semC, semAB;
pthread_t threadA;
pthread_t threadB;
pthread_t threadC;

volatile int flag = 1;

void handler(int sig){
  flag = 0;
}

void* createA(void* argv){
  int i = 0;
  while(flag){
    sleep(1);
    if(0 != sem_post(&semA)){
      printf("Err\n");
      return NULL;
    }
    printf("A#%d\n", i++);
  }
}

void* createB(void* argv){
  int i = 0;
  while(flag){
  	sleep(2);
    if(0 != sem_post(&semB)){
      printf("Err\n");
      return NULL;
    }
  	printf("B#%d\n", i++);
  }
}

void* createC(void* argv){
  int i = 0;
  while(flag){
    sleep(3);
    if(0 != sem_wait(&semA)){
      printf("Err\n");
      return NULL;
    }
    if(0 != sem_wait(&semB)){
      printf("Err\n");
      return NULL;
    }
    if(0 != sem_post(&semC)){
      printf("Err\n");
      return NULL;
    }
    printf("C#%d\n", i++);
  }
}

void* createW(){
  int i = 0;
  while(flag){
    if(0 != sem_wait(&semC)){
      printf("Err\n");
      return NULL;
    }
    printf("W#%d\n", i++);
  }
}

int main(){
  if(0 != sem_init(&semA, 0, 0)){
    printf("Err\n");
    return -1;
  }
  if(0 != sem_init(&semB, 0, 0)){
    printf("Err\n");
    return -1;
  }
  if(0 != sem_init(&semC, 0, 0)){
    printf("Err\n");
    return -1;
  }
  if(0 != pthread_create(&threadA, NULL, createA, NULL)){
    printf("Err\n");
    return -1;
  }
  if(0 != pthread_create(&threadB, NULL, createB, NULL)){
    printf("Err\n");
    return -1;
  }
  if(0 != pthread_create(&threadC, NULL, createC, NULL)){
    printf("Err\n");
    return -1;
  }
  createW();
  if(0 != sem_destroy(&semA)){
    printf("Err\n");
  }
  if(0 != sem_destroy(&semB)){
    printf("Err\n");
  }
  if(0 != sem_destroy(&semC)){
    printf("Err\n");
  }
  if(0 != sem_destroy(&semAB)){
    printf("Err\n");
  }
  return 0;
}
