#include <stdio.h>
#include <pthread.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>

#define NUM_STEPS 200000000

void* calc_pi(void*);

int main(int argc, char* argv[]){
  int threads_num;
  size_t num_steps = NUM_STEPS;
  double *retval;
  double sum = 0;
  size_t* args;
  pthread_t* threads;

  if(argc == 2) threads_num = atoi(argv[1]);
  else threads_num = 4;

  threads = (pthread_t*)malloc(threads_num * sizeof(pthread_t));
  args = (size_t*)malloc(threads_num * sizeof(size_t));

  for(int i = 0; i < threads_num; i++){
    args[i] = i + 1;
    pthread_create(&threads[i], NULL, calc_pi, args + i);
  }

  for(int i = 0; i < threads_num; i++){
    pthread_join(threads[i], (void**)&retval);
    sum += *(size_t*)retval;
  }

  printf("%f\n", sum);

  free(args);
  free(threads);

  pthread_exit(NULL);
}

void* calc_pi(void* arg){
  size_t* p = (size_t*)arg;
  printf("%ld\n", *p);
  pthread_exit(p);
}

// int
// main(int argc, char** argv) {
//
//     double pi = 0;
//     int i;
//
//     for (i = 0; i < num_steps ; i++) {
//
//          pi += 1.0/(i*4.0 + 1.0);
//          pi -= 1.0/(i*4.0 + 3.0);
//     }
//
//     pi = pi * 4.0;
//     printf("pi done - %.15g \n", pi);
//
//     return (EXIT_SUCCESS);
// }
