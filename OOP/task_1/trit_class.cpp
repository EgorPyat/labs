#include "header.h"

TritSet::TritSet(unsigned int user_capa){

    int size;
    int real_capa;

    size = 2*user_capa/8/sizeof(unsigned int);
    real_capa = size*sizeof(unsigned int)*8/2;

    this->user_capa = user_capa;

    if(real_capa < user_capa){
      size++;
      real_capa = size*sizeof(unsigned int)*8/2;
    }
    this->real_capa = real_capa;

    this->cont = new unsigned int[size];
    this->trit_value = 0;

}

TritSet::~TritSet(){

  delete[] cont;
  cont = NULL;
  cout << "Bye" << endl;

}

int TritSet::capacity(){

  return real_capa;

}

unsigned int& TritSet::operator[](int n){

  unsigned int capa_index;
  unsigned int big_byte;
  unsigned int big_byte_index;

  if(this->trit_value > 0){

    capa_index = 2*(this->trit_index)/8/sizeof(unsigned int);
    big_byte = this->cont[capa_index];
    big_byte_index = (this->trit_index) - (capa_index*8*sizeof(unsigned int)/2);

    this->cont[capa_index] |= (this->trit_value << ((sizeof(unsigned int)*8 - (big_byte_index + 1)*2)));

  }

  capa_index = 2*n/8/sizeof(unsigned int);
  big_byte = this->cont[capa_index];
  big_byte_index = n - (capa_index*8*sizeof(unsigned int)/2);

  this->trit_value = (this->cont[capa_index] >> (sizeof(unsigned int)*8 - (big_byte_index + 1)*2)) & 0x3;

  this->trit_index = n;

  return this->trit_value;

}
