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
	file = open("find_str.c", O_RDONLY);

	while(read(file, &c, 1)){
		if(c == '\n'){
			lines[num] = lseek(file, 0, SEEK_CUR);
			++num;					
		}
	}

	while(1){
		printf("Write N of string: ");
		scanf("%d", &num);
		if(num == 0) return 0;
		if(num < 0) printf("N should be > 0!\n");
		if(num == 1){
			lseek(file, 0, SEEK_SET);
			read(file, string, lines[0]);
			string[lines[0] - 1] = '\n';
			write(1, string, lines[0]);		
		}	
		else if( num >= 2){
			lseek(file, lines[num - 2], SEEK_SET);
			read(file, string, lines[num - 1] - lines[num - 2] - 1);
			string[lines[num - 1] - lines[num - 2] - 1] = '\n';
			write(1, string, lines[num - 1] - lines[num - 2]);
		}
	}

	close(file);

	return 0;
}
