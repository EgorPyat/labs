#include <stdio.h>
#include <string.h>
#include <omp.h>
#include <stdlib.h>
#include <math.h>

const double EPS = 0.00001;
const double tau_p = +0.001;
const double tau_n = -0.001;

int main(int argc, char* argv[]){
  int N = 10;
  double t = tau_p;
  double *A = (double*)malloc(N*N*sizeof(double));
  double *B = (double*)malloc(N*sizeof(double));
  double *X = (double*)malloc(N*sizeof(double));
  double *S = (double*)malloc(N*sizeof(double));
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

  for(;;){
    u = 0;
    memset(R, 0, N*sizeof(double));
    memset(S, 0, N*sizeof(double));

    #pragma omp parallel for
    for(int i = 0; i < N; i++){
      for(int j = 0; j < N; j++){
        R[i] += A[i*N + j]*X[j];
      }
    }

    #pragma omp parallel for
    for(int i = 0; i < N; i++){
      S[i] = R[i] - B[i];
    }

    #pragma omp parallel for reduction(+:u)
    for(int i = 0; i < N; i++){
      u += S[i]*S[i];
    }

    #pragma omp parallel for
    for (int i = 0; i < N; i++){
      S[i] *= t;
    }

    #pragma omp parallel for
    for(int i = 0; i < N; i++){
      R[i] = X[i] - S[i];
    }

    memcpy(X, R, N*sizeof(double));
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
      printf("%d\n", count);
      break;
    }
  }

  free(A);
  free(B);
  free(R);
  free(S);
  free(X);

  return 0;
}
