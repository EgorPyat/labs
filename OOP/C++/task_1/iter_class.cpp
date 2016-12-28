#include "header.h"

TritSet::Triterator::Triterator(TritSet& a, unsigned int pos){
  this->tr = &a;
  this->pos = pos;
}

TritSet::Triterator::~Triterator(){}

void TritSet::Triterator::operator=(const Triterator& a){}

bool TritSet::Triterator::operator!=(const Triterator& a){
  if (this->pos == a.pos){
    return 0;
  }
  return 1;
}

TritSet::Reference TritSet::Triterator::operator*(){
  size_t ind = 2 * (this->pos) / 8 / sizeof(unsigned int);
  size_t b_ind = (this->pos) - ind * 8 * sizeof(unsigned int) / 2;
  return Reference(*(this->tr), this->tr->capa[ind], b_ind);
}

TritSet::Triterator& TritSet::Triterator::operator++(){
  (this->pos)++;
  return *this;
}

TritSet::const_Triterator::const_Triterator(const TritSet& a, unsigned int pos){
  this->tr = &a;
  this->pos = pos;
}

TritSet::const_Triterator::~const_Triterator(){}

void TritSet::const_Triterator::operator=(const const_Triterator& a){}

bool TritSet::const_Triterator::operator!=(const const_Triterator& a){
  if (this->pos == a.pos){
    return 0;
  }
  return 1;
}

unsigned int TritSet::const_Triterator::operator*(){
  return this->tr->operator[](this->pos);
}

TritSet::const_Triterator& TritSet::const_Triterator::operator++(){
  (this->pos)++;
  return *this;
}
