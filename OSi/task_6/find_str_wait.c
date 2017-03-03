/*
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
	int i;
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
		if(read(fd, str, 4) == 0){
			printf("Time is over!\n");
			lseek(file, 0, SEEK_SET);
			while(read(file, &c, 1)){
				write(1, &c, 1);
			}
			return 0;
		};
		num = atoi(str);
		if(num == 0) {
			close(file);
			return 0;
		}
		else if(num < 0) printf("N should be > 0!\n");
		else if(num == 1){
			lseek(file, 0, SEEK_SET);
			read(file, string, lines[0]);
			string[lines[0] - 1] = '\n';
			write(1, string, lines[0]);		
		}	
		else if(num >= 2){
			lseek(file, lines[num - 2], SEEK_SET);
			read(file, string, lines[num - 1] - lines[num - 2] - 1);
			string[lines[num - 1] - lines[num - 2] - 1] = '\n';
			write(1, string, lines[num - 1] - lines[num - 2]);
		}
	}

	close(file);

	return 0;
}
*/

#include <stdio.h>
#include <sys/time.h>
#include <sys/types.h>
#include <unistd.h>

int main(void) {
	fd_set rfds;
	struct timeval tv;
	int retval;
	char string[3];
	/* Watch stdin (fd 0) to see when it has input. */
	FD_ZERO(&rfds);
	FD_SET(0, &rfds);
	/* Wait up to five seconds. */
	tv.tv_sec = 5;
	tv.tv_usec = 0;
	retval = select(1, &rfds, NULL, NULL, &tv);
	/* Don’t rely on the value of tv now! */
	read(0, string, 3);
	if (retval == -1)
	perror("select()");
	else if (retval)
	printf("Data is available now.\n");
	/* FD_ISSET(0, &rfds) will be true. */
	else
	printf("No data within five seconds.\n");
	return 0;
}
