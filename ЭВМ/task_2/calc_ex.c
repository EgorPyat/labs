#include "header.h"
double calc_ex(double n, double x) {

  double ex = 1;
  double i;
  double j = 1;

  /*Calculate ex*/
  if (x != 0) {
    for (i = 1; i < n; i++){
      j*=(x/i);
      ex+=j;
    }
  } else return ex;

  return ex;
}
