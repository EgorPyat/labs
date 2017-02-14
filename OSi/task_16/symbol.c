#include <stdio.h>
#include <termios.h>
#include <fcntl.h>
#include <unistd.h>

int main(){
	int fd;
	struct termios terminal;
	struct termios save_set; 
	char ch;

	fd = open("/dev/tty", O_RDWR);
	tcgetattr(fd, &terminal);
	save_set = terminal;
	terminal.c_lflag = terminal.c_lflag & ( ~ICANON );
	terminal.c_cc[VMIN] = 1;
	tcsetattr(fd, TCSANOW, &terminal);
	write(fd, "Enter: ", 8);	
	read(fd, &ch, 1);
	write(fd, "\nYou entered: ", 15);
	write(fd, &ch, 1);
	write(fd, "\n", 1);
	
	tcsetattr(fd, TCSAFLUSH, &save_set);
	
	return 0;
}
