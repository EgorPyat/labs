#include <stdio.h>
#include <pthread.h>
#include <unistd.h>

void* func();

int main(){
	pthread_t thr;
	int i;

	if(pthread_create(&thr, NULL, func, NULL) != 0){
		return 1;
	}

	for(i = 0; i < 10; i++){
		printf("Parent's String#%d\n", i);
		sleep(1);
	}

	return 0;
}

void* func(void* ptr){
	int i;

	for(i = 0; i < 10; i++){
		printf("Child's String#%d\n", i);
		sleep(2);
	}
}
