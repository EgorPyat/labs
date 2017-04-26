#include <sys/types.h>
#include <unistd.h>
#include <stdio.h>
#include <sys/ipc.h>
#include <sys/msg.h>
#include <string.h>

int main(){
	int mid;
	struct {
		long mtype;
		char mtext[64];
	}buf;

	char name[16];
	int eof = 0;
	int live = 5;
	int i;
	pid_t pid;
	int chpid[5];

	if((mid = msgget(getuid(), IPC_CREAT | 0666)) == -1){
		printf("Queue error!\n");
		return 1;
	}

	for(i = 0; i < 5; i++){
		pid = fork();
		if(pid == -1){
			printf("Fork error!\n");
			return 1;
		}
		else if(pid == 0){
			sprintf(name, "Child#%d", i);
			execl("receive.out", name, NULL);
			printf("Exec error!\n");
			return 1;
		}
		else chpid[i] = pid;
	}

	while(1){
		printf("Enter message: ");
		if(fgets(buf.mtext, 64, stdin) == NULL){
			printf("\n");
			++eof;
			strcpy(buf.mtext, "FIN!\n");
		}
		buf.mtext[strlen(buf.mtext) - 1] = '\0';

		for(i = 0; i < 5; i++){
			buf.mtype = chpid[i];
			if(msgsnd(mid, &buf,  strlen(buf.mtext) + 1,  0) == -1) {
				printf("Send to main error!\n");
				return 1;
			}

		}

		if(eof) break;
		
		sleep(1);
	}

	while(live){ 
		for(i = 0; i < 5; i++){
    	 		if(msgrcv(mid, &buf, 64, 2, IPC_NOWAIT) != -1){
       				if(!strcmp(buf.mtext, "FIN!")) live--;
			}
		}
   	} 

	msgctl(mid, IPC_RMID, NULL);

	return 0;
}
