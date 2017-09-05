#include <stdio.h>
#include <pthread.h>
#include <unistd.h>

void* func();

int main(){
	pthread_t thr;
	int i;
	char *m1[] = {"A", "1", "B", "2", "C"};

	if(pthread_create(&thr, NULL, func, m1) != 0){
		return 1;
	}

	sleep(2);

	pthread_cancel(thr);

	pthread_join(thr, NULL);

  	return 0;
}

void* func(void* ptr){
	char** message;
	int i;
	message = (char**)ptr;

	for(i = 0; i < 5; i++){
		printf("%s\n", message[i]);
		sleep(1);
	}
}
