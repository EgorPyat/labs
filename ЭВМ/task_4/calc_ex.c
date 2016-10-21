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
// Попытка векторизовать
// #include "header.h"
//
// double calc_ex(double n, double x) {
//
//   double ex = 1;
//   double i;
//   double j1 = 1, j2 = 1;
//
//   /*Calculate ex*/
//   if (x != 0) {
//     for (i = 1; i < n; i+=2){
//       j1*=(x/i);
//       j2*=(x/(i+1));
//       ex+=j1+j2;
//     }
//   } else return ex;
//
//   return ex;
// }
