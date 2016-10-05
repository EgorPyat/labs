#include "header.h"

TritSet::Reference::Reference(TritSet& b, unsigned int& a, size_t p) {
	this->mpt = &a;
	this->pos = p;
	this->tr = &b;
}

TritSet::Reference::~Reference() {};

/*Work here*/
void TritSet::Reference::operator=(unsigned int x) {
	unsigned int size;
	unsigned int real_capa;
	unsigned int user_capa;
	cout << this->tr->real_capa << endl;
	user_capa = this->tr->user_capa;
	
	size = 2 * user_capa / 8 / sizeof(unsigned int);
	real_capa = size*sizeof(unsigned int) * 8 / 2;

	if (real_capa < user_capa) {
		size++;
		real_capa = size*sizeof(unsigned int) * 8 / 2;// по user_capa восстанавливается прошлая real_capa
	}
	cout << "u: " << user_capa << endl;
	if ((this->tr->real_capa >= user_capa) && (this->tr->real_capa < real_capa)) {
		cout << "x: " << x << endl;
		if (x == Unknown) {
			this->tr->real_capa = real_capa;
			return;
		}
		else {
			this->tr->user_capa = this->tr->real_capa + 1;
			cout << this->tr->user_capa << endl;
		}
	}
	else if (((this->tr->real_capa + 1) > real_capa) && ((this->mpt) >= &this->tr->capa[size])) {
		if (x == Unknown) {
			cout << "no" << endl;
			cout << real_capa << endl;
			cout << this->tr->real_capa << endl;
			this->tr->real_capa = real_capa;
			return;
		}
		else if(x != Unknown){
			cout << "yes" << endl;
			this->tr->real_capa = real_capa;
			return;
		}
	}

	this->tr->real_capa = real_capa;
	/*if((this->tr->real_capa) < (user_capa)){
		cout << "m" << endl;
		(this->tr->real_capa) = real_capa;
	}*/
	//else if (this->tr->real_capa > real_capa) {
	//	cout << (this->tr->real_capa) << "dds" << endl;
	//	if ((this->tr->real_capa) > real_capa) {
	//		cout << "hi" << endl;
	//		(this->tr->real_capa) = real_capa;
	//		return;
	//	}
	//}

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