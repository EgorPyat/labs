#include "header.h"
int main() {
	TritSet a(1000);
	// a[23] = 23;
	a[0] = False;
	a[1] = True;
	a[2] = a[1];
	a[21] = Unknown;
	a[33] = True;
	a[500] = False;
	a[500] = True;
	a[988] = True;
	a[997] = True;

	a[998] = False;
	a[999] = False;
	//a[1000] = True;
	a[1005] = True;
	//cout << a[1007] << endl;
	a[1008] = True;
	// a[2000] = Unknown;
	//  cout << "sd " << a[2000] << endl;
	a[1100] = True;
  a[1500] = False;
	a[1700] = True;
	a[2000] = False;
  cout << "capa " << a.capacity() << endl;
  // getchar();
  // cout << "l: " << a[1500] << endl;
  // getchar();
	cout << "s" << a[0] << endl;
	cout << a[1] << endl;
	cout << a[21] << endl;
	cout << a[33] << endl;
	cout << a[500] << endl;
	cout << a[997] << endl;
	cout << a[998] << endl;
	cout << a[999] << endl;
	cout << "a:" << a << endl;
	TritSet c(1000);
	c = a;
	c[10] = False;
	cout << "c:" << c << endl;
	a = ~c;
	cout << "~a:" << a << endl;
	TritSet b = ~a;
	//b = ~a;
	cout << a[0] << endl;
	b[100] = a[0];
	b[101] = a[999];
	cout << b[100] << endl;
	int y = b[100];
	cout << "y:" << y << endl;
	cout << "b:" << b << endl;
	//b = ~a;

	// a.flip();
	// cout << b << endl;
	// b[100] = True;
	// TritSet ca = !a;
	// !a;
	// cout << ca << endl;
	// cout << a << endl;
	// cout << b << endl;
	// a & b;
	// a | b;
	// TritSet ca = !a;
	// cout << ca << endl;
	// cout << "user_capa " << a.capacity() << endl;
	// int c = a[1] == 1;
	// cout << (a[1] == 2) << endl;
	// cout << c << endl;
	//cout << (a[0]==b[102]);
	TritSet d = a|b;
	cout << d << endl;
	a[56] = True;
	a.shrink();
	cout << a << " " << a.capacity() << endl;
	a = ~b;
	cout << a << " " << a.capacity() << endl;
	a[5] = 100;
	getchar();
	return 0;
}
