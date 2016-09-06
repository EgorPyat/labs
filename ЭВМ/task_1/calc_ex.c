#include "header.h"

// e^x = sum ( x^n/n! )

double calc_ex(int n, int x) {
  double ex = 1;
  int i;
  double j = 1;

  /*Calculate ex*/
  if (x != 0) {
    for (i = 1; i <= n; i++){
      j*=((float)x/(float)i);
      ex+=j;
    }
  } else return ex;

  return ex;
}
