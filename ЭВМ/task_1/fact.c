#include "header.h"

int* fact(int n) {
  int i;
  int f = 1;
  int *m = malloc(sizeof(int)*n);

  for (i = 1; i <= n; i++) {
    f *= i;
    m[i - 1] = f;
  }

  //printf("%d\n", m[1]);

  return m;
}
