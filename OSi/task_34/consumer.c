#include <stdio.h>
#include <sys/types.h>
#include <sys/ipc.h>
#include <sys/sem.h>
#include <sys/shm.h>

int main(){
	static struct sembuf prod = {0,-1, 0};
	static struct sembuf cons = {1, 1, 0};
	int sid, shid, i;
	char *mem;

	if((sid = semget(getuid(), 0, 0)) == -1){
		printf("Semaphores error!\n");
		return 1;
	}

	if((shid = shmget(getuid(), 0, 0)) == -1){
		printf("Mem error!\n");
		return 1;
	}

	mem = shmat(shid, 0, SHM_RDONLY);

	while(1){
		i = semop(sid, &prod, 1);
		if(i == -1) break;
		printf("Message: %s\n", mem);
		semop(sid, &cons, 1);
	}

	shmdt(mem);

	return 0;
}
