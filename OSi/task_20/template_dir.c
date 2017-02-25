#include <unistd.h>
#include <stdio.h>
#include <dirent.h>
#include <string.h>
#include <ctype.h>
#include <stdlib.h>

int suit(char* filename, char* template){
	int i = 0;
	int j = 0;
	int last = 0;
	int symb = 0;
	int find = 0;
	int star = 0;
	int ques = 0;
	int flen = strlen(filename);
	int tlen = strlen(template);

	for(; i < tlen; i++){
		switch(template[i]){
			case '/':
				printf("Bad template format\n");
				return 1;
			case '?':
				++ques;
				break;
			case '*':
				star = 1;
				break;
			default:
				symb = 1;
				for(; j < flen; j++){
					if(filename[j] == template[i]){
						find = 1;
						if(star == 0 && (j - last) != ques){
							// printf("Don't match!\n");
							return 1;
						}
			 			else if(star == 1 && (j - last) < ques){
							// printf("Don't match!\n");
							return 1;
						}
						j++;
						last = j;
						break;
					}
				}
				if(find == 0){
					// printf("Don't match! %c\n", template[i]);
					return 1;
				}
				ques = 0;
				star = 0;
				find = 0;
				break;
		}
	}
 	if(symb == 0 && star == 0 && flen != ques){
		// printf("Don't match!\n");
		return 1;
	}
	else if(symb == 0 && star == 1 && flen < ques){
		// printf("Don't match!\n");
		return 1;
	}
	else if(symb == 1 && star == 0 && ques != (flen - last)){
		// printf("Don't match!\n");
		return 1;
	}
	else if(symb == 1 && star == 1 && ques > (flen - last)){
		// printf("Don't match!\n");
		return 1;
	}
	return 0;
}

char** parse(char* template, int* sz){
	// printf("Template: %s\n", template);
	int i;
	int size = 0;
	int len = strlen(template);
	char** parsed;
	int j = 0;
	int end = 0;

	for(i = 0; i < len; i++){
		if(template[i] == '/'){
			++size;
			if(i == len - 1) end = 1;
		}
	}
	if(end == 0) ++size;
	if(size == 0) ++size;

	parsed = (char**)malloc(size);
	*sz = size;
	size = 0;

	for(i = 0; i < len; i++){
		if(template[i] == '/'){
			parsed[size] = (char*)malloc(i - j + 1);
			memcpy(parsed[size], template + j, i - j);
			parsed[size][i] = '\0';
			j = i;
			++j;
			++size;
		}
	}
	if(end == 0){
		parsed[size] = (char*)malloc(len - j + 1);
		memcpy(parsed[size], template + j, len - j);
		parsed[size][len] = '\0';
	}

	return parsed;
}

int main(int argc, char *argv[]){
	DIR *dirp;
	struct dirent *dp;
	int i;
	int find = 0;
	int sz = 0;
	char** template;
	if(argc == 3){
		if ((dirp = opendir(argv[1])) == NULL) {
			perror(argv[1]);
			return 1;
		}
		template = parse(argv[2], &sz);
		// if(template == NULL) return 0;
		// for(i = 0; i < sz; i++){
		// 	printf("%s\n", template[i]);
		// }
		while ((dp = readdir(dirp)) != NULL){
			if(!suit(dp->d_name, argv[2])) {
				printf("%-14.*s\n", dp->d_reclen, dp->d_name);
				++find;
			}
		}
		if(find == 0) printf("Template: %s\n", argv[2]);

		closedir(dirp);
	}
	else printf("%s: Bad arguments\n", argv[0]);

	return 0;
}
