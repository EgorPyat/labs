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

  this->capa = new unsigned int[V];
}

TritSet::~TritSet(){

  delete[] capa;
  capa = NULL;

}

unsigned int TritSet::capacity(){

  return this->user_capa;

}

TritSet::Reference TritSet::operator[](int n){

  size_t ind = 2*n/8/sizeof(unsigned int);
  size_t b_ind = n - ind*8*sizeof(unsigned int)/2;

  return Reference(this->capa[ind], b_ind);
}

// unsigned int TritSet::operator[](double n){
//
//   size_t ind = 2*n/8/sizeof(unsigned int);
//   size_t b_ind = n - ind*8*sizeof(unsigned int)/2;
//   unsigned int k = ((sizeof(unsigned int)*8 - (b_ind + 1)*2));
//
//   return (this->capa[ind] >> ((sizeof(unsigned int)*8 - (b_ind + 1)*2))) & 0x3;
// }

TritSet::Reference::Reference(unsigned int& a, size_t p){
  this->mpt = &a;
  this->pos = p;
  // cout << "pos " << this->pos << endl;
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
// void *TritSet::Reference::operator bool() const{
//   cout << (*this->mpt >> (sizeof(unsigned int)*8 - ((this->pos) + 1)*2)) << endl;
//   cout << this->pos << endl;
//   cout << *this->mpt << endl;
// }
