#include <stdio.h>
#include <dirent.h>
#include <string.h>
#include <ctype.h>

int suit(char* filename, char* template){
	int flen;
	int tlen;
	int i = 0;
	int j = 0;
	int s = 0;
	int t = 0;
	int symbol = 0;
	int find = 0;
	int star = 0;
	int quest = 0;
	
	flen = strlen(filename);
	tlen = strlen(template);

	for(; i < tlen; i++){
		switch(template[i]){
			case '/': 
				printf("Bad template format\n");
				return 1;
		
			case '?': 
				++quest;
				break;
			case '*': 
				star = 1;
				break;
			default:
				symbol = 1;
				for(; j < flen; j++){
					if(filename[j] == template[i]){
						find = 1;			
						if((j - s) < quest){
							printf("Don't match!\n");
							return 1;					
						}	
						if((j - s) > quest && star == 0){
							printf("Don't match!\n");
							return 1;					
						}	
						j++;	
						break;	
					}		
				}			
				if(find == 0){
					printf("Don't match! %c\n", template[i]);
					return 1;					
				}
				s = j;	
				quest = 0;
				star = 0;	
				break;	
		}
	}
	if(symbol == 0 && flen > quest && star == 0){
		printf("Don't match!\n");
		return 1;	
	}

	return 0;
}

int main(int argc, char *argv[]){
	DIR *dirp;
	struct dirent *dp;
	
	if(argc == 3){
		if ((dirp = opendir(argv[1])) == NULL) {
			perror(argv[1]);
			return 1;
		}		
		while ((dp = readdir(dirp)) != NULL){
			if(!suit(dp->d_name, argv[2])) printf("%-14.*s\n", dp->d_reclen, dp->d_name);
			else printf("%s\n", argv[2]);
		}
		closedir(dirp);
	}
	else printf("%s: Bad arguments\n", argv[0]);

	return 0;
}
