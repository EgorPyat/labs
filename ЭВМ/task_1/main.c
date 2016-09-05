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

  printf("Enter X: ");
  scanf("%d", &x);

  ex = calc_ex(n, x);
  printf("e^x = %f", ex);

  return 0;

}
