#include "header.h"
int main() {
  TritSet a(1000);
  a[23] = 23;
  a[0] = False;
  // a[0] = True;
  a[1] = True;
  a[21] = Unknown;
  a[33] = True;
  a[500] = False;
  // a[500] = True;
  // a[988] = True;
  a[997] = True;

  a[998] = False;
  a[999] = False;
  // a[1000] = Unknown;
  // int c = a[999];
  // a[1000] = True;
  cout << a[0] <<  endl;
  cout << a[1] << endl;
  cout << a[21] <<  endl;
  cout << a[33] <<  endl;
  cout << a[500] << endl;
  cout << a[997] << endl;
  cout << a[998] << endl;
  cout << a[999] << endl;
  cout << a << endl;
  // a = !a;
  TritSet b = a;
  b[100] = True;
  TritSet ca = !a;
  !a;
  cout << ca << endl;
  cout << a << endl;
  cout << b << endl;
  // a & b;
  // a | b;
  // TritSet ca = !a;
  // cout << ca << endl;
  // cout << "user_capa " << a.capacity() << endl;
  // int c = a[1] == 1;
  // cout << (a[1] == 2) << endl;
  // cout << c << endl;
  return 0;
}
