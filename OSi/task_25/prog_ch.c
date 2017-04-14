#include <sys/types.h>
#include <ctype.h>
#include <stdio.h>
#include <unistd.h>
#include <string.h>

int main(){
	int pid;
	int fields[2];
	char* text[4] = {"Hello", ", " , "World", "!\n"};
	char ch;
	int i;

	if(pipe(fields) == -1){
		printf("Pipe error!\n");
		return 1;
	}
	if((pid = fork()) > 0){
		close(fields[0]);
		for(i = 0; i < 4; i++){
			 write(fields[1], text[i], strlen(text[i]));
		}
 		close(fields[1]);
	}
	else if(pid == 0){
		close(fields[1]);	
		while(read(fields[0], &ch, 1)){
			ch = toupper(ch);
			write(1, &ch, 1);	
		}
		close(fields[0]);
	}
	else{	
		printf("Fork error!\n");
		return 1;
	}

	return 0;
}
