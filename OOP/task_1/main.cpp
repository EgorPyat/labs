#include "header.h"
int main() {
  TritSet a(1000);
  //cout << a[21.0] <<  endl;
  a[21] = Unknown;
  a[0] = False;
  a[23] = 23;
  a[33] = True;
  cout << a[0.0] <<  endl;
  cout << a[33.0] <<  endl;
  cout << a[21.0] <<  endl;
  cout << "user_capa " << a.capacity() << endl;
  cout << "size_t " << sizeof(double) << endl;
  cout << "uint " << sizeof(unsigned int) << endl;
  return 0;
}
