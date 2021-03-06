#include <sys/types.h>
#include <unistd.h>
#include <stdio.h>
	
int main(int argc, char *argv[])
{	
	FILE *file;
	uid_t id;
	
	if(argc < 2){
		printf("Bad argc!\n");
		return 1;
	}
	
	printf("1 RUID: %d\n1 EUID: %d\n", getuid(), geteuid());
	
	if ((file = fopen(argv[1], "r")) == NULL) {
		perror(argv[1]);
		return 1;
	}
	else {
		printf("1 Success\n");
		fclose(file);
	}
	
	setuid(id = getuid());
	
	printf("2 RUID: %d\n2 EUID: %d\n", id, getuid(), geteuid() );
	
	if ((file = fopen(argv[1], "r")) == NULL) {
		perror(argv[1]);
		return 1;
	}
	else {
		printf("2 Success\n");
		fclose(file);
	}
	
	return 0;
}	
