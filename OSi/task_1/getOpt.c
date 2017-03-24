#include <stdlib.h>
#include <stdio.h>
#include <ulimit.h>
#include <sys/resource.h>

extern char **environ;
	
int main(int argc, char* argv[]){
	struct rlimit lim;
	char ch;
	char *buf;
	int i = 0;

	while ((ch = getopt(argc, argv, "ispuU:cC:dvV:")) != EOF) {
		switch(ch) {
			case 'i':
				printf("RUID: %d\n", getuid());
				printf("EUID: %d\n", geteuid());
				printf("RGID: %d\n", getgid());
				printf("EGID: %d\n", getegid());
				break;
			case 's':
				setpgid(0,0);			
				break;
			case 'p':
				printf("Proc num: %ld\n", getpid());
				printf("Parent proc num: %ld\n", getppid());
				printf("Group proc num: %ld\n", getpgid(0));
				break;
			case 'u':
				printf("Ulimit = %ld\n", ulimit(UL_GETFSIZE, 0) );
				break;
			case 'U':
				if(ulimit(UL_SETFSIZE, atol(optarg)) == -1) printf("Bad arg!\n");
				break;
			case 'c':
				getrlimit(RLIMIT_CORE, &lim);
				printf("Core sz = %ld\n", lim.rlim_cur);
				break;

			case 'C':
				getrlimit(RLIMIT_CORE, &lim);
				lim.rlim_cur = atol(optarg);
				setrlimit(RLIMIT_CORE, &lim);
				break;
			case 'd':
				printf("DIR: %s\n", buf = getcwd(NULL, 256));
				free(buf);
				break;
			case 'v':
				while(*(environ + i)) {
					printf("%s\n", *(environ + i));
					++i;
				}
				break;
			case 'V':
				putenv(optarg);
				break;
			}
	}

	return 0;
}

