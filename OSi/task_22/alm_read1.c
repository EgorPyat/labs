#include <stdio.h>
#include <fcntl.h>
#include <signal.h>
#include <errno.h>
#include <stdlib.h>
#include <string.h>

extern int errno;
int alm = 0;

void handle(int sig){
	signal(sig, handle);
	alm = 1;
}

int main(int argc, char *argv[]){
	int file[3];
	int i, j;
	int num;
	char str[64];
	int fn = argc;
	int fc = argc - 1;

	if(argc == 1 || argc > 4){
		printf("Bad args!\n");
		return 1;
	}	
	
	signal(SIGALRM, handle);

	for(i = 1; i < fn; i++){
		file[i - 1] = open(argv[i], O_RDONLY);
		if(file[i - 1] == -1){
			printf("Can't open: %s!\n", argv[i]);
			--fc;
			continue;
		}
	}

	while(fc){
		for(i = 0; i < fc; i++){
			if(file[i] == -1) continue;
			printf("%s:\n", argv[i + 1]);
			alarm(5);
			str[0] = '\0';
			if((num = read(file[i], str, 64)) <= 0){
				printf("%d\n", num);
				if(errno == EINTR && alm){
					errno = alm = 0;					
					continue;
				}
				else{
					close(file[i]);
					--fc;
					file[i] = -1;
				}
			}
			alarm(0);
			str[num] = '\0';
			printf("%s", str);
		}
	}	
	
	return 0;
}
