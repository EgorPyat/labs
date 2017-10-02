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
  
  dim2* two = (dim2*)malloc(sizeof(dim2)*N);
  // dim3 three[N];
  // dim4 four[N];
  // dim5 five[N];
  // dim6 six[N];

  double* x = (double*)malloc(sizeof(double) * 1024 * 1024);
  // double y[N];
  // double z[N];
  // double u[N];
  // double v[N];
  // double w[N];
  // asm("rdtsc\n":"=a"(start.t32.th), "=d"(start.t32.tl));
  // for(int i = 0; i < 2; i++){
  //   two[i].x = x[i];
  //   two[i].y = y[i];
  // }
    printf("%lu\n", sizeof(dim3));
  // asm("rdtsc\n":"=a"(end.t32.th), "=d"(end.t32.tl));
  // t = (end.t64 - start.t64);
  // printf("%llu\n", t);

  // printf("%lu\n", 100*1024*1024);

  return 0;
}
