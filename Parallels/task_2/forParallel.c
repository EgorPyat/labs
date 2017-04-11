#include <stdio.h>
#include <string.h>
#include <omp.h>
#include <stdlib.h>
#include <math.h>

const double EPS = 10e-9;
const double tau_p = 10e-6;
const double tau_n = -0.001;

int main(int argc, char* argv[]){
  int N = 1000;
  double t = tau_p;
  double *A = (double*)malloc(N*N*sizeof(double));
  double *B = (double*)malloc(N*sizeof(double));
  double *X = (double*)calloc(N, sizeof(double));
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
  // memset(X, 0, N*sizeof(double));
  for(int i = 0; i < N; i++){
    X[i] = 0;
  }
  double start = omp_get_wtime();
  for(;;){
    u = 0;
    // memset(R, 0, N*sizeof(double));
    #pragma omp parallel for
    for(int i = 0; i < N; i++){
      R[i] = 0;
    }

    #pragma omp parallel for
    for(int i = 0; i < N; i++){
      for(int j = 0; j < N; j++){
        R[i] += A[i*N + j]*X[j];
      }
    }

    #pragma omp parallel for
    for(int i = 0; i < N; i++){
      R[i] -= B[i];
    }

    #pragma omp parallel for reduction(+:u)
    for(int i = 0; i < N; i++){
      u += R[i]*R[i];
    }

    #pragma omp parallel for
    for (int i = 0; i < N; i++){
      R[i] *= t;
    }

    #pragma omp parallel for
    for(int i = 0; i < N; i++){
      X[i] -= R[i];
    }

    if(u/b < EPS*EPS){
      int count = 0;
      for(int i = 0; i < N; i++){
        // printf("%.10f\n", X[i]);
        if(fabs(X[i] - 1) < EPS) continue;
        else {
          ++count;
          break;
        }
      }
      if(count > 0) printf("%d\n", count);
      break;
    }
  }
  double finish = omp_get_wtime();
  printf("N: %d\nTime: %f\n", N, finish - start);
  free(A);
  free(B);
  free(R);
  free(X);

  return 0;
}
