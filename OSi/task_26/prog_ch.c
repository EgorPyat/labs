#include <stdio.h>

int main(){
	FILE *file;
	char* text[4] = {"Hello", ", " , "World", "!\n"};
	int i;

	file = popen("./up", "w");

	for(i = 0; i < 4; i++){
		fputs(text[i], file);
	}

	pclose(file);
}
