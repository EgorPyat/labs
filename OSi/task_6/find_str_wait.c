#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <fcntl.h>

int main(){
	char c;	
	int file;
	int lines[100];
	int num = 0;
	char string[256];
	char str[4];
	int fd;
	file = open("find_str_wait.c", O_RDONLY);

	while(read(file, &c, 1)){
		if(c == '\n'){
			lines[num] = lseek(file, 0, SEEK_CUR);
			++num;					
		}
	}
	if ((fd = open("/dev/tty", O_RDONLY | O_NDELAY)) == -1) {
		perror("/dev/tty");
		exit(2);
	}
	while(1){
		printf("Write N of string: \n");
		sleep(5);
		if(read(fd, str, 4) == 0) return 0;
		num = atoi(str);
		if(num == 0) return 0;
		else if(num < 0) printf("N should be > 0!\n");
		else if(num == 1){
			lseek(file, 0, SEEK_SET);
			read(file, string, lines[0]);
			string[lines[0] - 1] = '\0';
			printf("%s\n", string);		
		}	
		else if( num >= 2){
			lseek(file, lines[num - 2], SEEK_SET);
			read(file, string, lines[num - 1] - lines[num - 2] - 1);
			string[lines[num - 1] - lines[num - 2] - 1] = '\0';
			printf("%s\n", string);
		}
	}

	close(file);

	return 0;
}
