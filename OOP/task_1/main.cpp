#include "header.h"
int main() {
  TritSet a(1000);
  //cout << a[21.0] <<  endl;
  a[23] = 23;
  a[21] = Unknown;
  a[0] = False;
  a[988] = True;
  a[33] = True;
  cout << a[21] <<  endl;
  cout << a[0] <<  endl;
  cout << a[988] << endl;
  cout << a[33] <<  endl;
  cout << "user_capa " << a.capacity() << endl;
  return 0;
}
