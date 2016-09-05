#include "header.h"

int main() {
  float ex;
  int n;
  int x;

  union ticks{
    unsigned long long t64;
    struct s32 {
      long th, tl;
    } t32;
  } start, end;

  double cpu_Hz = 2500000000ULL; // for 2.5 GHz CPU

  do{
    printf("Enter N: ");
    scanf("%d", &n);
    if (n <= 0) printf("N should be >= 0\n");
  }
  while (n < 0);

  printf("Enter X: ");
  scanf("%d", &x);

  asm("rdtsc\n":"=a"(start.t32.th),"=d"(start.t32.tl));
  // some work

  ex = calc_ex(n, x);

  asm("rdtsc\n":"=a"(end.t32.th),"=d"(end.t32.tl));

  printf("e^x = %f\n", ex);
  printf("Time taken: %lf sec.\n",(end.t64-start.t64)/cpu_Hz);

  return 0;

}
