#include "header.h"

int main() {
  float ex;
  int n;
  int x;

  do{
    printf("Enter N: ");
    scanf("%d", &n);
    if (n <= 0) printf("N should be >= 0\n");
  }
  while (n < 0);

  //printf("N fact = %d\n", fact(n));
  //fact(n);

  printf("Enter X: ");
  scanf("%d", &x);

  power(x, n);
  printf("e^x = %f", ex);

  return 0;

}
