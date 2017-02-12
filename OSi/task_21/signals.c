#include <stdio.h>
#include <stdlib.h>
#include <signal.h>

int count;
void interruption(int sig){
        if (sig == SIGQUIT) {
		if(count == 1) printf("   %d interruption.\n", count);
		else printf("   %d interruptions.\n", count);
        	exit(1);
        }
	printf("   Oops!\n");
        count++;
	signal(sig, interruption);
}

int main(){
	signal(SIGINT, interruption);
	signal(SIGQUIT, interruption);

	for (;;) {}
  
	return 0;
}
