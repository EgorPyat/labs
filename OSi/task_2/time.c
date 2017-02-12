#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <time.h>

int main(){
	time_t t;
	t = time(0);
	printf("My: %s", ctime(&t));
	putenv("TZ=PST8PDT");
	printf("Ca: %s", ctime(&t)); 

	return 0;
}
