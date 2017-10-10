#include "header.h"
#include <malloc.h>
int main(){
  unsigned long long t;
  union ticks {
    unsigned long long t64;
    struct s32 {
      long th, tl;
    } t32;
  } start, end;
  double cpu_Hz = 3300000000ULL; //3.3 GHz

  dim2* two = (dim2*)malloc(sizeof(dim2) * N);
  // dim3* thr = (dim3*)malloc(sizeof(dim3 ) * N);
  // dim4* fou = (dim4*)malloc(sizeof(dim4) * N);
  // dim5* fiv = (dim5*)malloc(sizeof(dim5) * N);
  // dim6* six = (dim6*)malloc(sizeof(dim6) * N);
  // dim7* sev = (dim7*)malloc(sizeof(dim7 * N);
  // dim8* eig = (dim8*)malloc(sizeof(dim8 * N);
  // dim9* nin = (dim9*)malloc(sizeof(dim9 * N);
  // dim10* ten = (dim10*)malloc(sizeof(dim10 * N);
  // dim11* elf = (dim11*)malloc(sizeof(dim11 * N);
  // dim12* twe = (dim12*)malloc(sizeof(dim12 * N);

  double* x = (double*)malloc(sizeof(double) * N);
  double* y = (double*)malloc(sizeof(double) * N);
  // double* z = (double*)malloc(sizeof(double) * N);
  // double* u = (double*)malloc(sizeof(double) * N);
  // double* v = (double*)malloc(sizeof(double) * N);
  // double* w = (double*)malloc(sizeof(double) * N);
  // double* h = (double*)malloc(sizeof(double) * N);
  // double* j = (double*)malloc(sizeof(double) * N);
  // double* k = (double*)malloc(sizeof(double) * N);
  // double* m = (double*)malloc(sizeof(double) * N);
  // double* n = (double*)malloc(sizeof(double) * N);
  // double* o = (double*)malloc(sizeof(double) * N);

  asm("rdtsc\n":"=a"(start.t32.th), "=d"(start.t32.tl));
  for(int i = 0; i < 13107200; i++){
    two[i].x = x[i];
    two[i].y = y[i];
  }
  asm("rdtsc\n":"=a"(end.t32.th), "=d"(end.t32.tl));

  t = (end.t64 - start.t64);

  printf("Ticks: %llu\n", t / N / 2);
  printf("Mb/s: %f\n", 200 / (t / cpu_Hz));
  //
  // asm("rdtsc\n":"=a"(start.t32.th), "=d"(start.t32.tl));
  // for(int i = 0; i < 13107200; i++){
  //   thr[i].x = x[i];
  //   thr[i].y = y[i];
  //   thr[i].z = z[i];
  // }
  // asm("rdtsc\n":"=a"(end.t32.th), "=d"(end.t32.tl));
  //
  // t = (end.t64 - start.t64);
  //
  // printf("Ticks: %llu\n", t / N / 3);
  // printf("Mb/s: %f\n", 300 / (t / cpu_Hz));
  //
  // asm("rdtsc\n":"=a"(start.t32.th), "=d"(start.t32.tl));
  // for(int i = 0; i < 13107200; i++){
  //   fou[i].x = x[i];
  //   fou[i].y = y[i];
  //   fou[i].z = z[i];
  //   fou[i].u = u[i];
  // }
  // asm("rdtsc\n":"=a"(end.t32.th), "=d"(end.t32.tl));
  //
  // t = (end.t64 - start.t64);
  //
  // printf("Ticks: %llu\n", t / N / 4);
  // printf("Mb/s: %f\n", 400 / (t / cpu_Hz));
  //
  // asm("rdtsc\n":"=a"(start.t32.th), "=d"(start.t32.tl));
  // for(int i = 0; i < 13107200; i++){
  //   fiv[i].x = x[i];
  //   fiv[i].y = y[i];
  //   fiv[i].z = z[i];
  //   fiv[i].u = u[i];
  //   fiv[i].v = v[i];
  // }
  // asm("rdtsc\n":"=a"(end.t32.th), "=d"(end.t32.tl));
  //
  // t = (end.t64 - start.t64);
  //
  // printf("Ticks: %llu\n", t / N / 5);
  // printf("Mb/s: %f\n", 500 / (t / cpu_Hz));
  //
  // asm("rdtsc\n":"=a"(start.t32.th), "=d"(start.t32.tl));
  // for(int i = 0; i < 13107200; i++){
  //   six[i].x = x[i];
  //   six[i].y = y[i];
  //   six[i].z = z[i];
  //   six[i].u = u[i];
  //   six[i].v = v[i];
  //   six[i].w = w[i];
  // }
  // asm("rdtsc\n":"=a"(end.t32.th), "=d"(end.t32.tl));
  //
  // t = (end.t64 - start.t64);
  //
  // printf("Ticks: %llu\n", t / N / 6);
  // printf("Mb/s: %f\n", 600 / (t / cpu_Hz));

  asm("rdtsc\n":"=a"(start.t32.th), "=d"(start.t32.tl));
  for(int i = 0; i < 13107200; i++){
    six[i].x = x[i];
    six[i].y = y[i];
    six[i].z = z[i];
    six[i].u = u[i];
    six[i].v = v[i];
    six[i].w = w[i];
    six[i].h = h[i];
  }
  asm("rdtsc\n":"=a"(end.t32.th), "=d"(end.t32.tl));

  t = (end.t64 - start.t64);

  printf("Ticks: %llu\n", t / N / 7);
  printf("Mb/s: %f\n", 700 / (t / cpu_Hz));

  free(two);
  // free(thr);
  // free(fou);
  // free(fiv);
  // free(six);
  // free(sev);
  // free(eig);
  // free(nin);
  // free(ten);
  // free(elf);
  // free(twe);
  free(x);
  free(y);
  // free(z);
  // free(u);
  // free(v);
  // free(w);
  // free(i);
  // free(j);
  // free(k);
  // free(m);
  // free(n);
  // free(o);

  return 0;
  /*Сделать до 12 | скачек*/
}
