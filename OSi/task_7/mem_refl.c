#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <fcntl.h>
#include <sys/mman.h>

int main(){
	int file;
	int lines[101] = {0};
	int num = 1;
	char str[4];
	int fd;
	int length;
	char *pa;
	int pos = 0;
	int i;
	fd_set rfds;
	struct timeval tv;
	int retval;

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
	pos = num;
	while(1){
		printf("Write N of string: \n");
		FD_ZERO(&rfds);
		FD_SET(0, &rfds);
		tv.tv_sec = 5;
		tv.tv_usec = 0;
		retval = select(1, &rfds, NULL, NULL, &tv);
		if(retval == 0){
			printf("Time is over!\n");
			write(1, pa, length);
			return 0;
		}
		else if(retval == -1){
			printf("select\n");
			return 0;		
		}
		else{
			read(0, str, 4);
			num = atoi(str);
			if(num == 0) {
				close(file);
				return 0;
			}
			else if(num < 0) printf("N should be > 0!\n");
			else if(num >= pos) printf("Out of range!\n");
			else if(num >= 1){
				write(1, pa + lines[num - 1], lines[num] - lines[num - 1]);
			}
		}
	}

	close(file);

	return 0;
}
