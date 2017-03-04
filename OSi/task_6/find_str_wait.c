#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <fcntl.h>
#include <unistd.h>

int main(){
	char c;
	int file;
	int lines[101] = {0};
	int num = 1;
	char string[256];
	char str[4];
	int i;
	fd_set rfds;
	struct timeval tv;
	int retval;

	file = open("find_str_wait.c", O_RDONLY);

	while(read(file, &c, 1)){
		if(c == '\n'){
			lines[num] = lseek(file, 0, SEEK_CUR);
			++num;
		}
	}
	c = num;
	while(1){
		printf("Write N of string: \n");
		FD_ZERO(&rfds);
		FD_SET(0, &rfds);
		tv.tv_sec = 5;
		tv.tv_usec = 0;
		retval = select(1, &rfds, NULL, NULL, &tv);
		if(retval == 0){
			printf("Time is over!\n\n");
			lseek(file, 0, SEEK_SET);
			while(read(file, &c, 1)){
				write(1, &c, 1);
			}
			return 0;
		}
		else if(retval == -1){
			printf("select\n");
			return 0;
		}
		else{
			read(1, str, 4);
			num = atoi(str);
			if(num == 0){
				close(file);
				return 0;
			}
			else if(num >= c) printf("Out of range!\n");
			else if(num < 0) printf("N should be > 0!\n");
			else if(num >= 1){
				lseek(file, lines[num - 1], SEEK_SET);
				read(file, string, lines[num] - lines[num - 1] - 1);
				string[lines[num] - lines[num - 1] - 1] = '\n';
				write(1, string, lines[num] - lines[num - 1]);
			}
		}
	}

	close(file);

	return 0;
}
