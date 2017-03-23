#include <stdlib.h>
#include <stdio.h>
#include <ulimit.h>
#include <sys/resource.h>

extern char **environ;
	
int main(int argc, char* argv[]){
	char options[ ] = "ispuU:cC:dvV:";
	struct rlimit rlp;
	char **p;
	char ch;
	while ((ch = getopt(argc, argv, options)) != EOF) {
		switch (ch) {
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
				getrlimit(RLIMIT_CORE, &rlp);
				printf("Core sz = %ld\n", rlp.rlim_cur);
				break;

			case 'C':
				getrlimit(RLIMIT_CORE, &rlp);
				rlp.rlim_cur = atol(optarg);
				if (setrlimit(RLIMIT_CORE, &rlp) == -1)
				printf("Can't do it!\n");
				break;
			case 'd':
				printf("DIR: %s\n", getcwd(NULL,100));
				break;
			case 'v':
				printf("ENV:\n");
				for (p = environ; *p; p++)
				printf("%s\n", *p);
				break;
			case 'V':
				putenv(optarg);
				break;
			}
	}

	return 0;
}

