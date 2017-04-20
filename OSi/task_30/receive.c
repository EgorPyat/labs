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
	
	int mid, rtn, i;

	mid = msgget(getuid(), 0);

	for(i = 0; i < 4; i++){
		rtn = msgrcv(mid, &buf, 64, 0, 0);
		printf("Message size: %d | Type: %ld | Message: %s\n",rtn, buf.mtype, buf.mtext);
	}

	buf.mtype = 2;

	msgsnd(mid, &buf, 0, 0);
	
	return 0;
}
