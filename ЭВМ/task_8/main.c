#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <time.h>
#include <cblas.h> // libblas-dev liblapack-dev
#include <xmmintrin.h>

void print(int N, char * name, double* array);
void matr_mk(int N, double* a);

void inverse(double *a, int N, int M);
void blas_inverse(double *a, int N, int M);
void hand_inverse(double *a, int N, int M);

double get_aij(double *matr, int row, int column, int N);
void transpose(double* a, double* t, int N);
void sum(double *m1, double *m2, int N);

void mult(double *m1, double *m2, double *m3, int N);
void divsc(double *matr, double *tr, int N, double r);
void make_r(double *a, double *b, double *I, double *r, int N);
double maxj(double *matr, int N);
double maxi(double *matr, int N);

void blas_mult(double* a, double*  b,double*  c, int N);
void blas_divsc(double *matr, double *tr, int N, double r);
void blas_make_r(double *a, double *b, double *I, double *r, int N);
double blas_maxi(double *matr, int N);
double blas_maxj(double *matr, int N);

void hand_mult(double *m1, double *m2, double *m3, int N);
void hand_divsc(double *matr, double *tr, int N, double r);
void hand_make_r(double *a, double *b, double *I, double *r, int N);
void hand_sum(double *m1, double *m2, int N);

double scal(double* a, double* b, int N);

int main(int argc, char* argv[]){
	struct timespec start, end;
	int N;
	int M;
	double *a;
	// double a[]={2,5,7,6,3,4,5,-2,-3,8,4,3,-5,-5,1,0};

	if(argc < 3){
		printf("Enter matrix size N = ");
		scanf("%d",&N);
		printf("Enter row length M = ");
		scanf("%d",&M);
	}
	else{
		N = atoi(argv[1]);
		M = atoi(argv[2]);
	}

	a=(double*)malloc(sizeof(double)*N*N);
	matr_mk(N, a);

	clock_gettime(CLOCK_MONOTONIC_RAW, &start);
	inverse(a, N, M);
	clock_gettime(CLOCK_MONOTONIC_RAW, &end);
  printf("PURE Time taken: %.10lf sec.\n",end.tv_sec-start.tv_sec+ 0.000000001*(end.tv_nsec-start.tv_nsec));

	free(a);
	// a=(double*)malloc(sizeof(double)*N*N);
	// matr_mk(N, a);
	//
	// clock_gettime(CLOCK_MONOTONIC_RAW, &start);
	// blas_inverse(a, N, M);
	// clock_gettime(CLOCK_MONOTONIC_RAW, &end);
  // printf("BLAS Time taken: %.10lf sec.\n",end.tv_sec-start.tv_sec+ 0.000000001*(end.tv_nsec-start.tv_nsec));
	//
	// free(a);
	// a=(double*)_mm_malloc(sizeof(double)*N*N, 64);
	// matr_mk(N, a);
	//
	// clock_gettime(CLOCK_MONOTONIC_RAW, &start);
	// hand_inverse(a,N,M);
	// clock_gettime(CLOCK_MONOTONIC_RAW, &end);
	// printf("HAND Time taken: %.10lf sec.\n",end.tv_sec-start.tv_sec+ 0.000000001*(end.tv_nsec-start.tv_nsec));
	//
	// _mm_free(a);

	return 0;
}

void matr_mk(int N, double* a){
	int i,j;
	for (i = 0; i < N; i++) {
		a[i*N+i] = i+1;
	}
}

void print(int N, char * name, double* array){
	int i, j;
	printf("\n%s\n",name);
	for (i=0;i<N;i++){
		for (j=0;j<N;j++) {
			printf("%0.5f  ",array[N*i+j]);
		}
		printf("\n");
	}
}

void transpose(double* a, double *t, int N){
	for (int i = 0; i < N; i++){
		for(int j = 0; j < N; j++){
			t[i*N + j] = a[j*N + i];
		}
	}
}

double get_aij(double *matr, int row, int column, int N){
	return matr[row * N + column];
}

double maxj(double *matr, int N){
  double max = 0;
  for(int i = 0; i < N; i++) {
    	double tmp = 0;
      for(int j = 0; j < N; j++) {
          tmp += abs(get_aij(matr, j, i, N));
      }
      if(max < tmp) {
          max = tmp;
      }
  }
  return max;
}

double maxi(double *matr, int N) {
	double max = 0;
  for(int i = 0; i < N; i++) {
      double tmp = 0;
      for(int j = 0; j < N; j++) {
          tmp += abs(get_aij(matr, i, j, N));
      }
      if(max < tmp) {
          max = tmp;
      }
  }
  return max;
}

void divsc(double *matr, double *tr, int N, double r){
	for(int i = 0; i < N; i++){
		for(int j = 0 ; j < N; j++){
			matr[i*N+j] = tr[i*N+j] / r;
		}
	}
}

void mult(double *m1, double *m2, double *m3, int N){
	double *y=(double*)malloc(sizeof(double)*N*N);
	transpose(m2, y, N);

	for (int row = 0; row < N; row++) {
    for (int col = 0; col < N; col++) {
			m3[row*N+col] = 0;
      for (int inner = 0; inner < N; inner++) {
          m3[row*N+col] += m1[row*N+inner] * y[col*N+inner];
      }
    }
  }

	free(y);
}

void make_r(double *a, double *b, double *I, double *r, int N){
	double *y=(double*)malloc(sizeof(double)*N*N);
	mult(b, a, y, N);
	for(int i=0; i<N; i++){
		for(int j=0; j<N; j++){
			r[i*N+j] = I[i*N+j] - y[i*N+j];
		}
	}
	free(y);
}

void sum(double *m1, double *m2, int N){
	for(int i=0; i<N; i++){
		for(int j=0; j<N; j++){
			m1[i*N+j] +=m2[i*N+j];
		}
	}
}

void inverse(double *a, int N, int M){
	double io;
	double* I; // единичная матрица
	double* t; // транспонированная матрица а
	double* b; // t/(maxj*maxi)
	double* r;
	double* c; // обратная матрица
	I=(double*)malloc(sizeof(double)*N*N);
	t=(double*)malloc(sizeof(double)*N*N);
	b=(double*)malloc(sizeof(double)*N*N);
	r=(double*)malloc(sizeof(double)*N*N);
	c=(double*)malloc(sizeof(double)*N*N);

	for(int j = 0; j < N; j++){
		I[j*N + j] = 1;
	} // получаем I

	transpose(a, t, N); // получаем t

	io = maxj(a, N)*maxi(a,N);
	divsc(b, t, N, io); // получаем b
	make_r(a,b,I,r,N);
	make_r(a,b,I,t,N);

	for(int i = 1; i <= M; i++){
		sum(I, t, N);
		mult(t, r, a, N);
		memcpy(t, a, sizeof(double)*N*N);
	}

	mult(I, b, c, N);

	if (N < 17) {
		print(N, "c", c);
	}

	free(b);
	free(c);
	free(I);
	free(t);
	free(r);
}

void blas_mult(double* a, double* b, double* c, int N){
	double alpha = 1.0, beta = 0.;
	int incx = 1;
	int incy = N;
	cblas_dgemm(CblasRowMajor, CblasNoTrans, CblasNoTrans, N, N, N, alpha, b, N, a, N, beta, c, N);
}

void blas_make_r(double *a, double *b, double *I, double *r, int N){
	double *y = (double*)malloc(sizeof(double)*N*N);
	blas_mult(b, a, y, N);
	for(int i=0; i<N; i++){
		for(int j=0; j<N; j++){
			r[i*N+j] = I[i*N+j] - y[i*N+j];
		}
	}
	free(y);
}

double blas_maxj(double *matr, int N){
  double max = 0;
  for(int i = 0; i < N; i++) {
    	double tmp = cblas_dasum(N, matr + i, N);
      if(max < tmp) {
          max = tmp;
      }
  }
  return max;
}

double blas_maxi(double *matr, int N){
  double max = 0;
  for(int i = 0; i < N; i++) {
    	double tmp = cblas_dasum(N, matr + i*N, 1);
      if(max < tmp) {
          max = tmp;
      }
  }
  return max;
}

void blas_divsc(double *matr, double *tr, int N, double r){
	cblas_dscal(N*N, 1/r, tr, 1);
	cblas_dcopy(N*N, tr, 1, matr, 1);
}

void blas_inverse(double *a, int N, int M){
	double io;
	double* I; // единичная матрица
	double* t; // транспонированная матрица а
	double* b; // t/(maxj*maxi)
	double* r;
	double* c; // обратная матрица
	I=(double*)malloc(sizeof(double)*N*N);
	t=(double*)malloc(sizeof(double)*N*N);
	b=(double*)malloc(sizeof(double)*N*N);
	r=(double*)malloc(sizeof(double)*N*N);
	c=(double*)malloc(sizeof(double)*N*N);

	for(int j = 0; j < N; j++){
		I[j*N + j] = 1;
	} // получаем I

	transpose(a, t, N); // получаем t
	io = blas_maxj(a, N)*blas_maxi(a,N);
	blas_divsc(b, t, N, io); // получаем b

	blas_make_r(a,b,I,r,N);
	blas_make_r(a,b,I,t,N);

	for(int i = 1; i <= M; i++){
		sum(I, t, N);
		blas_mult(t, r, a, N);
		memcpy(t, a, sizeof(double)*N*N);
	}

	blas_mult(I, b, c, N);

	if (N < 17) {
		print(N, "c", c);
	}

	// blas_mult()
	free(b);
	free(c);
	free(I);
	free(t);
	free(r);
}

// double scal(double* x, double* y, int n){
// 	__m128d *xx, *yy;
// 	__m128d p, s, a, b;
// 	xx = (__m128d*)x;
// 	yy = (__m128d*)y;
// 	s = _mm_setzero_pd();
// 	for(int i=0; i<n; i+=2){
// 		a = _mm_load_pd(&x[i]);
// 		b = _mm_load_pd(&y[i]);
// 		p = _mm_mul_pd(a,b);
// 		s = _mm_add_pd(s,p);
// 	}
// 	// p = _mm_movehl_ps(p,s);
// 	// s = _mm_add_pd(s,p);
// 	p = _mm_shuffle_pd(s,s,1);
// 	s = _mm_add_sd(s,p);
// 	double sum;
// 	_mm_store_sd(&sum,s);
// 	return sum;
// }
void hand_sum(double *y, double *r, int N){
	__m128d *m1 = (__m128d*)r;
	__m128d *m2 = (__m128d*)y;
  // __m128d *m3 = (__m128d*)r;

  // for(int i = 0; i < N*N; i+=2) {
      for(int j = 0; j < N*N/2; j++) {
          m2[j] = _mm_add_pd(m1[j], m2[j]);
      }
  // }
}

void hand_mult(double* a, double* b, double* r, int N){
	// __m128d *xx, *yy;
	// xx = (__m128d*)a;
	// yy = (__m128d*)b;
	__m128d p, s, x, u;
	double *y=(double*)_mm_malloc(sizeof(double)*N*N, 64);
	transpose(b, y, N);
	// printf("\n%f\n", scal(&a[2], &y[2], N));
	for(int i = 0; i < N; i++){
		for(int j = 0; j < N; j++){
			s = _mm_setzero_pd();
			for(int k=0; k<N; k+=2){
				x = _mm_load_pd(a + i*N + k);
				u = _mm_load_pd(b + j*N + k);
				p = _mm_mul_pd(x,u);
				s = _mm_add_pd(s,p);
			}
			// p = _mm_movehl_ps(p,s);
			// s = _mm_add_pd(s,p);
			p = _mm_shuffle_pd(s,s,1);
			s = _mm_add_sd(s,p);
			double sum;
			_mm_store_sd(&sum,s);
			r[i*N + j] = sum;
		}
	}
	_mm_free(y);
}

void hand_divsc(double *matr, double *tr, int N, double r){
	r = 1/r;
	// printf("%f\n", r);

	__m128d *xx = (__m128d*)tr;
	__m128d *yy = (__m128d*)matr;
	__m128d tmp = {r,r};
	// for(int i = 0; i < N*N; i+=2){
		for(int j = 0; j < N*N / 2; j++){
			yy[j] = _mm_mul_pd(xx[j], tmp);
		}
	// }
}
void hand_make_r(double *a, double *b, double *I, double *r, int N){
	double *y=(double*)_mm_malloc(sizeof(double)*N*N, 64);
	hand_mult(b, a, y, N);
	__m128d *m1 = (__m128d*)I;
	__m128d *m2 = (__m128d*)y;
  __m128d *m3 = (__m128d*)r;

  // for(int i = 0; i < N*N; i+=2) {
      for(int j = 0; j < N*N/2; j++) {
          m3[j] = _mm_sub_pd(m1[j], m2[j]);
      }
  // }
	_mm_free(y);
};

void hand_inverse(double *a, int N, int M){
	double io;
	double* I; // единичная матрица
	double* t; // транспонированная матрица а
	double* b; // t/(maxj*maxi)
	double* r;
	double* c; // обратная матрица
	I=(double*)_mm_malloc(sizeof(double)*N*N, 64);
	t=(double*)_mm_malloc(sizeof(double)*N*N, 64);
	b=(double*)_mm_malloc(sizeof(double)*N*N, 64);
	r=(double*)_mm_malloc(sizeof(double)*N*N, 64);
	c=(double*)_mm_malloc(sizeof(double)*N*N, 64);

	for(int j = 0; j < N; j++){
		I[j*N + j] = 1;
	} // получаем I
	// print(N, "a", a);
	// print(N, "I", I);
	// print(N, "b", b);


	transpose(a, t, N); // получаем t
	// print(N, "t", t);

	io = maxj(a, N)*maxi(a,N);
	// printf("\nio = %f\n", io);
	hand_divsc(b, t, N, io); // получаем b
	// print(N, "b", b);

	// print(N, "t", t);

	hand_make_r(a,b,I,r,N);
	// print(N, "r", r);
	hand_make_r(a,b,I,t,N);
	// for(int i = 0; i < N; i++){
	// 	for(int j = 0; j < N; j++){
	// 		t[i*N+j]=r[i*N+j];
	// 	}
	// }
	for(int i = 1; i <= M; i++){
		hand_sum(I, t, N);
		hand_mult(t, r, a, N);
		memcpy(t, a, sizeof(double)*N*N);
	}

	hand_mult(I, b, c, N);
	// print(N, "I", I);
	// print(N, "b", b);

	if (N < 17) {
		print(N, "c", c);
	}
	memset(a,0,N*N*sizeof(double));
	matr_mk(N,a);
	// hand_mult(a,c,r,N);
	// print(N, "a", a);
	// print(N, "r", r);
	_mm_free(b);
	_mm_free(c);
	_mm_free(I);
	_mm_free(t);
	_mm_free(r);
}

//calculate determinant
// double det(double *matr, int N){
//   int temp = 0;
//   int k = 1;
//   if(N < 1){
//       printf("Неверный размер матрицы!!!");
//       return 0;
//   }
//   else if (N == 1) temp = matr[0];
//   else if (N == 2) temp = matr[0] * matr[1*N + 1] - matr[1*N + 0] * matr[1];
//   else{
//       for(int i = 0; i < N; i++){
//           int m = N - 1;
//           double *temp_matr = (double*)malloc(sizeof(double)*m*m);
// 					if(!temp_matr) exit(1);
//           // for(int j = 0; j < m; j++)
//               // temp_matr[j] = new int [m];
//           // get_matr(matr, N, temp_matr, 0, 0);
// 					for(int i = 1; i < N; i++){
// 						for(int j = 1; j < N; j++){
//
// 							temp_matr[(i-1)*m + (j - 1)] = matr[i*N+j];
// 						}
// 					}
// 					// k = gaussMethod(N, N, )
// 					print(N,"matr", matr);
// 					print(m,"temp", temp_matr);
// 					printf("%f\n", temp_matr[2]);
// 					getchar();
//           temp = temp + k * matr[0*N+i] * det(temp_matr, m);
//           k = -k;
//           // FreeMem(temp_matr, m);
// 					free(temp_matr);
//       }
//   }
//   return temp;
// }

//strike out
// void get_matr(double *matr, int N, double *temp_matr, int indRow, int indCol){
// 	int ki = 0;
// 	for (int i = 0; i < N; i++){
// 		if(i != indRow){
// 			for (int j = 0, kj = 0; j < N; j++){
// 				if (j != indCol){
// 					temp_matr[ki*N + kj] = matr[i*N + j];
// 					kj++;
// 				}
// 			}
// 			ki++;
// 		}
// 	}
// }

// double *y=(double*)malloc(sizeof(double)*N*N);
// transpose(a, y, N);
// __m128d * xx = (__m128d*)a;
// __m128d * yy = (__m128d*)y;
// __m128d p, s;
// for(unsigned i = 0; i < N; ++i) {
//   for(unsigned j = 0; j < N; ++j) {
// 		s = _mm_setzero_pd();
// 		for(int k = 0; k < N/4; k++){
// 			p = _mm_mul_pd(xx[i], yy[i]);
// 			s = _mm_add_pd(s,p);
// 		}
//
// 		p=_mm_movehl_pd(p,s);
// 		s=_mm_add_pd(s,p);
// 		p=_mm_shuffle_pd(s,s,1);
// 		s=_mm_add_sd(s,p);
// 		double sum;
// 		_mm_store_sd(&sum, s);
//
// 		r[i*N+j] = sum;
// 	}
// }
