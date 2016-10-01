#ifndef TRIT
#define TRIT
#define MEM_ERR 1
#include <iostream>
#include <cstdlib>
#include <ostream>

using namespace std;

enum Trit{Unknown, False, True};

class Reference{
  unsigned int *mpt;
  size_t pos;
public:
  Reference(unsigned int&, size_t);
  ~Reference();
  void operator=(unsigned int);
  bool operator==(int);
  operator int() const;
};

class TritSet {
  unsigned int real_capa;
  unsigned int user_capa;
  unsigned int *capa;

public:

  TritSet(unsigned int);

  TritSet(TritSet&);

  ~TritSet();

  TritSet& operator!();

  void operator&(TritSet&);
  void operator|(TritSet&);

  Reference operator[](int);

  unsigned int capacity();

  friend ostream& operator <<(ostream &, TritSet&);

};

#endif
