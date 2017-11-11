#include <stdio.h>
#include <stdlib.h>
#include <time.h>
// L1 / 2 = 256 * 128 = 32Kb
// L2 / 2 = 512 * 256 = 128Kb
// L3 / 2= 2048 * 1538 = 3072Kb
// L3 * 10 = 8192 * 7690 = 61520Kb
const int N = 5;
const int M = 5;

void init_matrix(int* matrix){
  for(int i = 0; i < N + 2; i++){
    for(int j = 0; j < M + 2; j++){
      if(i != 0 && i != N + 1 && j != 0 && j != M + 1){
        matrix[i * (N + 2) + j] = rand() % 255;
      }
    }
  }
  matrix[0] = matrix[1 * (N + 2) + 1];
  matrix[N + 1] = matrix[1 * (N + 2) + M];
  matrix[(N + 1) * (N + 2) + 0] = matrix[N * (N + 2) + 1];
  matrix[(N + 1) * (N + 2) + (M + 1)] = matrix[N * (N + 2) + M];
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
  int t = j;
  --i;
  for(int k = 0; k < 3; k++){
    j = t;
    --j;
    for(int l = 0; l < 3; l++){
      filter[count++] = matrix[i * (N + 2) + j];
      ++j;
    }
    ++i;
  }
  return filter;
}
void use_filter(int* matrix, int* new, int* filter){
  for(int i = 1; i < N + 1; i++){
    for(int j = 1; j < M + 1; j++){
      new[i * (N + 2) + j] = get_median(update_filter(matrix, filter, i, j), 9);
    }
  }
}
void print_matrix(int* matrix){
  printf("\n\n");
  for(int i = 0; i < N + 2; i++){
    printf("\t");
    for(int j = 0; j < N + 2; j++){
      printf("%4d ", matrix[i * (N + 2) + j]);
    }
    printf("\n\n");
  }
  printf("\n");
}
int main(int argc, char* argv[]){
  int* matrix = (int*)calloc((1 + N + 1) * (1 + M + 1), sizeof(int));
  int* new = (int*)calloc((1 + N + 1) * (1 + M + 1), sizeof(int));

  int* filter = (int*)calloc(9, sizeof(int));
  srand(time(NULL));
  init_matrix(matrix);
  print_matrix(matrix);
  use_filter(matrix, new, filter);
  print_matrix(new);
  return 0;
}
