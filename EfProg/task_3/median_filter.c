#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <pthread.h>
#include <unistd.h>
#include <string.h>
// L1 / 2 = 256 * 128 = 32Kb
// L2 / 2 = 512 * 256 = 128Kb
// L3 / 2= 2048 * 1538 = 3072Kb
// L3 * 10 = 8192 * 7690 = 61520Kb
const int ITERS = 3;
const int N = 12;
const int M = 10;
int* matrix = NULL;
int* new = NULL;
int thread_num = 4;
pthread_t threads[128][1];
volatile int ids[5] = {0};
void my_barrier(int id, int iter){
  int left  = (id == 0 ? (thread_num) : (id - 1));
  int right = (id == (thread_num) ? 0 : (id + 1));
  // printf("id: %d left: %d right: %d\n", id, left, right);
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
  for(int i = 0; i < ITERS; i++){
    use_filter(matrix, new, filter, *(int*)arg);
    my_barrier(*(int*)arg, ++iter);
    memcpy(matrix + (((N + 1) / thread_num) * id + 1) * (M + 2), new + (((N + 1) / thread_num) * id + 1) * (M + 2), sizeof(int) * ((id + 1) * ((N + 1) / thread_num) + 1 - (((N + 1) / thread_num) * id + 1)) * (M + 2));
  }
  free(filter);
}
int main(int argc, char* argv[]){
  matrix = (int*)calloc((1 + N + 1) * (1 + M + 1), sizeof(int));
  new = (int*)calloc((1 + N + 1) * (1 + M + 1), sizeof(int));
  int* args = (int*)malloc(128 * sizeof(int));
  srand(time(NULL));
  init_matrix(matrix);
  print_matrix(matrix);
  for(int i = 0; i < thread_num; i++){
    args[i] = i;
    if(0 != pthread_create(&threads[i][0], NULL, thread_func, (void*)(args + i))) printf("create error\n");
  }
  int iter = 0;
  for(int i = 0; i < ITERS; i++){
    my_barrier(thread_num, ++iter);
    print_matrix(matrix);
  }

  if(0 != pthread_join(threads[0][0], NULL)) printf("join error\n");
  free(matrix);
  free(new);
  return 0;
}

// matrix[0] = matrix[1 * (M + 2) + 1];
// matrix[M + 1] = matrix[1 * (M + 2) + M];
// matrix[(N + 1) * (M + 2) + 0] = matrix[N * (M + 2) + 1];
// matrix[(N + 1) * (M + 2) + (M + 1)] = matrix[N * (M + 2) + M];
