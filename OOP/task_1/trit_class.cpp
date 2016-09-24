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

}

TritSet::~TritSet(){

  delete[] cont;
  cont = NULL;
  cout << "Bye" << endl;

}

int TritSet::capacity(){

  return this->real_capa;

}

unsigned int TritSet::operator[](int n){

  

}
