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
      return NO_ARGS;
    }

    clock_gettime(CLOCK_MONOTONIC_RAW, &start);
    // some work

    ex = calc_ex(n, x);

    clock_gettime(CLOCK_MONOTONIC_RAW, &end);

    printf("Time taken: %.10lf sec.\n",end.tv_sec-start.tv_sec+ 0.000000001*(end.tv_nsec-start.tv_nsec));

    printf("e^x = %.10f\n", ex);

  } else printf("No arguments or too many!");

  return 0;

}
