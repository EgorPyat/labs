#include <stdio.h> 
#include <sys/types.h>
#include <sys/ipc.h>
#include <sys/sem.h>
#include <sys/shm.h>
#include <string.h>

int main(){
	struct sembuf prod = {0, 1, 0};
	struct sembuf cons = {1,-1, 0};
	struct sembuf cons1 = {1, 1, 0}; 
	int sid, shid, i;
	char *mem;
	char *messages[4] = {"Hello", ",", "World", "!"};

	if((sid = semget(getuid(), 2, IPC_CREAT | 0666)) == -1){
		printf("Semaphores error!\n");
		return 1;
	}

	if((shid = shmget(getuid(), 16, IPC_CREAT | 0666)) == -1){
		printf("Mem error!\n");
		return 1;
	}

	mem = shmat(shid, 0, 0);
	semop(sid, &cons1, 1);
	for(i = 0; i < 4; i++){
		semop(sid, &cons, 1);
		strcpy(mem, messages[i]);
		semop(sid, &prod, 1);
	}
	semop(sid, &cons, 1);
	shmdt(mem);

	shmctl(shid, IPC_RMID, 0);
	semctl(sid, 0, IPC_RMID, 0);

	return 0;
}
