#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <mpi.h>

const double EPS = 0.00001;

int main(int argc, char *argv[]){
  int N = 64;
  int m;
  int size, rank;
  int i;
  int j;
  double t1, t2, tmax;
  double prev;
  double locmax = 0;
  double globmax;
  int err = 0;

  MPI_Init(&argc, &argv);
  MPI_Comm_size(MPI_COMM_WORLD, &size);
  MPI_Comm_rank(MPI_COMM_WORLD, &rank);

  MPI_Request req[4];
  MPI_Status st[4];

  m = N / size;
  double A[m + 2][N + 2];
  double B[m][N];

  for(i = 0; i < m; i++){
    for(j = 0; j < N; j++){
      B[i][j] = 0;
    }
  }

  for(i = 0; i < m + 2; i++){
    for(j = 0; j < N + 2; j++){
      if(i == 0 && rank == 0) A[i][j] = j;
      else if(i == (m + 1) && rank == size - 1) A[i][j] = (N + 1) + j;
      else if(j == 0) A[i][j] = (i + m * rank);
      else if(j == N + 1) A[i][j] = (i + m * rank) + (N + 1);
      else A[i][j] = 0;
    }
  }

  t1 = MPI_Wtime();

  do {
    for(i = 1; i <= N; i++){
      prev = B[0][i - 1];
      B[0][i - 1] = 0.25 * (A[0][i] + A[2][i] + A[1][i + 1] + A[1][i - 1]);
      if(fabs(B[0][i - 1] - prev) > locmax) locmax = fabs(B[0][i - 1] - prev);
      prev = B[m - 1][i - 1];
      B[m - 1][i - 1] = 0.25 * (A[m][i - 1] + A[m][i + 1] + A[m - 1][i] + A[m + 1][i]);
      if(fabs(B[m - 1][i - 1] - prev) > locmax) locmax = fabs(B[m - 1][i - 1] - prev);
    }

    if(rank != 0) MPI_Isend(&B, N, MPI_DOUBLE, rank - 1, 0, MPI_COMM_WORLD, &req[0]);
    if(rank != size - 1) MPI_Isend(&B[m - 1][0], N, MPI_DOUBLE, rank + 1, 0, MPI_COMM_WORLD, &req[1]);

    if(rank != 0) MPI_Irecv(&A[0][1], N, MPI_DOUBLE, rank - 1, 0, MPI_COMM_WORLD, &req[2]);
    if(rank != size - 1) MPI_Irecv(&A[m + 1][1], N, MPI_DOUBLE, rank + 1, 0, MPI_COMM_WORLD, &req[3]);

    for(i = 2; i <= m - 1; i++){
      for(j = 1; j <= N; j++){
        B[i - 1][j - 1] = 0.25 * (A[i][j - 1] + A[i][j + 1] + A[i - 1][j] + A[i + 1][j]);
      }
    }

    for(i = 1; i <= m; i++){
      for(j = 1; j <= N; j++){
        prev = A[i][j];
        A[i][j] = B[i - 1][j - 1];
        if(fabs(A[i][j] - prev) > locmax) locmax = fabs(A[i][j] - prev);
      }
    }

    for(i = 0; i < 4; i++){
      if(rank != 0 && rank != size - 1) MPI_Wait(&req[i], &st[i]);
    }


    MPI_Allreduce(&locmax, &globmax, 1, MPI_DOUBLE, MPI_MAX, MPI_COMM_WORLD);

    locmax = 0;

  } while(globmax > EPS);

  t2 = MPI_Wtime();
  t2 -= t1;

  MPI_Allreduce(&t2, &tmax, 1, MPI_DOUBLE, MPI_MAX, MPI_COMM_WORLD);

  if(rank == 0) printf("N = %d\nThreads = %d\nTime = %f\n", N, size, tmax);;

  MPI_Finalize();

  return 0;
}
