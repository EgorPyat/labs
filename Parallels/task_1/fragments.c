#include <stdio.h>
#include <mpi.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>

double* mul(double *matrix, double *vector, double *result, int cols, int rows, int sh, int N){
  for(int i = 0; i < rows; i++){
    for(int j = 0; j < cols; j++){
      result[i] += matrix[i*N + (j + sh)]*vector[j];
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

  double a[N*(N/size + shift)];
  double b[N/size + shift];
  double x[N/size + 1];

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
  double nB;
  for(int i = 0; i < N/size + shift; i++){
     b[i] = N + 1;
     norm_B += b[i]*b[i];
  }

  MPI_Allreduce(&norm_B, &nB, 1, MPI_DOUBLE, MPI_SUM, MPI_COMM_WORLD);
  memset(x, 0, (N/size + 1)*sizeof(double));

  /*____________________________________________________________________________________*/

  double t1 = MPI_Wtime();

  /*____________________________________________________________________________________*/
  double S[N/size + shift];
  double R[N/size + shift];
  double norm_U;
  double c;

  int shift_ = 0;
  int rank_ = 0;

  for(;;){
    norm_U = 0;
    memset(R, 0, (N/size + shift)*sizeof(double));
    memset(S, 0, (N/size + shift)*sizeof(double));

    shift_ = shift;

    for(int p = 0; p < size; p++){
// double* mul(double *matrix, double *vector, double *result, int cols, int rows, int sh, int N)
      mul(a, x, R, N/size + shift_, N/size + shift, rows, N);
      MPI_Send(x, N/size + 1, MPI_DOUBLE, (rank + 1) % size, 0, MPI_COMM_WORLD);
      MPI_Recv(x, N/size + 1, MPI_DOUBLE, (rank - 1 + size) % size, 0, MPI_COMM_WORLD, 0);

      shift_ = ((rank - 1 - p + size) % size < N % size) ? 1 : 0;
      rank_ = (rank - 1 - p + size) % size;
      rows = 0;

      for(int i = 0; i < rank_; i++){
        rows += N/size + (i < N % size ? 1 : 0);
      }
      // printf("%d multy\n", rank);

    }

    // printf("done\n");
    // for(int i = 0; i < N/size + shift; i++){
    //   printf("%d %.10f\n", rank, x[i]);
    // }
    // getchar();

    sub(R, b, S, N/size + shift);

    for(int i = 0; i < N/size + shift; i++){
      norm_U += S[i]*S[i];
    }
    MPI_Allreduce(&norm_U, &c, 1, MPI_DOUBLE, MPI_SUM, MPI_COMM_WORLD);

    scmul(S, tau_p, N/size + shift);
    sub(x, S, R, N/size + shift);
    memcpy(x, R, (N/size + shift)*sizeof(double));


    if(c/nB < EPS*EPS){
      for(int i = 0; i < N/size + shift; i++){
        printf("%d %.10f\n", rank, x[i]);
      }
      break;
    }
    //
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
  }
  /*____________________________________________________________________________________*/

  double t2 = MPI_Wtime();
  // printf("%d %f\n", rank, t2 - t1);

  MPI_Finalize();

  return 0;
}
