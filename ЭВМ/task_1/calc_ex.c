#include "header.h"

// e^x = sum ( x^n/n! )

float calc_ex(int n, int x) {
  float ex = 1;
  int i;
  int j = 1;
  int f = 1;
  long long int *power = (long long int *)malloc(sizeof(long long int)*(n + 1));
  long long int *fact = (long long int *)malloc(sizeof(long long int)*(n + 1));

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
    if(i != fact[i]/fact[i-1]) {
      printf("Oops overflow = %d\n", i);
      exit(OVERFLOW);
    }
  }

  /*Calculate ex*/
  if (x != 0) {

    for(i = 1; i <= n; i++){
      ex += (float)power[i]/(float)fact[i];
    }

  } else return ex;

  printf("%lld\n", fact[n]);
  free(fact);
  free(power);

  return ex;
}
