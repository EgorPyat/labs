#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>
#include <pthread.h>
#include <mpi.h>

int createThreads();
void* doTasks(void*);
void* sendTasks(void*);

const int TASKS = 40;
int processTaskNum;
int balance;

int *tasks;
int nextTask;
int ids[2] = {0,1};
pthread_t thrs[2];
pthread_mutex_t mutex;

int size;
int rank;

int main(int argc, char* argv[]){
  int provided;
  int i;
  double t1, t2, mt;

  balance = atoi(argv[1]);

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

  processTaskNum = TASKS / size;

  tasks = (int*)malloc(sizeof(int) * processTaskNum);

  for(i = 0; i < processTaskNum; i++){
    tasks[i] = 1 + rank * 8 + rand() % 8;
  }

  t1 = MPI_Wtime();

  createThreads();

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

  if(balance == 1){
    if(pthread_create(&thrs[1], &attrs, sendTasks, &ids[1]) != 0){
      perror("Cannot create a SEND thread!");
      return 1;
    }
  }

  pthread_attr_destroy(&attrs);

  if(pthread_join(thrs[0], NULL) != 0){
    perror("Cannot join a thread");
    return 1;
  }

  if(balance == 1){
    if(pthread_join(thrs[1], NULL) != 0){
      perror("Cannot join a thread");
      return 1;
    }
  }

  return 0;
}

void* doTasks(void* args){
  int time;
  int i;
  int request;

  for(nextTask = 0; nextTask < processTaskNum;){
    pthread_mutex_lock(&mutex);
    time = tasks[nextTask];
    tasks[nextTask] = -1;
    ++nextTask;
    pthread_mutex_unlock(&mutex);
    sleep(time);
    printf("Rank#%d did task.\n", rank);
  }

  if(balance == 1){
    for(i = 0; i < size; i++){
      request = 1;

      while(1){
        MPI_Send(&request, 1, MPI_INT, (rank + i) % size, 0, MPI_COMM_WORLD);
        int answer;
        MPI_Recv(&answer, 1, MPI_INT, (rank + i) % size, 1, MPI_COMM_WORLD, 0);
        if(answer == -1) break;

        sleep(answer);
        printf("Rank#%d did alien task from Rank#%d.\n", rank, (rank + i) % size);
      }
    }
    
    MPI_Barrier(MPI_COMM_WORLD);
    request = 0;
    MPI_Send(&request, 1, MPI_INT, rank, 0, MPI_COMM_WORLD);
  }
  printf("Rank#%d finished tasks.\n", rank);
}

void* sendTasks(void* args){
  MPI_Status st;
  int request;
  int answer;

  while(1){
    MPI_Recv(&request, 1, MPI_INT, MPI_ANY_SOURCE, 0, MPI_COMM_WORLD, &st);
    if(request == 0) break;

    pthread_mutex_lock(&mutex);

    if(nextTask == processTaskNum){
      answer = -1;
    }
    else{
      answer = tasks[nextTask];
      ++nextTask;
    }

    pthread_mutex_unlock(&mutex);

    MPI_Send(&answer, 1, MPI_INT, st.MPI_SOURCE, 1, MPI_COMM_WORLD);

  }
  printf("Rank#%d finished sending tasks.\n", rank);
}
