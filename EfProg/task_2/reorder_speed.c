#include "header.h"

int main(){
  unsigned long long t;
  union ticks {
    unsigned long long t64;
    struct s32 {
      long th, tl;
    } t32;
  } start, end;
  double cpu_Hz = 3300000000ULL; //3.3 GHz

  // dim1* one = (dim1*)malloc(sizeof(dim1) * N);
  // dim2* two = (dim2*)malloc(sizeof(dim2) * N);
  // dim3* thr = (dim3*)malloc(sizeof(dim3) * N);
  dim4* fou = (dim4*)malloc(sizeof(dim4) * N);
  // dim5* fiv = (dim5*)malloc(sizeof(dim5) * N);
  // dim6* six = (dim6*)malloc(sizeof(dim6) * N);
  // dim7* sev = (dim7*)malloc(sizeof(dim7) * N);
  // dim8* eig = (dim8*)malloc(sizeof(dim8) * N);
  // dim9* nin = (dim9*)malloc(sizeof(dim9) * N);
  // dim10* ten = (dim10*)malloc(sizeof(dim10) * N);
  // dim11* elf = (dim11*)malloc(sizeof(dim11) * N);
  // dim12* twe = (dim12*)malloc(sizeof(dim12) * N);
  // dim13* ten3 = (dim13*)malloc(sizeof(dim13) * N);
  // dim14* ten4 = (dim14*)malloc(sizeof(dim14) * N);
  // dim15* ten5 = (dim15*)malloc(sizeof(dim15) * N);
  // dim16* ten6 = (dim16*)malloc(sizeof(dim16) * N);

  double* x = (double*)malloc(sizeof(double) * N);
  double* y = (double*)malloc(sizeof(double) * N);
  double* z = (double*)malloc(sizeof(double) * N);
  double* u = (double*)malloc(sizeof(double) * N);
  // double* v = (double*)malloc(sizeof(double) * N);
  // double* w = (double*)malloc(sizeof(double) * N);
  // double* h = (double*)malloc(sizeof(double) * N);
  // double* j = (double*)malloc(sizeof(double) * N);
  // double* k = (double*)malloc(sizeof(double) * N);
  // double* m = (double*)malloc(sizeof(double) * N);
  // double* n = (double*)malloc(sizeof(double) * N);
  // double* o = (double*)malloc(sizeof(double) * N);
  // double* a = (double*)malloc(sizeof(double) * N);
  // double* s = (double*)malloc(sizeof(double) * N);
  // double* d = (double*)malloc(sizeof(double) * N);
  // double* f = (double*)malloc(sizeof(double) * N);

  // asm("rdtsc\n":"=a"(start.t32.th), "=d"(start.t32.tl));
  // for(int i = 0; i < 13107200; i++){
  //   one[i].x = x[i];
  // }
  // asm("rdtsc\n":"=a"(end.t32.th), "=d"(end.t32.tl));
  //
  // t = (end.t64 - start.t64);
  //
  // printf("Ticks: %llu\n", t / N / 1);
  // printf("Mb/s: %f\n", 100 / (t / cpu_Hz));

  // asm("rdtsc\n":"=a"(start.t32.th), "=d"(start.t32.tl));
  // for(int i = 0; i < 13107200; i++){
  //   two[i].x = x[i];
  //   two[i].y = y[i];
  // }
  // asm("rdtsc\n":"=a"(end.t32.th), "=d"(end.t32.tl));
  //
  // t = (end.t64 - start.t64);
  //
  // printf("Ticks: %llu\n", t / N / 2);
  // printf("Mb/s: %f\n", 200 / (t / cpu_Hz));

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

  asm("rdtsc\n":"=a"(start.t32.th), "=d"(start.t32.tl));
  for(int i = 0; i < 13107200; i++){
    fou[i].x = x[i];
    fou[i].y = y[i];
    fou[i].z = z[i];
    fou[i].u = u[i];
  }
  asm("rdtsc\n":"=a"(end.t32.th), "=d"(end.t32.tl));

  t = (end.t64 - start.t64);

  printf("Ticks: %llu\n", t / N / 4);
  printf("Mb/s: %f\n", 400 / (t / cpu_Hz));

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

  // asm("rdtsc\n":"=a"(start.t32.th), "=d"(start.t32.tl));
  // for(int i = 0; i < 13107200; i++){
  //   sev[i].x = x[i];
  //   sev[i].y = y[i];
  //   sev[i].z = z[i];
  //   sev[i].u = u[i];
  //   sev[i].v = v[i];
  //   sev[i].w = w[i];
  //   sev[i].h = h[i];
  // }
  // asm("rdtsc\n":"=a"(end.t32.th), "=d"(end.t32.tl));
  //
  // t = (end.t64 - start.t64);
  //
  // printf("Ticks: %llu\n", t / N / 7);
  // printf("Mb/s: %f\n", 700 / (t / cpu_Hz));

  // asm("rdtsc\n":"=a"(start.t32.th), "=d"(start.t32.tl));
  // for(int i = 0; i < 13107200; i++){
  //   eig[i].x = x[i];
  //   eig[i].y = y[i];
  //   eig[i].z = z[i];
  //   eig[i].u = u[i];
  //   eig[i].v = v[i];
  //   eig[i].w = w[i];
  //   eig[i].h = h[i];
  //   eig[i].j = j[i];
  // }
  // asm("rdtsc\n":"=a"(end.t32.th), "=d"(end.t32.tl));
  //
  // t = (end.t64 - start.t64);
  //
  // printf("Ticks: %llu\n", t / N / 8);
  // printf("Mb/s: %f\n", 800 / (t / cpu_Hz));

  // asm("rdtsc\n":"=a"(start.t32.th), "=d"(start.t32.tl));
  // for(int i = 0; i < 13107200; i++){
  //   nin[i].x = x[i];
  //   nin[i].y = y[i];
  //   nin[i].z = z[i];
  //   nin[i].u = u[i];
  //   nin[i].v = v[i];
  //   nin[i].w = w[i];
  //   nin[i].h = h[i];
  //   nin[i].j = j[i];
  //   nin[i].k = k[i];
  // }
  // asm("rdtsc\n":"=a"(end.t32.th), "=d"(end.t32.tl));
  //
  // t = (end.t64 - start.t64);
  //
  // printf("Ticks: %llu\n", t / N / 9);
  // printf("Mb/s: %f\n", 900 / (t / cpu_Hz));

  // asm("rdtsc\n":"=a"(start.t32.th), "=d"(start.t32.tl));
  // for(int i = 0; i < 13107200; i++){
  //   ten[i].x = x[i];
  //   ten[i].y = y[i];
  //   ten[i].z = z[i];
  //   ten[i].u = u[i];
  //   ten[i].v = v[i];
  //   ten[i].w = w[i];
  //   ten[i].h = h[i];
  //   ten[i].j = j[i];
  //   ten[i].k = k[i];
  //   ten[i].m = m[i];
  // }
  // asm("rdtsc\n":"=a"(end.t32.th), "=d"(end.t32.tl));
  //
  // t = (end.t64 - start.t64);
  //
  // printf("Ticks: %llu\n", t / N / 10);
  // printf("Mb/s: %f\n", 1000 / (t / cpu_Hz));

  // asm("rdtsc\n":"=a"(start.t32.th), "=d"(start.t32.tl));
  // for(int i = 0; i < 13107200; i++){
  //   elf[i].x = x[i];
  //   elf[i].y = y[i];
  //   elf[i].z = z[i];
  //   elf[i].u = u[i];
  //   elf[i].v = v[i];
  //   elf[i].w = w[i];
  //   elf[i].h = h[i];
  //   elf[i].j = j[i];
  //   elf[i].k = k[i];
  //   elf[i].m = m[i];
  //   elf[i].n = n[i];
  // }
  // asm("rdtsc\n":"=a"(end.t32.th), "=d"(end.t32.tl));
  //
  // t = (end.t64 - start.t64);
  //
  // printf("Ticks: %llu\n", t / N / 11);
  // printf("Mb/s: %f\n", 1100 / (t / cpu_Hz));

  // asm("rdtsc\n":"=a"(start.t32.th), "=d"(start.t32.tl));
  // for(int i = 0; i < 13107200; i++){
  //   twe[i].x = x[i];
  //   twe[i].y = y[i];
  //   twe[i].z = z[i];
  //   twe[i].u = u[i];
  //   twe[i].v = v[i];
  //   twe[i].w = w[i];
  //   twe[i].h = h[i];
  //   twe[i].j = j[i];
  //   twe[i].k = k[i];
  //   twe[i].m = m[i];
  //   twe[i].n = n[i];
  //   twe[i].o = o[i];
  // }
  // asm("rdtsc\n":"=a"(end.t32.th), "=d"(end.t32.tl));
  //
  // t = (end.t64 - start.t64);
  //
  // printf("Ticks: %llu\n", t / N / 12);
  // printf("Mb/s: %f\n", 1200 / (t / cpu_Hz));

  // asm("rdtsc\n":"=a"(start.t32.th), "=d"(start.t32.tl));
  // for(int i = 0; i < 13107200; i++){
  //   ten3[i].x = x[i];
  //   ten3[i].y = y[i];
  //   ten3[i].z = z[i];
  //   ten3[i].u = u[i];
  //   ten3[i].v = v[i];
  //   ten3[i].w = w[i];
  //   ten3[i].h = h[i];
  //   ten3[i].j = j[i];
  //   ten3[i].k = k[i];
  //   ten3[i].m = m[i];
  //   ten3[i].n = n[i];
  //   ten3[i].o = o[i];
  //   ten3[i].a = a[i];
  // }
  // asm("rdtsc\n":"=a"(end.t32.th), "=d"(end.t32.tl));
  //
  // t = (end.t64 - start.t64);
  //
  // printf("Ticks: %llu\n", t / N / 13);
  // printf("Mb/s: %f\n", 1300 / (t / cpu_Hz));

  // asm("rdtsc\n":"=a"(start.t32.th), "=d"(start.t32.tl));
  // for(int i = 0; i < 13107200; i++){
  //   ten4[i].x = x[i];
  //   ten4[i].y = y[i];
  //   ten4[i].z = z[i];
  //   ten4[i].u = u[i];
  //   ten4[i].v = v[i];
  //   ten4[i].w = w[i];
  //   ten4[i].h = h[i];
  //   ten4[i].j = j[i];
  //   ten4[i].k = k[i];
  //   ten4[i].m = m[i];
  //   ten4[i].n = n[i];
  //   ten4[i].o = o[i];
  //   ten4[i].a = a[i];
  //   ten4[i].s = s[i];
  // }
  // asm("rdtsc\n":"=a"(end.t32.th), "=d"(end.t32.tl));
  //
  // t = (end.t64 - start.t64);
  //
  // printf("Ticks: %llu\n", t / N / 14);
  // printf("Mb/s: %f\n", 1400 / (t / cpu_Hz));

  // asm("rdtsc\n":"=a"(start.t32.th), "=d"(start.t32.tl));
  // for(int i = 0; i < 13107200; i++){
  //   ten5[i].x = x[i];
  //   ten5[i].y = y[i];
  //   ten5[i].z = z[i];
  //   ten5[i].u = u[i];
  //   ten5[i].v = v[i];
  //   ten5[i].w = w[i];
  //   ten5[i].h = h[i];
  //   ten5[i].j = j[i];
  //   ten5[i].k = k[i];
  //   ten5[i].m = m[i];
  //   ten5[i].n = n[i];
  //   ten5[i].o = o[i];
  //   ten5[i].a = a[i];
  //   ten5[i].s = s[i];
  //   ten5[i].d = d[i];
  // }
  // asm("rdtsc\n":"=a"(end.t32.th), "=d"(end.t32.tl));
  //
  // t = (end.t64 - start.t64);
  //
  // printf("Ticks: %llu\n", t / N / 15);
  // printf("Mb/s: %f\n", 1500 / (t / cpu_Hz));

  // asm("rdtsc\n":"=a"(start.t32.th), "=d"(start.t32.tl));
  // for(int i = 0; i < 13107200; i++){
  //   ten6[i].x = x[i];
  //   ten6[i].y = y[i];
  //   ten6[i].z = z[i];
  //   ten6[i].u = u[i];
  //   ten6[i].v = v[i];
  //   ten6[i].w = w[i];
  //   ten6[i].h = h[i];
  //   ten6[i].j = j[i];
  //   ten6[i].k = k[i];
  //   ten6[i].m = m[i];
  //   ten6[i].n = n[i];
  //   ten6[i].o = o[i];
  //   ten6[i].a = a[i];
  //   ten6[i].s = s[i];
  //   ten6[i].d = d[i];
  //   ten6[i].f = f[i];
  // }
  // asm("rdtsc\n":"=a"(end.t32.th), "=d"(end.t32.tl));
  //
  // t = (end.t64 - start.t64);
  //
  // printf("Ticks: %llu\n", t / N / 16);
  // printf("Mb/s: %f\n", 1600 / (t / cpu_Hz));

  // free(one);
  // free(two);
  // free(thr);
  free(fou);
  // free(fiv);
  // free(six);
  // free(sev);
  // free(eig);
  // free(nin);
  // free(ten);
  // free(elf);
  // free(twe);
  // free(ten3);
  // free(ten4);
  // free(ten5);
  // free(ten6);

  free(x);
  free(y);
  free(z);
  free(u);
  // free(v);
  // free(w);
  // free(h);
  // free(j);
  // free(k);
  // free(m);
  // free(n);
  // free(o);
  // free(a);
  // free(s);
  // free(d);
  // free(f);

  return 0;
}
