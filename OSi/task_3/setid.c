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

	printf("1 RUID: %d\nEUID: %d\n", getuid(), geteuid());
	
	if ((file = fopen(argv[1], "r")) == NULL) {
		return 1;
	}
	else {
		printf("1 Success\n");
		fclose(file);
	}

	setuid(id = getuid());

	printf("2 RUID: %d\nEUID: %d\n", uid, getuid(), geteuid() );

	if ((file = fopen(argv[1], "r")) == NULL) {
		return 1;
	}
	else {
		printf("2 Success\n");
		fclose(fp);
	}
	
	return 0;
}
