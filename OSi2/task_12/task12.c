#include <pthread.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h>

pthread_mutex_t m1 = PTHREAD_MUTEX_INITIALIZER;
pthread_cond_t cond = PTHREAD_COND_INITIALIZER;

void* print_message (void* str){
    pthread_mutex_lock(&m1);
    for(int i = 0; i < 10; i++){
      pthread_cond_signal(&cond);
      // printf("%s\n", "signalc");
      // for(;;){};
      pthread_cond_wait(&cond, &m1);
      write(1, "Message : child\n", 16);
    }
    pthread_mutex_unlock(&m1);
    pthread_cond_signal(&cond);
    return NULL;
}

int main (int argc, char* argv){
    pthread_t thread;

    pthread_mutex_lock(&m1);

    if(0 != pthread_create(&thread, NULL, print_message, NULL)){
      printf("Error\n");
    }

    for(int i = 0; i < 10; i++){
      pthread_cond_signal(&cond);
      // printf("%s\n", "signalp");
      pthread_cond_wait(&cond, &m1);
      write(1, "Message : parent\n", 17);
    }

    pthread_mutex_unlock(&m1);
    pthread_cond_signal(&cond);

    pthread_join(thread, NULL);
    pthread_mutex_destroy(&m1);

    return 0;
}
