#include "header.h"

int main() {
  int capa;
  unsigned int size;

  cin >> capa;
  //size = 2*capa/8/sizeof(unsigned int);
  //unsigned int *cont = (unsigned int *)malloc();

  //cout << sizeof(unsigned int) << "\n" << size << "\n";
  //cout << (size+1)*sizeof(unsigned int)*8/2 << endl;

  enum Trit{Unknown, False, True};
  //cout << False << Unknown << True << endl;
  TritSet a(capa);
  //cout << "r: " << a.capacity() << endl;
  /*cout << a[4] << endl;
  cout << a[100] << endl;
  cout << a[992] << endl;*/
  //a[2] = Unknown;
  //a[0] = True;
  //cout << a[2] << endl;
  //cout << a[0] << endl;
  a[7] = True;
  a[100] = True;
  a[100] = False;
  //cout << "re " << a.trit_value << endl;
  //cout << "er " << a.trit_index << endl;
  cout << "a[7] = " << a[7] << endl;
  cout << "a[100] = " << a[100] << endl;

  return 0;
}
