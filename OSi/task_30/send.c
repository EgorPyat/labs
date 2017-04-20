#include <sys/types.h>
#include <unistd.h>
#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <sys/ipc.h>
#include <sys/msg.h>

int main(){
	struct msgbuf {
		long mtype;
		char mtext[64];
	}buf;

	char *messages[4] = {"Hello", ",", "World", "!"};
	
	int mid, rtn, i;

	mid = msgget(getuid(), IPC_CREAT | 0666);
	
	buf.mtype = 1;

	for(i=0; i < 4; i++){
		strcpy(buf.mtext, messages[i]);
		msgsnd(mid, &buf, strlen(buf.mtext) + 1, 0);
	}

	msgrcv(mid, &buf, 64, 2, 0);
	
	msgctl(mid, IPC_RMID, NULL);

	return 0;
}
