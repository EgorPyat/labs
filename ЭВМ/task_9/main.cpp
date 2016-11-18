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
  union ticks {
    unsigned long long t64;
    struct s32 {
      long th, tl;
    } t32;
  } start, end;
  srand(time(0));
  int K = 10;
  unsigned long long T, t;

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
    T = -1;
    for(int q = 0; q < 5; ++q) {
      asm("rdtsc\n":"=a"(start.t32.th), "=d"(start.t32.tl));
      for (int k=0, i=0; i<N*K; i++){
        k = a[k];
      }
      asm("rdtsc\n":"=a"(end.t32.th), "=d"(end.t32.tl));
      t = (end.t64 - start.t64) / (N * K);
      if(t < T) {
        T = t;
      }
    }
    // printf("%d Кб: Ticks taken: %llu\n", N/128, T);

    /*________________________________________________________*/
    // back
    for(int o = N - 1; o >= 0; o--){
      a[o] = o - 1;
    }
    a[0] = N - 1;
    T = -1;
    for(int q = 0; q < 5; ++q) {
      asm("rdtsc\n":"=a"(start.t32.th), "=d"(start.t32.tl));
      for (int k=0, i=0; i<N*K; i++){
        k = a[k];
      }
      asm("rdtsc\n":"=a"(end.t32.th), "=d"(end.t32.tl));
      t = (end.t64 - start.t64) / (N * K);
      if(t < T) {
        T = t;
      }
    }
    // printf("%d Кб: Ticks taken: %llu\n", N/128, T);

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
    T = -1;
    for(int q = 0; q < 5; ++q) {
      asm("rdtsc\n":"=a"(start.t32.th), "=d"(start.t32.tl));
      for (int k=0, i=0; i<N*K; i++){
        k = a[k];
      }
      asm("rdtsc\n":"=a"(end.t32.th), "=d"(end.t32.tl));
      t = (end.t64 - start.t64) / (N * K);
      if(t < T) {
        T = t;
      }
    }
    printf("%d Кб: Ticks taken: %llu\n", N/128, T);
    /*________________________________________________________*/
    free(a);
    if(N < 512*128) N*=2;
    else N+=512*128;
  }
  return 0;
}
