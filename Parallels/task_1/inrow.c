//#include <mpi.h>
#include <stdio.h>
#include <math.h>
#include <string.h>

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
  u = sqrt(u);
  for(int i = 0; i < N; i++){
    b += vec2[i]*vec2[i];
  }
  b = sqrt(b);
  return (u/b);
}

const double EPS = 0.00001;
const double tau_p = +0.001;
const double tau_n = -0.001;

int main (int argc, char* argv[]){
  int N = 7;
  double t = tau_p;
  double A[49] = {
    2.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0,
    1.0, 2.0, 1.0, 1.0, 1.0, 1.0, 1.0,
    1.0, 1.0, 2.0, 1.0, 1.0, 1.0, 1.0,
    1.0, 1.0, 1.0, 2.0, 1.0, 1.0, 1.0,
    1.0, 1.0, 1.0, 1.0, 2.0, 1.0, 1.0,
    1.0, 1.0, 1.0, 1.0, 1.0, 2.0, 1.0,
    1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 2.0,
  };
  double B[7] = {8.0, 8.0, 8.0, 8.0, 8.0, 8.0, 8.0};
  double X[7] = {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
  double S[7];
  double R[7];
  double u;

  for(;;){
    mul(A, X, R, N);
    sub(R, B, S, N);
    u = norm(S, B, N);
    // printf("Norm = %f\n", u);
    if(u < EPS) break;
    scmul(S, t, N);
    sub(X, S, R, N);
    memcpy(X, R, N*sizeof(double));
  }
  for(int i = 0; i < N; i++){
    printf("%f\n", X[i]);
  }

  return 0;
}

/*
int rank, size;

MPI_Init (&argc, &argv);
MPI_Comm_rank (MPI_COMM_WORLD, &rank);
MPI_Comm_size (MPI_COMM_WORLD, &size);
printf( "Hello world from process %d of %d\n", rank, size );
MPI_Finalize();
*/
