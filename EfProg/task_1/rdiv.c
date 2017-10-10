#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include <stdio.h>

static inline uint64_t read_time(void)
{
    uint32_t a, d;
    __asm__ volatile("rdtscp\n\t":"=a"(a),"=d"(d));
    return ((uint64_t)d<<32)+a;
}

int main(){
  union ticks {
    unsigned long long t64;
    struct s32 {
      long th, tl;
    } t32;
  } start, end;

  uint64_t start1, stop1, tick = 0;

  unsigned long long t;

  double a = 1.5;
  double b = 0.5;
  double c = 5.9;
  double d = 3.141592;
  double e = -23.32;
  double f = 1.000001;

  for(int i = 0; i < 2000000; i++){}

  start1 = read_time();
  for(int i = 0; i < 9000000; i++){
    a /= f;
    b /= a;
    c /= b;
    d /= c;
    e /= d;
    f /= e;
  }
  stop1 = read_time();

  printf("Latency: %f\n", (double)(stop1 - start1) / 9000000 / 6);

  start1 = read_time();
  for(int i = 0; i < 9000000; i++){
    a = b / c;
    d = e / f;
  }
  stop1 = read_time();

  printf("Throughput: %f\n", (double)(stop1 - start1) / 9000000 / 2);

  return 0;
}
