#include <stdio.h>
#include <sys/types.h>
#include <sys/ipc.h>
#include <sys/sem.h>

int main(int argc, char *argv[]){
	int sid;
	int num = atoi(argv[1]);
	int i;
	struct sembuf A_B[2] = {{0, -1, 0}, {1, -1, 0}};
	struct sembuf M = {3, 1, 0};

	sid = semget(getuid(), 0, 0);

	for(i = 1; i < num + 1; i++){
		semop(sid, A_B, 2);
		printf("Module #%d created!\n", i);
		semop(sid, &M, 1);
	}

	return 0;
}
