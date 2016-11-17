#include <stdio.h>
#include <math.h>
#include <stdlib.h>
#include <time.h>

void swap(size_t *a, size_t *b){
  int c;
  c = *a;
  *a = *b;
  *b = c;
}

int main(int argc, char* argv[]){
  int N;
  size_t *a = NULL;
  struct timespec start, end;
  srand(time(0));
  int K = 64;

  for(N = 128; N <= 8192*128; ){
    a = (size_t*)malloc(N*sizeof(size_t));
    if(!a){
      printf("NO MEMORY!\n");
      exit(1);
    }
    /*________________________________________________________*/
    // straight
    for(int o = 0; o < N; o++){
      a[o] = o + 1;
    }
    a[N - 1] = 0;
    clock_gettime(CLOCK_MONOTONIC_RAW, &start);
    for (size_t k = 0, i = 0; i < N*K; i++){
      k = a[k];
    }
	  clock_gettime(CLOCK_MONOTONIC_RAW, &end);
    // printf("%d Кб: Time taken: %.10lf sec.\n",N/128, end.tv_sec-start.tv_sec+ 0.000000001*(end.tv_nsec-start.tv_nsec));

    /*________________________________________________________*/
    // back
    for(int o = N - 1; o >= 0; o--){
      a[o] = o - 1;
    }
    a[0] = N - 1;
    clock_gettime(CLOCK_MONOTONIC_RAW, &start);
    for (int k=0, i=0; i<N*K; i++){
      k = a[k];
    }
	  clock_gettime(CLOCK_MONOTONIC_RAW, &end);
    // printf("%d Кб: Time taken: %.10lf sec.\n",N/128, end.tv_sec-start.tv_sec+ 0.000000001*(end.tv_nsec-start.tv_nsec));

    /*________________________________________________________*/
    // random
    for(int o = 0; o < N; o++){
      a[o] = o + 1;
    }
    a[N - 1] = 0;
    for(int o = 0; o < N; o++){
      int u = rand() % N;
      swap(&a[u], &a[o]);
    }
    clock_gettime(CLOCK_MONOTONIC_RAW, &start);
    for (int k=0, i=0; i<N*K; i++){
      k = a[k];
    }
	  clock_gettime(CLOCK_MONOTONIC_RAW, &end);
	  // printf("%d Кб: Time taken: %.10lf sec.\n",N/128, end.tv_sec-start.tv_sec+ 0.000000001*(end.tv_nsec-start.tv_nsec));

    /*________________________________________________________*/
    free(a);
    if(N < 512*128) N*=2;
    else N+=512*128;
  }
  return 0;
}



// for(int o = 0; o < N; o++){
//   printf("%d", a[o]);
// }
// printf("\n");
