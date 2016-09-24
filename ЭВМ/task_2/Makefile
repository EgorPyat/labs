main.o: main.c
				gcc -c -o main.o main.c
calc_ex.o: calc_ex.c
				gcc -c -o calc_ex.o calc_ex.c
calc0: main.o calc_ex.o
				gcc -o calc.exe main.o calc_ex.o -O0
calc1: main.o calc_ex.o
				gcc -o calc.exe main.o calc_ex.o -O1
calc2: main.o calc_ex.o
				gcc -o calc.exe main.o calc_ex.o -O2
calc3: main.o calc_ex.o
				gcc -o calc.exe main.o calc_ex.o -O3
calcs: main.o calc_ex.o
				gcc -o calc.exe main.o calc_ex.o -Os
calcf: main.o calc_ex.o
				gcc -o calc.exe main.o calc_ex.o -Ofast
calcg: main.o calc_ex.o
				gcc -o calc.exe main.o calc_ex.o -Og
clean:
				rm -rf calc.exe *.o
