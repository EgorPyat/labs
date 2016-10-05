#include "header.h"

TritSet::TritSet(unsigned int V) {
	int size;
	int real_capa;

	size = 2 * V / 8 / sizeof(unsigned int);
	real_capa = size*sizeof(unsigned int) * 8 / 2;

	this->user_capa = V;

	if (real_capa < V) {
		size++;
		real_capa = size*sizeof(unsigned int) * 8 / 2;
	}
	this->real_capa = real_capa;
	// this->capa = new unsigned int[size];
	this->capa = (unsigned int*)malloc(size*sizeof(unsigned int));
	memset(this->capa, 0, 2 * real_capa / 8);
}

TritSet::TritSet(const TritSet& th) {
	int i;
	int size;

	real_capa = th.real_capa;
	user_capa = th.user_capa;

	size = 2 * real_capa / 8 / sizeof(unsigned int);

	capa = new unsigned int[size];

	for (i = 0; i< size; i++) {
		capa[i] = th.capa[i];
	}
}

TritSet::~TritSet() {

	//delete[] capa;
	free(capa);
	capa = NULL;
}

unsigned int TritSet::capacity() {

	return this->user_capa;
}

TritSet::Reference TritSet::operator[](int n) {
	if ((n >= this->user_capa) && (n < this->real_capa)) {
		cout << "n: " << n << endl;
		this->real_capa = n;
		cout << "r: " << this->real_capa << endl;
	}
	else if (n >= this->real_capa) {
		cout << "something" << endl;
		cout << "n: " << n << endl;
		this->real_capa = n;
		size_t ind = 2 * n / 8 / sizeof(unsigned int); 
		
		size_t b_ind = n - ind * 8 * sizeof(unsigned int) / 2;
		return Reference(*this, this->capa[ind], b_ind);
	}

	size_t ind = 2 * n / 8 / sizeof(unsigned int);
	size_t b_ind = n - ind * 8 * sizeof(unsigned int) / 2;
	return Reference(*this, this->capa[ind], b_ind);
}

void TritSet::operator=(const TritSet& th) {
	int i;
	int size;
	this->real_capa = th.real_capa;
	this->user_capa = th.user_capa;

	size = 2 * real_capa / 8 / sizeof(unsigned int);

	//this->capa = new unsigned int[size];
	this->capa = (unsigned int*)malloc(size*sizeof(unsigned int));
	for (i = 0; i< size; i++) {
		capa[i] = th.capa[i];
	}
}

TritSet& TritSet::flip() {
	unsigned int i;
	unsigned int j;
	unsigned int s;
	unsigned int num;
	unsigned int diff;
	unsigned int size;
	size = 2 * (this->real_capa) / sizeof(unsigned int) / 8;
	if (this->real_capa > this->user_capa) {
		size--;
	}
	for (i = 0; i < size; i++) {
		for (j = 0; j < (sizeof(unsigned int) * 8) / 2; j++) {
			num = ((this->capa[i] >> ((sizeof(unsigned int) * 8 - ((j)+1) * 2))) & 0x3);
			s = i * 8 * sizeof(unsigned int) / 2 + j;

			if (num == 2) {
				this->operator[](s) = 1;
			}
			else if (num == 1) {
				this->operator[](s) = 2;
			}
		}
	}

	diff = this->user_capa - size*sizeof(unsigned int) * 8 / 2;
	if (diff > 0) {
		for (i = 0; i < diff; i++) {
			num = ((this->capa[size] >> ((sizeof(unsigned int) * 8 - ((i)+1) * 2))) & 0x3);

			s = size*sizeof(unsigned int) * 8 / 2 + i;
			if (num == 2) {
				this->operator[](s) = 1;
			}
			else if (num == 1) {
				this->operator[](s) = 2;
			}
		}
	}

	return *this;
}

TritSet TritSet::operator~() {

	return TritSet(*this).flip();
}

TritSet TritSet::operator&(TritSet& a) {
	unsigned int i;
	TritSet b = *this;
	for (i = 0; i < this->user_capa; i++) {

		if ((b[i] == False) || (a[i] == False)) {
			b[i] = False;
		}
		else if ((b[i] == True) && (a[i] == True)) {
			continue;
		}
		else if ((b[i] == Unknown) && (a[i] == Unknown)) {
			continue;
		}
		else if ((b[i] == Unknown) && (a[i] == True)) {
			continue;
		}
		else if ((b[i] == True) && (a[i] == Unknown)) {
			b[i] = Unknown;
		}
	}

	return b;
}

TritSet TritSet::operator|(TritSet& a) {
	unsigned int i;
	TritSet b = *this;
	for (i = 0; i < this->user_capa; i++) {

		if ((b[i] == False) && (a[i] == False)) {
			continue;
		}
		else if ((b[i] == True) || (a[i] == True)) {
			b[i] = True;
		}
		else if ((b[i] == Unknown) && (a[i] == Unknown)) {
			continue;
		}
		else if ((b[i] == Unknown) && (a[i] == False)) {
			continue;
		}
		else if ((b[i] == False) && (a[i] == Unknown)) {
			b[i] = Unknown;
		}
	}

	return b;
}

ostream& operator <<(ostream &os, TritSet& c) {
	unsigned int size;
	unsigned int i;
	unsigned int j;
	unsigned int diff;

	size = 2 * (c.real_capa) / sizeof(unsigned int) / 8;
	if (c.real_capa > c.user_capa) {
		size--;
	}
	for (i = 0; i < size; i++) {
		for (j = 0; j < (sizeof(unsigned int) * 8) / 2; j++) {
			os << ((c.capa[i] >> ((sizeof(unsigned int) * 8 - ((j)+1) * 2))) & 0x3);
		}
	}

	diff = c.user_capa - size*sizeof(unsigned int) * 8 / 2;
	if (diff > 0) {
		for (i = 0; i < diff; i++) {
			os << ((c.capa[size] >> ((sizeof(unsigned int) * 8 - ((i)+1) * 2))) & 0x3);
		}
	}

	return os;
}