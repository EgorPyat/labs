#include <libgen.h>
#include <stdlib.h>
#include <stdio.h>
#include <sys/types.h>
#include <time.h>

int main(){
	FILE *fp[2];
	char ch[4];	
	int i;
	
	srand(time(0));

	p2open("sort", fp);
	
	for(i = 0; i < 100; i++){
		sprintf(ch, "%02d\n", rand() % 100);
		fputs(ch, fp[0]);
	}

	fclose(fp[0]);

	for(i = 0; i < 100; i++){
		fgets(ch, 4, fp[1]);
		ch[2] = '\0';
		printf("%s ", ch);
		if((i + 1) % 10 == 0){
			printf("\n");
		} 	
	}
	
	fclose(fp[1]);	
	
	return 0;
}
