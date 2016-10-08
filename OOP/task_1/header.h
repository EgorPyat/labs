#ifndef TRIT
#define TRIT
#define MEM_ERR 1
#include <iostream>
#include <cstdlib> //errors
#include <ostream> //output overload
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

	TritSet(unsigned int);
	TritSet(const TritSet&);
	~TritSet();
	void operator=(const TritSet&);
	TritSet& flip();
	TritSet operator~();
	TritSet operator&(TritSet&);
	TritSet operator|(TritSet&);
	Reference operator[](int);
	unsigned int capacity();
	void shrink();
	void trim(size_t);
	size_t length();
	size_t cardinality(Trit);
	unordered_map<Trit, int, hash<int>> cardinality();

	friend ostream& operator <<(ostream &, TritSet&);
};

#endif
