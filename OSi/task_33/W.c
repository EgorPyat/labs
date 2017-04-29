#include <stdio.h>
#include <sys/types.h>
#include <sys/ipc.h>
#include <sys/sem.h>
#include <unistd.h>

int main(int argc, char *argv[]){
	int i;
	int sid;
	int num = atoi(argv[1]);
	int cnum = num + '0';
	struct sembuf C_M[2] = {{2, -1, 0}, {3, -1, 0}};

	if((sid = semget(getuid(), 4, IPC_CREAT | 0666)) == -1){
		return 1;
	}

	if(fork() == 0){
		execl("a.out", "a", &cnum, 0);
		return 1;
	}

	if(fork() == 0){
		execl("b.out", "b", &cnum, 0);
		return 1;
	}

	if(fork() == 0){
		execl("c.out", "c", &cnum, 0);
		return 1;
	}

	if(fork() == 0){
		execl("m.out", "m", &cnum, 0);
		return 1;
	}

	for(i = 1; i < num + 1; i++){
		if(semop(sid, C_M, 2) == -1){
			return 1;
		}
		printf("Widget #%d created!\n", i);
	}

	semctl(sid, 0, IPC_RMID, 0);
	
	return 0;
}
