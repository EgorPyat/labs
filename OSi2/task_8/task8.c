#include <stdio.h>
#include <pthread.h>
#include <unistd.h>
#include <stdlib.h>
#include <signal.h>

int threads_num = 4;
int stopped = 0;

void* calc_pi(void*);
void sighandler(int);

int main(int argc, char* argv[]){
  double *retval;
  double sum;
  double pi = 0.0;
  int* args;
  pthread_t* threads;

  signal(SIGINT, sighandler);

  if(argc == 2) threads_num = atoi(argv[1]);

  threads = (pthread_t*)malloc(threads_num * sizeof(pthread_t));
  args = (int*)malloc(threads_num * sizeof(int));

  for(int i = 0; i < threads_num; i++){
    args[i] = i;
    if(0 != pthread_create(&threads[i], NULL, calc_pi, (void*)(args + i))) printf("Err\n");
  }

  for(int i = 0; i < threads_num; i++){
    if(0 != pthread_join(threads[i], (void**)(&retval))) printf("Err\n");
    pi += *(double*)retval;
    free(retval);
  }

  printf("PI: %f\n", pi);

  free(args);
  free(threads);

  pthread_exit(NULL);
}

void* calc_pi(void* arg){
  double *pi = (double*)malloc(sizeof(double));
  *pi = 0.0;
  size_t iters = 0;
  size_t cycles = 0;

  for(int i = *(int*)arg; ; i += threads_num){
    ++iters;
    *pi += 1.0/(i*4.0 + 1.0);
    *pi -= 1.0/(i*4.0 + 3.0);
    if(iters == 200000){
      iters = 0;
      ++cycles;
      if(stopped == 1) break;
    }
  }

  *pi = *pi * 4.0;

  printf("THR_N: %d | RES: %f | CYC: %lu\n", *(int*)arg, *pi, cycles);

  pthread_exit((void*)pi);
}

void sighandler(int signum){
  if(stopped == 0){
    sleep(1);
    stopped = 1;
  }
  else exit(0);
}
