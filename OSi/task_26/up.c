#include <ctype.h>
#include <unistd.h>

int main(){
	char ch;

	while(read(0, &ch, 1)){
		ch = toupper(ch);
		write(1, &ch, 1);	
	}

	return 0;
}
