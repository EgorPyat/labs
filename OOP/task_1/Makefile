main.o: main.cpp
				g++ -c -o main.o main.cpp
trit_class: trit_class.cpp
				g++ -c -o trit_class.o trit_class.cpp
tests: tests.cpp
				g++ -c -o tests.o tests.cpp
trit: main.o trit_class.o tests.o
				g++ -o trit.exe main.o trit_class.o tests.o
clean:
				rm -rf trit.exe *.o
