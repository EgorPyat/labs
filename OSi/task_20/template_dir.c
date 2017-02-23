//#include <sys/types.h>
//#include <unistd.h>
//#include <stdlib.h>
#include <stdio.h>
#include <dirent.h>
//#include <sys/stat.h>

int main(int argc, char *argv[])
{
	DIR *dirp;
	struct dirent *dp;

	if ((dirp = opendir(argv[1])) == NULL) {
		perror(argv[0]);
		return 1;
	}
	while ((dp = readdir(dirp)) != NULL){
		printf("%-14.*s\n", dp->d_reclen, dp->d_name);
	}
	closedir(dirp);
	return 0;
}
