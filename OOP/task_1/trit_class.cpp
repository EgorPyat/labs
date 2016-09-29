#include "header.h"
TritSet::TritSet(unsigned int V){
  int size;
  int real_capa;

  size = 2*V/8/sizeof(unsigned int);
  real_capa = size*sizeof(unsigned int)*8/2;

  this->user_capa = V;

  if(real_capa < V){
    size++;
    real_capa = size*sizeof(unsigned int)*8/2;
  }
  this->real_capa = real_capa;
  cout << size << endl;
  this->capa = new unsigned int[size];
}

TritSet::~TritSet(){

  delete[] capa;
  capa = NULL;

}

unsigned int TritSet::capacity(){

  return this->user_capa;

}

TritSet::Reference TritSet::operator[](int n){
  if(n > this->user_capa){
    cout << "Out of limits!" << endl;
    exit(MEM_ERR);
  }

  size_t ind = 2*n/8/sizeof(unsigned int);
  size_t b_ind = n - ind*8*sizeof(unsigned int)/2;

  return Reference(this->capa[ind], b_ind);
}

TritSet::Reference::Reference(unsigned int& a, size_t p){
  this->mpt = &a;
  this->pos = p;
}

void TritSet::Reference::operator=(unsigned int x){
  if (x > 2) {
    cout << "Can't write! " << endl;
    return;
  }
  unsigned int l = (8*sizeof(unsigned int) - (((this->pos)+1)*2));
  *this->mpt |= x << l;
}

TritSet::Reference::operator int() const{

  return (*this->mpt >> (sizeof(unsigned int)*8 - ((this->pos) + 1)*2));

}

ostream& operator <<(ostream &os, TritSet& c){

  unsigned int size;
  unsigned int i;
  unsigned int j;
  unsigned int diff;

  size = 2*(c.real_capa)/sizeof(unsigned int)/8;
  if(c.real_capa > c.user_capa){
    size--;
  }
  cout << "size " << size << endl;
  for (i = 0; i < size; i++){
    for (j = 0; j < (sizeof(unsigned int) * 8)/2; j++){
      os << ((c.capa[i] >> ((sizeof(unsigned int)*8 - ((j) + 1)*2))) & 0x3);
    }
  }

  diff = c.real_capa - c.user_capa;

  if(diff > 0) {
    size++;
    for (i = 0; i < diff; i++){
      os << ((c.capa[size] >> ((sizeof(unsigned int)*8 - ((i) + 1)*2))) & 0x3);
    }
  }

  return os;

}
