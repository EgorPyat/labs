#include <fcntl.h>
#include <semaphore.h>
#include <stdio.h>
#include <unistd.h>

int main() {
	sem_t *sem1;
	sem_t *sem2;
	pid_t child;

	sem1 = sem_open("/a", O_CREAT | O_EXCL, 0600, 0);

	if (sem1 == SEM_FAILED){
		printf("open error!\n");
		return -1;
	}
	sem2 = sem_open("/b", O_CREAT | O_EXCL, 0600, 1);

	if(sem2 == SEM_FAILED){
		printf("open error!\n");
		return -1;
	}

	if(child = fork()){
		for(int i = 0; i < 10; i++){
			if(0 != sem_wait(sem1)){
				printf("wait error!\n");
			}
			printf("Child\n");
			if(0 != sem_post(sem2)){
				printf("post error!\n");
			}
		}
	}
	else{
		if(child < 0){
			printf("fork error!\n");
			return -1;
		}
		for(int i = 0; i < 10; i++){
			if(0 != sem_wait(sem2)){
				printf("wait error!\n");
			}
			printf("Parent\n");
			if(0 != sem_post(sem1)){
				printf("post error!\n");
			}
		}
		if(0 != sem_unlink("/a")){
			printf("unlink error!\n");
		}
		if(0 != sem_unlink("/b")){
			printf("unlink error!\n");
		}
	}

	if(0 != sem_close(sem1)){
		printf("close error!\n");
	}
	if(0 != sem_close(sem2)){
		printf("close error!\n");
	}

	return 0;
}
