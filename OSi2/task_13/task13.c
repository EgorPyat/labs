#include <pthread.h>
#include <stdio.h>
#include <semaphore.h>
#include <string.h>

sem_t p;
sem_t c;

void* print_message(void* str){
    for (int i = 0; i < 10; i++){
      sem_wait(&p);
      printf("Message : %s\n",(char*)str);
      sem_post(&c);
    }
    return NULL;
}

int main(int argc, char* argv){
    pthread_t thread;
    int i = 0;
    int err;

    sem_init(&p, 0, 0);
    sem_init(&c, 0, 1);

    if(0 != pthread_create(&thread, NULL, print_message, (void*)"child")){
      printf("Error\n");
    }

    for(int i = 0; i < 10; i++){
      sem_wait(&c);
      printf("Message : parent\n");
      sem_post(&p);
    }

    pthread_join(thread, NULL);

    sem_destroy(&p);
    sem_destroy(&c);

    pthread_exit(NULL);
}
