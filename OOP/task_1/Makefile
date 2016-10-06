main.o: main.cpp
				g++ -c -o main.o main.cpp -std=c++11
trit_class.o: trit_class.cpp
				g++ -c -o trit_class.o trit_class.cpp -std=c++11
ref_class.o: ref_class.cpp
				g++ -c -o ref_class.o ref_class.cpp -std=c++11
tests.o: tests.cpp
				g++ -c -o tests.o tests.cpp -std=c++11
trit: main.o trit_class.o tests.o ref_class.o
				g++ -o trit.exe main.o trit_class.o ref_class.o tests.o -std=c++11
clean:
				rm -rf trit.exe *.o
