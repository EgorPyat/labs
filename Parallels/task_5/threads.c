#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>
#include <pthread.h>
#include <mpi.h>

int createThreads();
void* doTasks(void*);
void* sendTasks(void*);

const int TASKS = 160;

int ids[2] = {0,1};
pthread_t thrs[2];
pthread_mutex_t mutex;

int main(int argc, char* argv[]){
  int size;
  int rank;
  int provided;
  int taskNum;
  int processTaskNum;
  int i;
  int *tasks;
  double t1, t2, mt;

  // pthread_attr_t attrs;

  MPI_Init_thread(&argc, &argv, MPI_THREAD_MULTIPLE, &provided);
  MPI_Comm_size(MPI_COMM_WORLD, &size);
  MPI_Comm_rank(MPI_COMM_WORLD, &rank);

  if(provided != MPI_THREAD_MULTIPLE) {
    printf("Can't get needed level!\n");
    MPI_Finalize();
    return 1;
  }

  srand(0);
  pthread_mutex_init(&mutex, NULL);

  taskNum = TASKS;
  processTaskNum = taskNum / size;

  tasks = (int*)malloc(sizeof(int) * processTaskNum);

  for(i = 0; i < processTaskNum; i++){
    tasks[i] = rank * 8 + rand() % 8;
  }

  t1 = MPI_Wtime();

  createThreads();
  // if(pthread_attr_init(&attrs) != 0){
  //   perror("Cannot initialize attributes!");
  //   return 1;
  // };
  //
  // if(pthread_attr_setdetachstate(&attrs, PTHREAD_CREATE_JOINABLE) != 0){
  //   perror("Error in setting attributes!");
  //   return 1;
  // }
  //
  // if(pthread_create(&thrs[0], &attrs, doTasks, &ids[0]) != 0){
  //   perror("Cannot create a DO thread!");
  //   return 1;
  // }
  //
  // if(pthread_create(&thrs[1], &attrs, sendTasks, &ids[1]) != 0){
  //   perror("Cannot create a SEND thread!");
  //   return 1;
  // }
  //
  // pthread_attr_destroy(&attrs);
  //
  // for(i = 0; i < 2; i++){
  //   if(pthread_join(thrs[i], NULL) != 0){
  //     perror("Cannot join a thread");
  //     return 1;
  //   }
  // }

  t2 = MPI_Wtime();

  t2 -= t1;

  MPI_Reduce(&t2, &mt, 1, MPI_DOUBLE, MPI_MAX, 0, MPI_COMM_WORLD);

  if(rank == 0){
    printf("Tasks = %d\nThreads = %d\nTime = %f\n", TASKS, size, mt);
  }

  MPI_Finalize();

  return 0;
}

int createThreads(){
  pthread_attr_t attrs;
  int i;

  if(pthread_attr_init(&attrs) != 0){
      perror("Cannot initialize attributes!");
      return 1;
  };

  if(pthread_attr_setdetachstate(&attrs, PTHREAD_CREATE_JOINABLE) != 0){
    perror("Error in setting attributes!");
    return 1;
  }

  if(pthread_create(&thrs[0], &attrs, doTasks, &ids[0]) != 0){
    perror("Cannot create a DO thread!");
    return 1;
  }

  if(pthread_create(&thrs[1], &attrs, sendTasks, &ids[1]) != 0){
    perror("Cannot create a SEND thread!");
    return 1;
  }

  pthread_attr_destroy(&attrs);

  for(i = 0; i < 2; i++){
    if(pthread_join(thrs[i], NULL) != 0){
      perror("Cannot join a thread");
      return 1;
    }
  }

  return 0;
}

void* doTasks(void* args){
  for(int i = 0; i < 100; i++){
    printf("1\n");
  }
}

void* sendTasks(void* args){
  for(int i = 0; i < 100; i++){
    printf("2\n");
  }
}
