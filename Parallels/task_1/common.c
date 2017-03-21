#include <stdio.h>
#include <mpi.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>

double* mul(double *matrix, double *vector, double *result, int N, int rows){
  for(int i = 0; i < rows; i++){
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

const double EPS = 0.00001;
const double tau_p = +0.001;
const double tau_n = -0.001;

int main(int argc, char* argv[]){
  int N = 8;
  int rank, size;

  MPI_Init (&argc, &argv);
  MPI_Comm_rank (MPI_COMM_WORLD, &rank);
  MPI_Comm_size (MPI_COMM_WORLD, &size);

  if(size > N){
    if(rank == 0) printf("Too many threads!\n");
    MPI_Finalize();
    return 0;
  }
  /*____________________________________________________________________________________*/
  int shift = ((rank < N % size) ? 1 : 0);

  double *a = (double*)malloc(N*(N/size + shift)*sizeof(double));
  double *b = (double*)malloc((N)*sizeof(double));
  double *S = (double*)malloc((N/size + shift)*sizeof(double));
  double *R = (double*)malloc((N/size + shift)*sizeof(double));
  double *x = (double*)malloc((N)*sizeof(double));

  int rows = 0;
  for(int i = 0; i < rank; i++){
    rows += N/size + (i < N % size ? 1 : 0);
  }
  for(int i = 0; i < N/size + shift; i++){
    for(int j = 0; j < N; j++){
      if((i*N + j - rows) % (N + 1) == 0){
        a[i*N + j] = 2.0;
      }
      else a[i*N + j] = 1.0;
    }
  }
  double norm_B = 0;
  for(int i = 0; i < N; i++){
     b[i] = N + 1;
     norm_B += b[i]*b[i];
  }
  memset(x, 0, N*sizeof(double));
  /*____________________________________________________________________________________*/

  double t1 = MPI_Wtime();

  /*____________________________________________________________________________________*/
  double norm_U;
  double c;

  int *recvcounts = (int*)malloc(size*sizeof(int));
  memset(recvcounts, 0, size*sizeof(int));
  for(int i = 0; i < size; i++){
    recvcounts[i] = N/size + (i < N % size ? 1 : 0);
  }

  int *displs = (int*)malloc(size*sizeof(int));
  memset(displs, 0, size*sizeof(int));
  int y = 0;
  for(int i = 1; i < size; i++){
    y += N/size + ((i - 1) < N % size ? 1 : 0);
    displs[i] = y;
  }

  for(;;){
    memset(R, 0, (N/size + shift)*sizeof(double));
    memset(S, 0, (N/size + shift)*sizeof(double));

    norm_U = 0;
    mul(a, x, R, N, N/size + shift);

    sub(R, b + rows, S, N/size + shift);
    for(int i = 0; i < N/size + shift; i++){
      norm_U += S[i]*S[i];
    }
    MPI_Allreduce(&norm_U, &c, 1, MPI_DOUBLE, MPI_SUM, MPI_COMM_WORLD);

    scmul(S, tau_p, N/size + shift);
    sub(x + rows, S, R, N/size + shift);
    memcpy(x + rows, R, (N/size + shift)*sizeof(double));

    if(c/norm_B < EPS*EPS){
      int count = 0;
      for(int i = 0; i < N/size + shift; i++){
        // printf("%d %.10f\n", rank, x[rows + i]);
        if(fabs(x[rows + i] - 1) < EPS) continue;
        else {
          ++count;
          break;
        }
      }
      printf("%d\n", count);
      break;
    }

    MPI_Allgatherv(R, N/size + shift, MPI_DOUBLE, x, recvcounts, displs, MPI_DOUBLE, MPI_COMM_WORLD);

  }
  /*____________________________________________________________________________________*/

  double t2 = MPI_Wtime();
  // printf("%d %f\n", rank, t2 - t1);

  MPI_Finalize();

  free(a);
  free(b);
  free(R);
  free(S);
  free(x);

  return 0;
}
// if(rank == 0){
//   int r = N/size + shift;
//   for(int p = 1; p < size; p++){
//     MPI_Recv(x + r, N/size + ((p < N % size) ? 1 : 0), MPI_DOUBLE, p, 0, MPI_COMM_WORLD, 0);
//     r += N/size + ((p < N % size) ? 1 : 0);
//   }
//   for(int p = 1; p < size; p++){
//     MPI_Send(x, N, MPI_DOUBLE, p, 0, MPI_COMM_WORLD);
//   }
// }
// else{
//   MPI_Send(R, N/size + shift, MPI_DOUBLE, 0, 0, MPI_COMM_WORLD);
//   MPI_Recv(x, N, MPI_DOUBLE, 0, 0, MPI_COMM_WORLD, 0);
// }
