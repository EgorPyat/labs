#include "header.h"
int main(int argc, char *argv[]) {
  double ex;
  double n;
  double x;
  struct timespec start, end;
  if (argc == 3 ) {

    n = atof(argv[1]);
    x = atof(argv[2]);

    if(n <= 0){
      printf("Enter N > 0");
      return BAD_ARGS;
    }
    clock_gettime(CLOCK_MONOTONIC_RAW, &start);

    ex = calc_ex(n, x);

    clock_gettime(CLOCK_MONOTONIC_RAW, &end);

    printf("Time taken: %.10lf sec.\n",end.tv_sec-start.tv_sec+ 0.000000001*(end.tv_nsec-start.tv_nsec));

    printf("e^x = %.10f\n", ex);

  } else printf("Bad arguments");

  return 0;

}
//using rdtsc
//#include <iostream>
/*
union ticks {
unsigned long long t64;

struct s32 {
unsigned long th, tl;
} t32;
} rdtsc_start, rdtsc_end;

double cpu_Hz = 2500000000ULL;
*/
/*
asm("rdtsc\n":"=a"(rdtsc_start.t32.tl),"=d"(rdtsc_start.t32.th));
std::cerr << "tl = " << rdtsc_start.t32.tl << "\n"
<< "th = " << rdtsc_start.t32.th << "\n"
<< "t64 = " << rdtsc_start.t64 << "\n"
<< "exp = " << ( rdtsc_start.t32.tl |
(rdtsc_start.t32.th<<32))

<< "\n";
rdtsc_start.t64 = rdtsc_start.t32.tl | (rdtsc_start.t32.th<<32);
std::cerr << "t64 now = " << rdtsc_start.t64 << "\n";
ex = calc_ex(n, x);
*/
/*
asm("rdtsc\n":"=a"(rdtsc_end.t32.tl),"=d"(rdtsc_end.t32.th));
std::cerr << "tl = " << rdtsc_end.t32.tl
<< "th = " << rdtsc_end.t32.th
<< "t64 = " << rdtsc_end.t64
<< "\n";

rdtsc_end.t64 = rdtsc_end.t32.tl | (rdtsc_end.t32.th<<32);
printf("Time taken: %lf sec.\n", (rdtsc_end.t64-rdtsc_start.t64)/cpu_Hz);
*/
