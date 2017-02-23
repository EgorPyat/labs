#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <fcntl.h>
#include <sys/mman.h>

int main(){
	int file;
	int lines[100];
	int num = 0;
	char str[4];
	int fd;
	int length;
	char *pa;
	int pos = 0;
	int i;

	file = open("mem_refl.c", O_RDONLY);
	length = lseek(file, 0, SEEK_END);
	pa = mmap(0, length, PROT_READ, MAP_SHARED, file, 0);
	
	for(i = 0; i < length; i++){
		if(*(pa + i) == '\n'){
			++pos;
			lines[num] = pos;
			++num;					
		}
		else ++pos;
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
			write(1, pa, length);
			return 0;
		};
		num = atoi(str);
		if(num == 0) return 0;
		else if(num < 0) printf("N should be > 0!\n");
		else if(num == 1){
			write(1, pa, lines[0]);	
		}	
		else if(num >= 2){
			write(1, pa + lines[num - 2], lines[num - 1] - lines[num - 2]);
		}
	}

	close(file);

	return 0;
}
