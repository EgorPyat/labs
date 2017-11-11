#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <pthread.h>
#include <unistd.h>
#include <stdint.h>
#include <string.h>
// L1 / 2 = 256 * 128 = 32Kb
// L2 / 2 = 512 * 256 = 128Kb
// L3 / 2= 2048 * 1538 = 3072Kb
// L3 * 10 = 8192 * 7690 = 61520Kb
static inline uint64_t read_time(void)
{
    uint32_t a, d;
    __asm__ volatile("rdtscp\n\t":"=a"(a),"=d"(d));
    return ((uint64_t)d<<32)+a;
}
double cpu_Hz = 3100000000ULL;
pthread_mutex_t m=PTHREAD_MUTEX_INITIALIZER;
double gmin = 100000000.0;
double gavg = 0.0;
void set_min_avg(double* mini, double* avgi){
  pthread_mutex_lock(&m);
  if(*mini < gmin) gmin = *mini;
  gavg += *avgi;
  pthread_mutex_unlock(&m);
}
const int ITERS = 3;
const int N = 256;
const int M = 128;
int* matrix = NULL;
int* new = NULL;
int thread_num = 128;
pthread_t threads[128][1];
volatile int ids[129] = {0};
void my_barrier(int id, int iter){
  int left  = (id == 0 ? (thread_num) : (id - 1));
  int right = (id == (thread_num) ? 0 : (id + 1));
  ++ids[id];
  while(ids[left] < iter || ids[right] < iter){}
}
void init_matrix(int* matrix){
  for(int i = 0; i < N + 2; i++){
    for(int j = 0; j < M + 2; j++){
      if(i != 0 && i != N + 1 && j != 0 && j != M + 1){
        matrix[i * (M + 2) + j] = rand() % 255;
      }
    }
  }
}
void print_matrix(int* matrix){
  printf("\n\n");
  for(int i = 0; i < N + 2; i++){
    printf("\t");
    for(int j = 0; j < M + 2; j++){
      printf("%4d ", matrix[i * (M + 2) + j]);
    }
    printf("\n\n");
  }
  printf("\n");
}
int cmpfunc(const void * a, const void * b) {
   return(*(int*)a - *(int*)b );
}
int get_median(int* filter, int length){
  qsort(filter, length, sizeof(int), cmpfunc);
  return filter[length / 2];
}
int* update_filter(int* matrix, int* filter, int i, int j){
  int count = 0;
  int ti = i;
  int tj = j;
  --i;
  for(int k = 0; k < 3; k++){
    j = tj;
    --j;
    for(int l = 0; l < 3; l++){
      if(matrix[i * (M + 2) + j] == 0){
        filter[count++] = matrix[ti * (M + 2) + tj];
      }
      else{
        filter[count++] = matrix[i * (M + 2) + j];
      }
      ++j;
    }
    ++i;
  }
  return filter;
}
void use_filter(int* matrix, int* new, int* filter, int id){
  for(int i = ((N + 1) / thread_num) * id + 1; i < (id + 1) * ((N + 1) / thread_num) + 1; i++){
    for(int j = 1; j < M + 1; j++){
      new[i * (M + 2) + j] = get_median(update_filter(matrix, filter, i, j), 9);
    }
  }
}
void* thread_func(void* arg){
  int id = *(int*)arg;
  int* filter = (int*)calloc(9, sizeof(int));
  int iter = 0;
  double min = 10000000.0;
  double avg = 0.0;
  for(int i = 0; i < ITERS; i++){
    use_filter(matrix, new, filter, *(int*)arg);
    uint64_t start1, stop1;
    start1 = read_time();
    my_barrier(*(int*)arg, ++iter);
    stop1 = read_time();
    double t = (double)(stop1 - start1) / cpu_Hz;
    avg += t;
    if(t < min && t != 0.0) min = t;
    set_min_avg(&min, &avg);
    memcpy(matrix + (((N + 1) / thread_num) * id + 1) * (M + 2), new + (((N + 1) / thread_num) * id + 1) * (M + 2), sizeof(int) * ((id + 1) * ((N + 1) / thread_num) + 1 - (((N + 1) / thread_num) * id + 1)) * (M + 2));
  }
  printf("min: %0.10f\nagv: %f\n", min, avg / 3);
  free(filter);
}
int main(int argc, char* argv[]){
  matrix = (int*)calloc((1 + N + 1) * (1 + M + 1), sizeof(int));
  new = (int*)calloc((1 + N + 1) * (1 + M + 1), sizeof(int));
  int* args = (int*)malloc(128 * sizeof(int));
  srand(time(NULL));
  init_matrix(matrix);
  for(int i = 0; i < thread_num; i++){
    args[i] = i;
    if(0 != pthread_create(&threads[i][0], NULL, thread_func, (void*)(args + i))) printf("create error\n");
  }
  int iter = 0;
  double min = 10000000.0;
  double avg = 0.0;
  for(int i = 0; i < ITERS; i++){
    uint64_t start1, stop1;
    start1 = read_time();
    my_barrier(thread_num, ++iter);
    stop1 = read_time();
    double t = (double)(stop1 - start1) / cpu_Hz;
    avg += t;
    if(t < min && t != 0.0) min = t;
    set_min_avg(&min, &avg);
  }
  printf("min: %0.10f\nagv: %f\n", min, avg / (3 * 129));

  for(int i = 0; i < thread_num; i++){
    if(0 != pthread_join(threads[i][0], NULL)) printf("join error\n");
  }
  printf("gmin: %0.10f\ngagv: %f\n", gmin, gavg / (3 * 129));
  free(matrix);
  free(new);
  return 0;
}
