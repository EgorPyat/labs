/*
 * Произведение двух матриц в топологии "двумерная решетка 2x2" компьютеров
 * В примере предполагается, что количество строк матрицы A и количество
 * столбцов матрицы B делятся без остатка на количество компьютеров в системе.
 */
  #include <stdio.h>
  #include <stdlib.h>
  #include <mpi.h>
/* NUM_DIMS - размер декартовой топологии. "двумерная решетка" P0xP1 */
  #define NUM_DIMS 2
  #define P0 2
  #define P1 2
/* Задаем размеры матриц A = MxN, B = NxK и C = MxK (Эти размеры значимы в ветви 0)*/
  #define M 8
  #define N 8
  #define K 8

  #define A(i,j) A[N*i+j]
  #define B(i,j) B[K*i+j]
  #define C(i,j) C[K*i+j]

  #define AA(i,j) AA[n[1]*i+j]
  #define BB(i,j) BB[nn[1]*i+j]
  #define CC(i,j) CC[nn[1]*i+j]

int mult(int *n, double *A, double *B, double *C, int *p, MPI_Comm comm){
  double *AA, *BB, *CC;  /* Локальные подматрицы (полосы) */
  int nn[2];             /* Размеры полос в A и B и подматриц CC в C */
  int coords[2];         /* Декартовы координаты ветвей */
  int rank;              /* Ранг ветвей */
  /* Смещения и размер подматриц CС для сборки в корневом процессе (ветви) */
  int *countc, *dispc, *countb, *dispb;
  /* Типы данных и массивы для создаваемых типов */
  MPI_Datatype typeb, typec, types[2];

  int blen[2];
  int i, j, k;
  int periods[2];
  int remains[2];
  long int sizeofdouble, disp[2];
  /* Коммуникаторы для 2D решетки, для подрешеток 1D, и копии */
  /* коммуникатора comm */
  MPI_Comm comm_2D, comm_1D[2], pcomm;
  /* Создаем новый коммуникатор */
  MPI_Comm_dup(comm, &pcomm);
  /* Нулевая ветвь передает всем ветвям массивы n[] и p[] */
  MPI_Bcast(n, 3, MPI_INT, 0, pcomm);
  MPI_Bcast(p, 2, MPI_INT, 0, pcomm);
  /* Создаем 2D решетку компьютеров размером p[0]*p[1] */
  periods[0] = 0;
  periods[1] = 0;
  MPI_Cart_create(pcomm, NUM_DIMS, p, periods, 0, &comm_2D);
  /* Находим ранги и декартовы координаты ветвей в этой решетке */
  MPI_Comm_rank(comm_2D, &rank);
  MPI_Cart_coords(comm_2D, rank, 2, coords);
  /* Нахождение коммуникаторов для подрешеток 1D для рассылки полос матриц A и B */
  for(i = 0; i < 2; i++){
    for(j = 0; j < 2; j++){
      remains[j] = (i == j);
    }
    MPI_Cart_sub(comm_2D, remains, &comm_1D[i]);
  }
  /* Во всех ветвях задаем подматрицы (полосы) */
  /* Здесь предполагается, что деление без остатка */

  nn[0] = n[0]/p[0];
  nn[1] = n[2]/p[1];

  AA = (double*)malloc(nn[0]*n[1]*sizeof(double));
  BB = (double*)malloc(n[1]*nn[1]*sizeof(double));
  CC = (double*)malloc(nn[0]*nn[1]*sizeof(double));

  if(rank == 0){
    MPI_Type_vector(n[1], nn[1], n[2], MPI_DOUBLE, &types[0]);
    MPI_Type_commit(&types[0]);
    MPI_Type_create_resized(types[0], 0, nn[1]*sizeof(MPI_DOUBLE), &typeb);
    MPI_Type_commit(&typeb);
  /* Вычисление размера подматрицы BB и смещений каждой
   * подматрицы в матрице B. Подматрицы BB упорядочены в B
   * в соответствии с порядком номеров компьютеров в решетке,
   * т.к. массивы расположены в памяти по строкам, то подматрицы
   * BB в памяти (в B) должны располагаться в следующей
   * последовательности:  BB0, BB1,.... */
    dispb = (int*)malloc(p[1] * sizeof(int));
    countb = (int*)malloc(p[1] * sizeof(int));

    for(j = 0; j < p[1]; j++){
     dispb[j] = j;
     countb[j] = 1;
    }

    MPI_Type_vector(n[1], nn[1], n[2], MPI_DOUBLE, &types[0]);
    MPI_Type_commit(&types[0]);
    MPI_Type_create_resized(types[0], 0, nn[1]*sizeof(MPI_DOUBLE), &typec);
    MPI_Type_commit(&typec);

  /* Вычисление размера подматрицы CС и смещений каждой
   * подматрицы в матрице C. Подматрицы CC упорядочены в С
   * в соответствии с порядком номеров компьютеров в решетке,
   * т.к. массивы расположены в памяти по строкам, то подматрицы
   * СС в памяти (в С) должны располагаться в следующей
   * последовательности:  СС0, СС1, СС2, CC3, СС4, СС5, СС6, СС7. */

    dispc = (int*)malloc(p[0]*p[1]*sizeof(int));
    countc = (int*)malloc(p[0]*p[1]*sizeof(int));

    for(i = 0; i < p[0]; i++){
      for(j = 0; j < p[1]; j++){
        dispc[i*p[1]+j] = (i*p[1]*nn[0] + j);
        countc[i*p[1]+j] = 1;
      }
    }
  }
  /* 1. Нулевая ветвь передает (scatter) горизонтальные полосы матрицы A по x координате */
  if(coords[1] == 0){
    MPI_Scatter(A, nn[0]*n[1], MPI_DOUBLE, AA, nn[0]*n[1], MPI_DOUBLE, 0, comm_1D[0]);
  }
  /* 2. Нулевая ветвь передает (scatter) горизонтальные полосы матрицы B по y координате */
  if(coords[0] == 0){
    MPI_Scatterv(B, countb, dispb, typeb, BB, n[1]*nn[1], MPI_DOUBLE, 0, comm_1D[1]);
  }
  /* 3. Передача подматриц  AA в измерении y */
  MPI_Bcast(AA, nn[0]*n[1], MPI_DOUBLE, 0, comm_1D[1]);
  /* 4. Передача подматриц  BB в измерении x */
  MPI_Bcast(BB, n[1]*nn[1], MPI_DOUBLE, 0, comm_1D[0]);
  /* 5. Вычисление подматриц СС в каждой ветви */
  for(i = 0; i < nn[0]; i++){
    for(j = 0; j < nn[1]; j++){
       CC(i,j) = 0.0;
       for(k = 0; k < n[1]; k++){
          CC(i,j) = CC(i,j) + AA(i,k) * BB(k,j);
       }
    }
  }

  MPI_Gatherv(CC, nn[0]*nn[1], MPI_DOUBLE, C, countc, dispc, typec, 0, comm_2D);

  free(AA);
  free(BB);
  free(CC);

  MPI_Comm_free(&pcomm);
  MPI_Comm_free(&comm_2D);

  for(i = 0; i < 2; i++){
    MPI_Comm_free(&comm_1D[i]);
  }

  if(rank == 0){
    free(countc);
    free(dispc);
    MPI_Type_free(&typeb);
    MPI_Type_free(&typec);
    MPI_Type_free(&types[0]);
  }

  return 0;
}

int main(int argc, char *argv[]){
  int size, rank, n[3], p[2], i, j, k;
  int dims[NUM_DIMS];    // число процессов вдоль измерения
  int periods[NUM_DIMS]; // переодичность граничных условий
  int reorder = 0;       // перенумерация процессов
  double *A, *B, *C;

  MPI_Comm comm;         // коммуникатор для решетки

  MPI_Init(&argc, &argv);
  MPI_Comm_size(MPI_COMM_WORLD, &size);
  MPI_Comm_rank(MPI_COMM_WORLD, &rank);

  for(i = 0; i < NUM_DIMS; i++){
   dims[i] = 0;
   periods[i] = 0;
  }

  MPI_Dims_create(size, NUM_DIMS, dims);
  MPI_Cart_create(MPI_COMM_WORLD, NUM_DIMS, dims, periods, reorder, &comm);

  if(rank == 0){
    n[0] = M;
    n[1] = N;
    n[2] = K;
    p[0] = P0;
    p[1] = P1;

    A = (double*)malloc(M*N*sizeof(double));
    B = (double*)malloc(N*K*sizeof(double));
    C = (double*)malloc(M*K*sizeof(double));

    for(i = 0; i < M; i++){
      for(j = 0; j < N; j++){
        A(i,j) = i + 1;
      }
    }
    for(j = 0; j < N; j++){
      for(k = 0; k < K; k++){
        B(j,k) = 21 + j;
      }
    }
    for(i = 0; i < M; i++){
      for(k = 0; k < K; k++){
        C(i,k) = 0.0;
      }
    }
  }

  double t1 = MPI_Wtime();
  mult(n, A, B, C, p, comm);
  double t2 = MPI_Wtime();
  double t3 = t2 - t1;
  double t4;

  MPI_Reduce(&t3, &t4, 1, MPI_DOUBLE, MPI_MAX, 0, MPI_COMM_WORLD);
  if(rank == 0){
    printf("N = %d\nThreads = %d\nTime = %f\n", N, size, t4);
  }
  /*
  if(rank == 0){
    for(i = 0; i < M; i++){
     for(j = 0; j < K; j++){
       printf("%7.1f", A(i,j));
     }
       printf("\n");
    }
    printf("\n");
    for(i = 0; i < M; i++){
     for(j = 0; j < K; j++){
       printf("%7.1f", B(i,j));
     }
       printf("\n");
    }
    printf("\n");
    for(i = 0; i < M; i++){
     for(j = 0; j < K; j++){
       printf("%7.1f", C(i,j));
     }
       printf("\n");
    }
  }
  */

  if(rank == 0){
    free(A);
    free(B);
    free(C);
  }

  MPI_Comm_free(&comm);
  MPI_Finalize();

  return 0;
}
