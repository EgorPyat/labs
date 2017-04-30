#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <sys/time.h>

/* Количество ячеек вдоль координат x, y */
#define in 20
#define jn 20

#define a 1

double Fresh(double, double);
double Ro(double, double);
void Inic();

/* Выделение памяти для 3D пространства */
double F[in+1][jn+1];
double hx, hy;

/* Функция определения точного решения */
double Fresh(double x, double y){
  double res;
  res = x + y;
  return res;
}

/* Функция задания правой части уравнения */
double Ro(double x, double y){
  double d;
  d = -a*(x+y);
  return d;
}

/* Подпрограмма инициализации границ 3D пространства */
void Inic(){
  int i, j, k;
  for(i = 0; i <= in; i++){
    for(j = 0; j <= jn; j++){
      if((i!=0)&&(j!=0)&&(i!=in)&&(j!=jn)){
        F[i][j] = 0;
      }
      else{
        F[i][j] = Fresh(i*hx,j*hy);
      }
    }
  }
}

int main(){
  double X, Y;
  double max, N, t1, t2;
  double owx, owy, c, e;
  double Fi, Fj, F1;

  int i, j,mi, mj;
  int R, fl, fl1, fl2;
  int it,f;
  long int osdt;

  struct timeval tv1,tv2;

  it = 0;
  X = 2.0;
  Y = 2.0;
  e = 0.00001;

  /* Размеры шагов */
  hx = X/in;
  hy = Y/jn;

  owx = hx * hx;
  owy = hy * hy;

  c = 2/owx + 2/owy + a;

  gettimeofday(&tv1,(struct timezone*)0);

  /* Инициализация границ пространства */
  Inic();

  /* Основной итерационный цикл */
  do{
    f = 1;
    for(i = 1; i < in; i++){
      for(j = 1; j < jn; j++){
          F1 = F[i][j];
          Fi = (F[i+1][j] + F[i-1][j]) / owx;
          Fj = (F[i][j+1] + F[i][j-1]) / owy;
          F[i][j] = (Fi + Fj - Ro(i*hx, j*hy)) / c;
          if(fabs(F[i][j] - F1) > e) f = 0;
      }
    }
    it++;
  }
  while(f == 0);

  gettimeofday(&tv2,(struct timezone*)0);
  osdt = (tv2.tv_sec - tv1.tv_sec)*1000000 + tv2.tv_usec-tv1.tv_usec;

  printf("\nin = %d | iter = %d | E = %f | T = %ld\n", in, it, e, osdt);

  /* Нахождение максимального расхождения полученного приближенного решения
   * и точного решения */
  max = 0.0;

  for(i = 1; i < in; i++){
    for(j = 1; j < jn; j++){
      if((F1 = fabs(F[i][j] - Fresh(i*hx, j*hy))) > max){
        max = F1;
        mi = i; mj = j;
      }
	  }
  }

  printf("Max differ = %f in point: (%d, %d)\n", max, mi, mj);

  return 0;
}
