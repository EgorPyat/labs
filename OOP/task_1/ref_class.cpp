#include "header.h"

TritSet::Reference::Reference(TritSet& b, unsigned int& a, size_t p) {
	this->mpt = &a;
	this->pos = p;
	this->tr = &b;
}

TritSet::Reference::~Reference() {};

void TritSet::Reference::operator=(unsigned int x) {
	unsigned int size;
	unsigned int size_1;
	unsigned int real_capa;
	unsigned int user_capa;

	user_capa = this->tr->user_capa;
	size = 2*user_capa/8/sizeof(unsigned int);
	real_capa = size*sizeof(unsigned int)*8/2;

	if (real_capa < user_capa) {
		size++;
		real_capa = size*sizeof(unsigned int) * 8/2;
	}
	size_1 = size;
	if ((this->tr->real_capa >= user_capa) && (this->tr->real_capa < real_capa)) {
		if (x == Unknown) {
			this->tr->real_capa = real_capa;
			return;
		}
		else {
			this->tr->last_ind = this->tr->real_capa;
			this->tr->user_capa = this->tr->real_capa + 1;
			cout << this->tr->user_capa << endl;
		}
	}
	else if (((this->tr->real_capa + 1) > real_capa) && ((this->mpt) >= &this->tr->capa[size])) {
		if (x == Unknown) {
			this->tr->real_capa = real_capa;
			return;
		}
		else if(x != Unknown){
      unsigned int size;
      unsigned int r_c;

			this->tr->last_ind = this->tr->real_capa;
      this->tr->user_capa = this->tr->real_capa + 1;
      size = (this->tr->user_capa)*2/8/sizeof(unsigned int);
      r_c = size*8*sizeof(unsigned int)/2;
      if(r_c < (this->tr->user_capa)){
        size++;
        r_c = size*8*sizeof(unsigned int)/2;
      }
      this->tr->real_capa = r_c;
			unsigned int* cp = new unsigned int[size];
			memset(cp, 0, 2 * r_c / 8);
			memcpy(cp, this->tr->capa, size_1*sizeof(unsigned int));
			delete[] this->tr->capa;
			this->tr->capa = cp;
      size--;
      this->mpt = &(this->tr->capa[size]);
      unsigned int s = 0;
    	unsigned int t = 0;
    	s = ~s;
    	if (x > 2) {
    		cout << "Can't write! " << endl;
    		return;
    	}
    	unsigned int l = (8 * sizeof(unsigned int) - (((this->pos) + 1) * 2));
    	s <<= l;
    	s <<= 2;
    	t = ~s;
    	t >>= 2;
    	s |= t;
    	*this->mpt &= s;
    	*this->mpt |= x << l;
      this->tr->capa[size] = *this->mpt;
			return;
		}
	}

	this->tr->real_capa = real_capa;

	unsigned int s = 0;
	unsigned int t = 0;
	s = ~s;
	if (x > 2) {
		cout << "Can't write! " << endl;
		return;
	}
	unsigned int l = (8 * sizeof(unsigned int) - (((this->pos) + 1) * 2));
	s <<= l;
	s <<= 2;
	t = ~s;
	t >>= 2;
	s |= t;
	*this->mpt &= s;
	*this->mpt |= x << l;
}

void TritSet::Reference::operator=(Reference& rf) {
	unsigned int t;
	t = rf;
	*this = t;
}

TritSet::Reference::operator int() const {

	return (*this->mpt >> (sizeof(unsigned int) * 8 - ((this->pos) + 1) * 2)) & 0x3;
}

bool TritSet::Reference::operator==(int n) {
	int x = (*this->mpt >> (sizeof(unsigned int) * 8 - ((this->pos) + 1) * 2)) & 0x3;

	if (x == n) {
		return 1;
	}
	else {
		return 0;
	}
}
