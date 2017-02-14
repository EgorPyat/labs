#include <stdio.h>
#include <termios.h>
#include <fcntl.h>
#include <unistd.h>

int main(){
	int fd;
	char ch;
	struct termios terminal;
	struct termios save_set; 

	fd = open("/dev/tty", O_RDWR);
	tcgetattr(fd, &terminal);
	save_set = terminal;
	terminal.c_lflag &= ~ICANON;
	terminal.c_cc[VMIN] = 1;
	tcsetattr(fd, TCSANOW, &terminal);

	write(fd, "Enter some symbol: ", 19);	

	read(fd, &ch, 1);
	
	write(fd, "\nYou entered: ", 14);
	write(fd, &ch, 1);
	write(fd, "\n", 1);
	
	tcsetattr(fd, TCSAFLUSH, &save_set);
	
	return 0;
}
