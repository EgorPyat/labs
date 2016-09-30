#include "header.h"
int main() {
  TritSet a(1000);
  // a[23] = 23;
  a[0] = False;
  a[1] = True;
  a[21] = Unknown;
  a[33] = True;
  a[988] = True;
  a[997] = True;
  a[998] = False;
  a[999] = False;
  // int c = a[999];
  // cout << "c " << c << endl;
  // a[1000] = True;
  cout << a[0] <<  endl;
  cout << a[1] << endl;
  cout << a[21] <<  endl;
  cout << a[33] <<  endl;
  cout << a[997] << endl;
  cout << a[998] << endl;
  cout << a[999] << endl;
  cout << a << endl;
  cout << "user_capa " << a.capacity() << endl;
  return 0;
}
