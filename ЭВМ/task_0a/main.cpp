#include "m_1.h"
#include "m_2.h"
#include "m_3.h"
#include <iostream>
using namespace std;
using namespace Module1;
using namespace Module2;
using namespace Module3;
int main (int argc, char** argv){

    cout <<  "Hello world!" << "\n";
    cout << getMyName1() << "\n";
    cout << getMyName2() << "\n";
    cout << getMyName3() << "\n";

    cout << getMyName1() << "\n"; // (A)
    cout << getMyName2() << "\n";
    cout << getMyName3() << "\n";

    //using namespace Module2; // (B)
    //cout << getMyName() << "\n"; // COMPILATION ERROR (C)

    //using Module2::getMyName;
    //cout << getMyName() << "\n"; // (D)

    return 0;
}
