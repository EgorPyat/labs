#include <math.h>
#include <stdio.h>
#include <string.h>
#include <omp.h>
#include <stdlib.h>

const double EPS = 0.00001;
const double tau_p = +0.001;
const double tau_n = -0.001;

int main(int argc, char* argv[]){
  int N = 10;
  double t = tau_p;
  double *A = (double*)malloc(N*N*sizeof(double));
  double *B = (double*)malloc(N*sizeof(double));
  double *X = (double*)malloc(N*sizeof(double));
  // double *S = (double*)malloc(N*sizeof(double));
  double *R = (double*)malloc(N*sizeof(double));
  double b = 0;
  double u = 0;

  for(int i = 0; i < N; i++){
    for(int j = 0; j < N; j++){
      if((i*N + j) % (N + 1) == 0){
        A[i*N + j] = 2.0;
      }
      else A[i*N + j] = 1.0;
    }
  }

  for(int i = 0; i < N; i++){
     B[i] = N + 1;
     b += B[i]*B[i];
  }
  memset(X, 0, N*sizeof(double));
  memset(R, 0, N*sizeof(double));

  int f = 1;
  #pragma omp parallel
  {
    while(f){
      #pragma omp single
      {
        memset(R, 0, N*sizeof(double));
        u = 0;
      }
      #pragma omp for
      for(int i = 0; i < N; i++){
        for(int j = 0; j < N; j++){
          R[i] += A[i*N + j]*X[j];
        }
      }

      #pragma omp for
      for(int i = 0; i < N; i++){
        R[i] -= B[i];
      }

      #pragma omp for reduction(+:u)
      for(int i = 0; i < N; i++){
        u += R[i]*R[i];
      }

      #pragma omp for
      for (int i = 0; i < N; i++){
        R[i] *= t;
      }

      #pragma omp for
      for(int i = 0; i < N; i++){
        X[i] -= R[i];
      }

      #pragma omp single
      {
        if(u/b < EPS*EPS){
          f = 0;
        }
      }
    }
  }

  int count = 0;
  for(int i = 0; i < N; i++){
    printf("%.10f\n", X[i]);
    if(fabs(X[i] - 1) < EPS) continue;
    else {
      ++count;
      break;
    }
  }
  printf("%d\n", count);

  free(A);
  free(B);
  free(R);
  // free(S);
  free(X);

  return 0;
}

// int sg = omp_get_num_threads();
// #pragma omp single
// {
//   printf("Num thread = %d\n", sg);
// }

// // OpenMP: openmp.org
// #include <omp.h>
// #include <stdio.h>
// int main(){
//   int n = 200000;
//   double a[n];
//   double b[n];
//   double c[n];
//
//   #pragma omp parallel for
//   for(int i = 0; i < n; i++){
//     // a[i] = b[i]*c[i];
//   }
//   #pragma omp parallel
//   printf("a\n");
//   // for(int i = 0; i < n; i++){
//   //   printf("%f ", a[i]);
//   // }
//   // printf("\n");
//   return 0;
// }
//
// // #pragma parallel
// // {
// //   #pragma omp for
// //   for(){}
// //   #pragma omp parallel for reduction(+:sum)
// // }
// // export OMP_THREAD_NUM=4
// // omp_set_num_threads(4)
// // omp_get_num_threads()
// // omp_get_threads_num()
