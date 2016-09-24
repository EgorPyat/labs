#ifndef TRIT
#define TRIT
#define MEM_ERR 1
#include <iostream>
#include <cstdlib>

using namespace std;

class TritSet {

  int user_capa;
  int real_capa;
  unsigned int *cont;

  public:
    unsigned int operator[](int);
    int capacity();
    TritSet(unsigned int);
    ~TritSet();

  //shrink();

};

#endif
