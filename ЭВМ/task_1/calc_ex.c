#include "header.h"

// e^x = sum ( x^n/n! )

float calc_ex(int n, int x) {
  float ex = 1;
  int i;
  int j = 1;
  int f = 1;
  float *power = malloc(sizeof(int)*(n + 1));
  float *fact = malloc(sizeof(int)*(n + 1));

  /*Caclulate x^n array*/
  for(i = 0; i <= n; i++){
    power[i] = j;
    j *= x;
  }

  /*Calculate n! array*/
  fact[0] = 1;
  for (i = 1; i <= n; i++) {
    f *= i;
    fact[i] = f;
  }

  /*Calculate ex*/
  if (x != 0) {

    for(i = 1; i <= n; i++){
      printf("e^x = %.2f\n", ex);
      ex += power[i]/fact[i];
    }

  } else return ex;

  free(fact);
  free(power);

  return ex;
}
