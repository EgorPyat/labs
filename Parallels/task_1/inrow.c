#include <stdio.h>
#include <string.h>
#include <stdlib.h>

double* mul(double *matrix, double *vector, double *result, int N){
  for(int i = 0; i < N; i++){
    for(int j = 0; j < N; j++){
      result[i] += matrix[i*N + j]*vector[j];
    }
  }
  return result;
};

double* sub(double *vec1, double *vec2, double *result, int N){
  for(int i = 0; i < N; i++){
    result[i] = vec1[i] - vec2[i];
  }
  return result;
};

double* scmul(double *vec, double tau, int N){
  for (int i = 0; i < N; i++) {
    vec[i] *= tau;
  }
  return vec;
};

double norm(double *vec1, double *vec2, int N){
  double u = 0;
  double b = 0;
  for(int i = 0; i < N; i++){
    u += vec1[i]*vec1[i];
  }
  for(int i = 0; i < N; i++){
    b += vec2[i]*vec2[i];
  }
  return (u/b);
}

const double EPS = 0.00001;
const double tau_p = +0.001;
const double tau_n = -0.001;

int main (int argc, char* argv[]){
  int N = 8;
  double t =tau_p;
  double *A = (double*)malloc(N*N*sizeof(double));
  double *B = (double*)malloc(N*sizeof(double));
  double *X = (double*)malloc(N*sizeof(double));
  double *S = (double*)malloc(N*sizeof(double));
  double *R = (double*)malloc(N*sizeof(double));
  double u;

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
  }
  memset(X, 0, N*sizeof(double));

  for(;;){
    memset(R, 0, N*sizeof(double));
    memset(S, 0, N*sizeof(double));
    mul(A, X, R, N);
    sub(R, B, S, N);
    u = norm(S, B, N);
    scmul(S, t, N);
    sub(X, S, R, N);
    memcpy(X, R, N*sizeof(double));
    if(u < EPS*EPS) break;
  }
  for(int i = 0; i < N; i++){
    printf("%.10f\n", X[i]);
  }

  free(A);
  free(B);
  free(R);
  free(S);
  free(X);

  return 0;
}
