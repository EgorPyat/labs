#include <sys/types.h>
#include <unistd.h>
#include <fcntl.h>

int main(int argc, char *argv[]){
	struct flock str;
	int file;

	if ((file = open("catch.c", O_RDWR)) == -1) {
		return 1;
	}

	str.l_type = F_WRLCK;
	str.l_whence = SEEK_SET;
	str.l_start = 0;
	str.l_len = 0;

	if (fcntl(file, F_SETLK, &str) == -1) {
		return 1;
	}

	system("vi catch.c");
		
	return 0;	
}
