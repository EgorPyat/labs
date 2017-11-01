#include <stdio.h>
#include <unistd.h>
#include <semaphore.h>
#include <pthread.h>

sem_t semA, semB, semC, semAB;

void* createA(void* argv){
  int i = 0;
  while(1){
    sleep(1);
    if(0 != sem_post(&semA)){
      printf("Err\n");
      return -1;
    }
    printf("A#%d\n", i++);
  }
}

void* createB(void* argv){
  int i = 0;
  while(1){
  	sleep(2);
    if(0 != sem_post(&semB)){
      printf("Err\n");
      return -1;
    }
  	printf("B#%d\n", i++);
  }
}

void* createC(void* argv){
  int i = 0;
  while(1){
    sleep(3);
    if(0 != sem_wait(&semA)){
      printf("Err\n");
      return -1;
    }
    if(0 != sem_wait(&semB)){
      printf("Err\n");
      return -1;
    }
    if(0 != sem_post(&semC)){
      printf("Err\n");
      return -1;
    }
    printf("C#%d\n", i++);
  }
}

void* createW(){
  int i = 0;
  while(1){
    if(0 != sem_wait(&semC)){
      printf("Err\n");
      return -1;
    }
    printf("W#%d\n", i++);
  }
}

int main(){
  pthread_t threadA;
  pthread_t threadB;
  pthread_t threadC;

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

  return 0;
}
