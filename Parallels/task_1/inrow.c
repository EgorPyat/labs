#include <mpi.h>
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
  int rank, size;
  int N = 8;
  double t = tau_p;
  double A[64] = {
    2.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0,
    1.0, 2.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0,
    1.0, 1.0, 2.0, 1.0, 1.0, 1.0, 1.0, 1.0,
    1.0, 1.0, 1.0, 2.0, 1.0, 1.0, 1.0, 1.0,
    1.0, 1.0, 1.0, 1.0, 2.0, 1.0, 1.0, 1.0,
    1.0, 1.0, 1.0, 1.0, 1.0, 2.0, 1.0, 1.0,
    1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 2.0, 1.0,
    1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 2.0,
  };
  double B[10] = {9.0, 9.0, 9.0, 9.0, 9.0, 9.0, 9.0, 9.0};
  double X[8] = {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
  double S[10];
  double R[10];
  double u;

  MPI_Init (&argc, &argv);
  MPI_Comm_rank (MPI_COMM_WORLD, &rank);
  MPI_Comm_size (MPI_COMM_WORLD, &size);
  printf( "Hello world from process %d of %d\n", rank, size );


  if(rank == 0){
    for(;;){
      memset(R, 0, N*sizeof(double));
      memset(S, 0, N*sizeof(double));
      mul(A, X, R, N);
      sub(R, B, S, N);
      u = norm(S, B, N);
      scmul(S, t, N);
      sub(X, S, R, N);
      memcpy(X, R, N*sizeof(double));
      if(u < EPS) break;
      // for(int i = 0; i < N; i++){
      //   printf("%f ", X[i]);
      // }
      // printf("\n");
      // getchar();
    }
    for(int i = 0; i < N; i++){
      printf("%.10f\n", X[i]);
    }
  }
  else{
    printf("rank = %d\n", rank);
  }
  MPI_Finalize();
  return 0;
}
/*
#include <stdio.h>
#include <mpi.h>
#include <stdlib.h>
#include <string.h>

int main(int argc, char* argv[]){
  int N = 12;
  int rank, size;

  MPI_Init (&argc, &argv);
  MPI_Comm_rank (MPI_COMM_WORLD, &rank);
  MPI_Comm_size (MPI_COMM_WORLD, &size);

  double a[72];
  double b[12] = {13.0, 13.0, 13.0, 13.0, 13.0, 13.0, 13.0,13.0,13.0,13.0,13.0,13.0};
  double x[12];

  printf("rank %d\n", rank);
  for(int i = 0; i < N/size; i++){
    for(int j = 0; j < N; j++){
      if((i*N + j - rank*(N/size)) % (N + 1) == 0){
        a[i*N + j] = 2.0;
      }
      else a[i*N + j] = 1.0;
    }
  }
  for(int i = 0; i < N*N/size; i++){
    printf("%f ", a[i]);
    if((i+1)%N == 0) printf("%d \n", rank);
  }

  // double t1 = MPI_Wtime();
  double ls[6] = {0};
  for(int i = 0; N/size; i++){
    for(int j = 0; j < N; j++){
      ls[i] += a[i*N + j]*b[j];
    }
  }*/

  // MPI_Allgather(ls, N/size, MPI_DOUBLE, x + rank*N/size, N/size, MPI_DOUBLE, MPI_COMM_WORLD);
  // if(rank == 0){
    // memcpy(x, ls, N/size);
    // for(int p = 1; p < size; p++){
      // MPI_Recv(ls, N/size, MPI_DOUBLE, p, 0, MPI_COMM_WORLD, 0);
      // memcpy(x, ls + p*N/size, N/size);
    // }
    // for(int i = 0; i < N; i++) printf("%f ",x[i]);
  // }
  // else{
    // MPI_Send(ls, N/size, MPI_DOUBLE, 0, 0, MPI_COMM_WORLD);
  // }
  // double t2 = MPI_Wtime();
  // printf("%f\n", t2 - t1);

/*  MPI_Finalize();

  return 0;
}*/

/*
char* file = "dat00.txt";
file[4] += rank;
*/

// for(int i = 0; i < N*(N/size + shift); i++){
//   printf("%.1f ", a[i]);
//   if((i+1)%N == 0) printf("%d \n", rank);
// }

// printf("%d %.2f\n", rank, ls[i]);

// for(int i = 0; i < N; i++) printf("%.2f ",x[i]);
// printf("\n");
