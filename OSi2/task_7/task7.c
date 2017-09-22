#include <stdio.h>
#include <pthread.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>

size_t num_steps = 200000;
int threads_num = 4;

void* calc_pi(void*);

int main(int argc, char* argv[]){
  double *retval;
  double sum;
  double pi = 0.0;
  int* args;
  pthread_t* threads;

  if(argc == 2) threads_num = atoi(argv[1]);

  threads = (pthread_t*)malloc(threads_num * sizeof(pthread_t));
  args = (int*)malloc(threads_num * sizeof(int));

  for(int i = 0; i < threads_num; i++){
    args[i] = i;
    pthread_create(&threads[i], NULL, calc_pi, (void*)(args + i));
  }

  for(int i = 0; i < threads_num; i++){
    if(0 != pthread_join(threads[i], (void**)(&retval))) printf("Err\n");
    printf("%f\n", *(double*)retval);
    pi += *(double*)retval;
    free(retval);
  }


  printf("PI: %f | ITERS: %ld\n", pi, num_steps);

  free(args);
  free(threads);

  pthread_exit(NULL);
}

void* calc_pi(void* arg){
  double *pi = (double*)malloc(sizeof(double));
  *pi = 0.0;
  for(int i = *(int*)arg; i < num_steps; i += threads_num){
    *pi += 1.0/(i*4.0 + 1.0);
    *pi -= 1.0/(i*4.0 + 3.0);
  }

  *pi = *pi * 4.0;

  printf("THR_N: %d | RES: %f\n", *(int*)arg, *pi);

  pthread_exit((void*)pi);
}
