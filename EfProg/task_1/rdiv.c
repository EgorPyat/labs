#include <stdio.h>

int main(){
  union ticks {
    unsigned long long t64;
    struct s32 {
      long th, tl;
    } t32;
  } start, end;

  unsigned long long t;

  double a = 1.5;
  double b = 0.5;
  double c = 5.9;
  double d = 3.141592;
  double e = -23.32;
  double f = 1.000001;

  for(int i = 0; i < 2000000; i++){}

  asm("rdtsc\n":"=a"(start.t32.th), "=d"(start.t32.tl));
  for(int i = 0; i < 9000000; i++){
    a /= f;
    b /= a;
    c /= b;
    d /= c;
    e /= d;
    f /= e;
  }
  asm("rdtsc\n":"=a"(end.t32.th), "=d"(end.t32.tl));

  t = (end.t64 - start.t64);

  printf("Latency: %llu\n", t / 9000000 / 8);

  asm("rdtsc\n":"=a"(start.t32.th), "=d"(start.t32.tl));
  for(int i = 0; i < 9000000; i++){
    a = b / c;
    d = e / f;
  }
  asm("rdtsc\n":"=a"(end.t32.th), "=d"(end.t32.tl));

  t = (end.t64 - start.t64);

  printf("Throughput: %llu\n", t / 9000000 / 2);

  return 0;
}
