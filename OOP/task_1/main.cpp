#include "header.h"

int main() {
  int capa;
  unsigned int size;
  cin >> capa;
  size = 2*capa/8/sizeof(unsigned int);
  //unsigned int *cont = (unsigned int *)malloc();

  cout << sizeof(unsigned int) << "\n" << size << "\n";
  cout << (size+1)*sizeof(unsigned int)*8/2 << endl;

  TritSet a(capa);
  cout << "r: " << a.capacity() << endl;

  return 0;
}
