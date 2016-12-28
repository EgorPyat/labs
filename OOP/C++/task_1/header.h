#ifndef TRIT
#define TRIT
#define MEM_ERR 1
#include <iostream>
#include <cstdlib> //errors
#include <cstring> //memset()
#include <cstdio> //getchar()
#include <unordered_map>
#include <gtest/gtest.h>

using namespace std;

enum Trit { Unknown, False, True };

class TritSet {
	unsigned int real_capa;
	unsigned int user_capa;
	unsigned int *capa;
	unsigned int last_ind;
public:
	class Reference {
		unsigned int *mpt;
		size_t pos;
		TritSet* tr;
	public:
		Reference(TritSet&, unsigned int&, size_t);
		~Reference();
		void operator=(unsigned int);
		void operator=(Reference&);
		bool operator==(int);
		operator int() const;
	};

	class Triterator {
		unsigned int pos;
		TritSet* tr;
	public:
		Triterator(TritSet&, unsigned int);
		~Triterator();
		void operator=(const Triterator&);
		bool operator!=(const Triterator&);
		Triterator& operator++();
		Reference operator*();
	};
	class const_Triterator {
		unsigned int pos;
		const TritSet* tr;
	public:
		const_Triterator(const TritSet&, unsigned int);
		~const_Triterator();
		void operator=(const const_Triterator&);
		bool operator!=(const const_Triterator&);
		const_Triterator& operator++();
		unsigned int operator*();
	};

	TritSet(unsigned int);
	TritSet(const TritSet&);
	~TritSet();
	void operator=(const TritSet&);
	TritSet& flip();
	TritSet operator~() const;
	TritSet operator&(const TritSet&) const;
	TritSet operator|(const TritSet&) const;
	void operator&=(const TritSet&);
	void operator|=(const TritSet&);
	Reference operator[](int);
	unsigned int operator[](int) const;
	unsigned int capacity() const;
	void shrink();
	void trim(size_t);
	size_t length() const;
	size_t cardinality(Trit) const;
	unordered_map<Trit, int, hash<int>> cardinality() const;
	Triterator begin();
	Triterator end();
	const_Triterator begin() const;
	const_Triterator end() const;

	friend ostream& operator <<(ostream &, const TritSet&);
};

#endif
