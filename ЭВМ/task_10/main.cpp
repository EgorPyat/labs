#include <stdio.h>
#include <stdlib.h>
int main() {
  union ticks {
      unsigned long long t64;
      struct s32 {
          long th, tl;
      } t32;
  } start, end;
  int *array;
  unsigned long long t = -1;
  unsigned long long cache = 6144 * 1024;
  for(int fragment = 1; fragment <= 32; fragment++) {

    int frag_sz = cache / sizeof(int) / fragment;
    int arr = cache / sizeof(int) * fragment;

    array = (int*)malloc(arr*sizeof(int));

    for(int i = 0; i < fragment; i++) {
      for(int j = 0; j < frag_sz; j++) {
        array[i * cache / sizeof(int) + j] = (i + 1) * cache / sizeof(int) + j;
      }
    }

    for(int i = 0; i < frag_sz; i++) {
      array[(fragment - 1) * cache / sizeof(int) + i] = i + 1;
    }

    for(int j = 0; j < 10; j++) {
      asm("rdtsc\n":"=a"(start.t32.th), "=d"(start.t32.tl));
      int index = 0;
      for (int i = 0; i < arr; i++) {
        index = array[index];
    }
      asm("rdtsc\n":"=a"(end.t32.th), "=d"(end.t32.tl));
    if(t > (end.t64 - start.t64)){
      t = (end.t64 - start.t64);
    }
  }
  t /= arr;
  printf("%llu\n", t);
  t = -1;
  free(array);
  }

  return 0;
}
