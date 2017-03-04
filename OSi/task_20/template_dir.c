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
							return 1;
						}
			 			else if(star == 1 && (j - last) < ques){
							return 1;
						}
						j++;
						last = j;
						break;
					}
				}
				if(find == 0){
					return 1;
				}
				ques = 0;
				star = 0;
				find = 0;
				break;
		}
	}
 	if(symb == 0 && star == 0 && flen != ques){
		return 1;
	}
	else if(symb == 0 && star == 1 && flen < ques){
		return 1;
	}
	else if(symb == 1 && star == 0 && ques != (flen - last)){
		return 1;
	}
	else if(symb == 1 && star == 1 && ques > (flen - last)){
		return 1;
	}
	return 0;
}

char** parse(char* template, int* sz){
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
			parsed[size][i - j] = '\0';
			j = i;
			++j;
			++size;
		}
	}
	if(end == 0){
		parsed[size] = (char*)malloc(len - j + 1);
		memcpy(parsed[size], template + j, len - j);
		parsed[size][len - j] = '\0';
	}

	return parsed;
}

void dir(char** template, int* sz, int* count, char* argv, char* path){
	struct dirent *dp;
	DIR* dirp;
	int i;
	int len;

	if ((dirp = opendir(argv)) == NULL) {
		(*count)--;
		return;
	}
	chdir(argv);
	if(*count == 0) printf("Current: %s/\n", argv);
	if(*count > 0) path = strcat(strcat(path,"/"), argv);
	len = strlen(path);

	while ((dp = readdir(dirp)) != NULL){
		if(strcmp(".", dp->d_name) == 0 || strcmp("..", dp->d_name) == 0) continue;
		if(!suit(dp->d_name, template[*count])){
			(*count)++;
			if(*count != *sz){
				dir(template, sz, count, dp->d_name, path);
				path[len] = '\0';

			}
			else {
				for(i = 0; i < *count; i++) printf("\t");
				path = strcat(strcat(path,"/"), dp->d_name);
				printf("%s\n", path);
				(*count)--;
				path[len] = '\0';
			}
		}
	}
	chdir("..");
	(*count)--;
	closedir(dirp);
}

int main(int argc, char *argv[]){
	DIR *dirp;
	struct dirent *dp;
	int i;
	int find = 0;
	int sz = 0;
	char** template;
	printf("%s\n", argv[2]);
	if(argc == 3){
		template = parse(argv[2], &sz);
		dir(template, &sz, &find, argv[1], argv[1]);
	}
	else printf("%s: Bad arguments\n", argv[0]);

	return 0;
}
