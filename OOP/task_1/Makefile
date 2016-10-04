main.o: main.cpp
				g++ -c -o main.o main.cpp
trit_class: trit_class.cpp
				g++ -c -o trit_class.o trit_class.cpp
ref_class: ref_class.cpp
				g++ -c -o ref_class.o ref_class.cpp
tests: tests.cpp
				g++ -c -o tests.o tests.cpp
trit: main.o trit_class.o tests.o ref_class.o
				g++ -o trit.exe main.o trit_class.o ref_class.o tests.o
clean:
				rm -rf trit.exe *.o
