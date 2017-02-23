#include <stdio.h>
#include <dirent.h>

int main(int argc, char *argv[])
{
	DIR *dirp;
	struct dirent *dp;

	if(argc == 3){
		if ((dirp = opendir(argv[1])) == NULL) {
			perror(argv[0]);
			return 1;
		}
		
		while ((dp = readdir(dirp)) != NULL){
			printf("%-14.*s\n", dp->d_reclen, dp->d_name);
		}

		closedir(dirp);
	}
	else printf("%s: Bad arguments\n", argv[0]);

	return 0;
}
