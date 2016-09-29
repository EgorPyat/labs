#ifndef TRIT
#define TRIT
#define MEM_ERR 1
#include <iostream>
#include <cstdlib>

using namespace std;

enum Trit{Unknown, False, True};


class TritSet {
  unsigned int real_capa;
  unsigned int user_capa;
  unsigned int *capa;

public:
  class Reference{
    unsigned int *mpt;
    size_t pos;
  public:
    Reference(unsigned int&, size_t);
    void operator=(unsigned int);
    // operator bool() const;
    operator int() const;
  };

  TritSet(unsigned int);

  ~TritSet();

  Reference operator[](int);
  unsigned int operator[](double);

  unsigned int capacity();
};

#endif
