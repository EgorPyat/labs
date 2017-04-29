#include <stdio.h>
#include <sys/types.h>
#include <sys/ipc.h>
#include <sys/sem.h>

int main(int argc, char *argv[]){
	int sid;
	int num = atoi(argv[1]);
	int i;
	struct sembuf C = {2, 1, 0};

	sid = semget(getuid(), 0, 0);

	for(i = 1; i < num + 1; i++){
		sleep(4);
		printf("Part C #%d created!\n", i);
		semop(sid, &C, 1);
	}

	return 0;
}
