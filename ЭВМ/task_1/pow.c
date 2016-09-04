#include "header.h"

int* power(int x, int n){
  int i;
  int j = 1;
  int *p = malloc(sizeof(int)*(n + 1));

  for(i = 0; i <= n; i++){
    p[i] = j;
    j *= x;
  }
  //printf("%d\n", p[n]);

  return p;

}
