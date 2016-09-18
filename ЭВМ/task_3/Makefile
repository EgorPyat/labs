main.o: main.c
				gcc -c -o main.o main.c
calc_ex.o: calc_ex.c
				gcc -c -o calc_ex.o calc_ex.c
calc: main.o calc_ex.o
				gcc -o calc.exe main.o calc_ex.o
clean:
				rm -rf calc.exe *.o *.s
