#include <sys/types.h>
#include <unistd.h>
#include <stdio.h>
#include <sys/ipc.h>
#include <sys/msg.h>
#include <string.h>

int main(int argc, char *argv[]){
	pid_t pid;
	char *valuep;

	struct {
		long mtype;
		char mtext[64];
	} buf;

	pid = msgget(getuid(), 0);

	
	while(1){
		msgrcv(pid, &buf, 64, getpid(), 0);
		printf("%s  received '%s'\n", argv[0], buf.mtext);
		if(!strcmp(buf.mtext, "FIN!")) break;
	}

	buf.mtype = 2;
	strcpy(buf.mtext, "FIN!");

	if(msgsnd(pid, &buf, strlen(buf.mtext) + 1,  0) == -1) {
		printf("Send error!\n");
		return 1;
	}
	return 0;
}
