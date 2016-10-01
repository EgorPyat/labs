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
  this->capa = new unsigned int[size];
}

TritSet::TritSet(TritSet& th){
  int i;
  int size;
  cout << "Copy" << endl;
  cout << "th " << th.real_capa << endl;
  real_capa = th.real_capa;
  user_capa = th.user_capa;

  size = 2*real_capa/8/sizeof(unsigned int);

  capa = new unsigned int[size];

  for(i = 0; i< size; i++){
    capa[i] = th.capa[i];
  }
}

TritSet::~TritSet(){

  delete[] capa;
  capa = NULL;

}

unsigned int TritSet::capacity(){

  return this->user_capa;

}

Reference TritSet::operator[](int n){
  if(n >= this->user_capa){
    cout << "Out of limits!" << endl;
    exit(MEM_ERR);
  }

  size_t ind = 2*n/8/sizeof(unsigned int);
  size_t b_ind = n - ind*8*sizeof(unsigned int)/2;

  return Reference(this->capa[ind], b_ind);
}

bool Reference::operator==(int n){
  int x = (*this->mpt >> (sizeof(unsigned int)*8 - ((this->pos) + 1)*2)) & 0x3;

  if (x == n){
    return 1;
  }else{
    return 0;
  }
}

void TritSet::operator&(TritSet& a){
  cout << "ds" << endl;
}

void TritSet::operator|(TritSet& b){
  cout << "dsd" << endl;
}

TritSet& TritSet::flip(){
  unsigned int i;
  unsigned int j;
  unsigned int s;
  unsigned int num;
  unsigned int diff;
  unsigned int size;
  size = 2*(this->real_capa)/sizeof(unsigned int)/8;
  if(this->real_capa > this->user_capa){
    size--;
  }
  for (i = 0; i < size; i++){
    for (j = 0; j < (sizeof(unsigned int) * 8)/2; j++){
      num = ((this->capa[i] >> ((sizeof(unsigned int)*8 - ((j) + 1)*2))) & 0x3);
      s = i*8*sizeof(unsigned int)/2 + j;

      // cout << s << endl;

      if(num == 2){
        // re = this->operator[](s);
        // re = 1;
        this->operator[](s) = 1;
        // re = this->operator[](s);
      }else if(num == 1){
        this->operator[](s) = 2;
      }
    }
  }

  diff = this->user_capa - size*sizeof(unsigned int)*8/2;
  if(diff > 0) {
    for (i = 0; i < diff; i++){
      num = ((this->capa[size] >> ((sizeof(unsigned int)*8 - ((i) + 1)*2))) & 0x3);

      s = size*sizeof(unsigned int)*8/2 + i;
      // cout << s << endl;
      if(num == 2){
        this->operator[](s) = 1;
      }else if(num == 1){
        this->operator[](s) = 2;
      }
    }
  }
  return *this;
}

TritSet& TritSet::operator~(){
  // TritSet u(*this);
  // u.flip();
  cout << *this << endl;
  // cout << TritSet(*this).flip() << endl;
  return TritSet(*this).flip();
}

Reference::Reference(unsigned int& a, size_t p){
  this->mpt = &a;
  this->pos = p;
}
Reference::~Reference(){};
// TritSet::Reference::Reference(Reference& th){
//
//   mpt = th.mpt;
//   pos = th.pos;
//
// }

void Reference::operator=(unsigned int x){
  unsigned int s = 0;
  unsigned int t = 0;
  s = ~s;
// cout << "s " << s << endl;
  if (x > 2) {
    cout << "Can't write! " << endl;
    return;
  }
  unsigned int l = (8*sizeof(unsigned int) - (((this->pos)+1)*2));
  s <<= l;
  s <<= 2;
  t = ~s;
  t >>= 2;
  s |= t;
  // cout << "s " << s << endl;
  *this->mpt &= s;
  *this->mpt |= x << l;
}

Reference::operator int() const{

  return (*this->mpt >> (sizeof(unsigned int)*8 - ((this->pos) + 1)*2)) & 0x3;

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
  // cout << "size " << size << endl;
  for (i = 0; i < size; i++){
    for (j = 0; j < (sizeof(unsigned int) * 8)/2; j++){
      os << ((c.capa[i] >> ((sizeof(unsigned int)*8 - ((j) + 1)*2))) & 0x3);
    }
  }

  diff = c.user_capa - size*sizeof(unsigned int)*8/2;
  if(diff > 0) {
    for (i = 0; i < diff; i++){
      os << ((c.capa[size] >> ((sizeof(unsigned int)*8 - ((i) + 1)*2))) & 0x3);
    }
  }

  return os;

}
