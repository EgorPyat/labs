#include <stdio.h>
#include <sys/types.h>
#include <unistd.h>
#include <stdlib.h>
#include <wait.h>

int main(int argc, char *argv[]){
	int ex_code;
	pid_t proc;
	proc = fork();
	if(proc == 0){
		if(argc > 1){
			execvp(argv[1], &argv[1]);		
		}	
	}
	else if(proc == -1){
		return 1;
	}
	
	wait(&ex_code);
	
	if(WIFEXITED(ex_code)) printf("Process finished with status: %d.\n", WEXITSTATUS(ex_code)); 
		
	return 0;
}
