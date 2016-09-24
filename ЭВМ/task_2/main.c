#include "header.h"

int main(int argc, char *argv[]) {
  struct timespec start, end;

  clock_gettime(CLOCK_MONOTONIC_RAW, &start);

  double ex;
  double n;
  double x;

  if (argc == 3 ) {

    n = atof(argv[1]);
    x = atof(argv[2]);

    if(n <= 0){
      printf("Enter N > 0");
      return BAD_ARGS;
    }

    ex = calc_ex(n, x);

    printf("e^x = %.10f\n", ex);

  } else printf("Bad arguments");

  clock_gettime(CLOCK_MONOTONIC_RAW, &end);

  printf("Time taken: %.10lf sec.\n",end.tv_sec-start.tv_sec+ 0.000000001*(end.tv_nsec-start.tv_nsec));

  return 0;
}
