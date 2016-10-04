#include "header.h"

TritSet::Reference::Reference(TritSet& b, unsigned int& a, size_t p) {
	this->mpt = &a;
	this->pos = p;
	this->tr = &b;
}

TritSet::Reference::~Reference() {};

/*Work here*/
void TritSet::Reference::operator=(unsigned int x) {
	int size;
	int real_capa;

	size = 2 * this->tr->user_capa / 8 / sizeof(unsigned int);
	real_capa = size*sizeof(unsigned int) * 8 / 2;

	if (real_capa < this->tr->user_capa) {
		size++;
		real_capa = size*sizeof(unsigned int) * 8 / 2;
	}
	if ((this->tr->real_capa >= this->tr->user_capa) && (this->tr->real_capa != real_capa)){
		cout << this->tr->real_capa << endl;
		if (this->tr->real_capa > real_capa){
			cout << "hi" << endl;
			this->tr->real_capa = real_capa;
			return;
		}else {
			// if (this->tr->user_capa < (this->tr->real_capa + 1)){
			//
			// 	this->tr->user_capa = this->tr->real_capa + 1;
			// }
			this->tr->real_capa = real_capa;
		}
	}
	//Организовать выделение памяти

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
