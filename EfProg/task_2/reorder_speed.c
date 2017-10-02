#include "header.h"
#include <malloc.h>
int main(){
  unsigned long long t;
  union ticks {
    unsigned long long t64;
    struct s32 {
      long th, tl;
    } t32;
  } start, end;
  double cpu_Hz = 3300000000ULL; //3.3 GHz

  dim2* two = (dim2*)malloc(sizeof(dim2) * N);
  double* x = (double*)malloc(sizeof(double) * N);
  double* y = (double*)malloc(sizeof(double) * N);

  asm("rdtsc\n":"=a"(start.t32.th), "=d"(start.t32.tl));
  for(int i = 0; i < 13107200 ; i++){
    two[i].x = x[i];
    two[i].y = y[i];
  }
  asm("rdtsc\n":"=a"(end.t32.th), "=d"(end.t32.tl));

  t = (end.t64 - start.t64);

  printf("%llu\n", t / N / 2);
  printf("%f\n", 200 / (t / cpu_Hz));

  free(two);
  free(x);
  free(y);

  return 0;
}
