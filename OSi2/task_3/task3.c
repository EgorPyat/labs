#include <stdio.h>
#include <pthread.h>

void* func();

int main(){
	pthread_t thr[4];
	int i;
	char *m1[2] = {"A", "1"};
	char *m2[2] = {"B", "2"};
	char *m3[2] = {"C", "3"};
	char *m4[2] = {"D", "4"};

	if(pthread_create(&thr[0], NULL, func, m1) != 0){
		return 1;
	}
	if(pthread_create(&thr[1], NULL, func, m2) != 0){
		return 1;
	}
	if(pthread_create(&thr[2], NULL, func, m3) != 0){
		return 1;
	}
	if(pthread_create(&thr[3], NULL, func, m4) != 0){
		return 1;
	}

	for (i = 0; i < 4; i++){
		pthread_join(thr[i], NULL);
	}

	return 0;
}

void* func(void* ptr){
	char** message;
	int i;
	message = (char**)ptr;

	for(i = 0; i < 2; i++){
		printf("%s\n", message[i]);
	}
}
