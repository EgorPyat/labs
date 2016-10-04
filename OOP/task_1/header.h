#ifndef TRIT
#define TRIT
#define MEM_ERR 1
#include <iostream>
#include <cstdlib> //errors
#include <ostream> //output overload
#include <cstring> //memset()
#include <cstdio> //getchar()

using namespace std;

enum Trit { Unknown, False, True };

class TritSet {
	unsigned int real_capa;
	unsigned int user_capa;
	unsigned int *capa;
	unsigned int last_val;
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

	//освобождение памяти до начального значения или
	//до значения необходимого для хранения последнего установленного трита
	void shrink();

	// забыть содержимое от lastIndex и дальше
	void trim(size_t);
	// logical length - индекс последнего не Unknown трита +1
	size_t length();

	//число установленных в данное значение тритов
	//для трита Unknown - число значение Unknown до последнего установленного трита
	size_t cardinality(Trit);

	//аналогично но сразу для всех типов тритов
	// unordered_map< Trit, int, std::hash<int> > cardinality();
	friend ostream& operator <<(ostream &, TritSet&);
};

#endif
