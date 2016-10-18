#include "header.h"

int main(int argc, char *argv[]) {
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

  return 0;

}
