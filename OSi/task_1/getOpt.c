#include <stdlib.h>
#include <stdio.h>
#include <ulimit.h>
#include <sys/resource.h>

extern char **environ;
	
int main(int argc, char* argv[]){
	char opts[ ] = "ispuU:cC:dvV:";
	struct rlimit lim;
	char **env;
	char ch;
	char *buf;
	while ((ch = getopt(argc, argv, opts)) != EOF) {
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
				ulimit(UL_SETFSIZE, atol(optarg));
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
				printf("ENV:\n");
				env = environ;
				while(*env) printf("%s\n", *env++);
				break;
			case 'V':
				putenv(optarg);
				break;
			}
	}

	return 0;
}

