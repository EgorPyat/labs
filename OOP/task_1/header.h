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
	void shrink();

	// çàáûòü ñîäåðæèìîå îò lastIndex è äàëüøå
	void trim(size_t);
	// logical length - èíäåêñ ïîñëåäíåãî íå Unknown òðèòà +1
	size_t length();

	//÷èñëî óñòàíîâëåííûõ â äàííîå çíà÷åíèå òðèòîâ
	//äëÿ òðèòà Unknown - ÷èñëî çíà÷åíèå Unknown äî ïîñëåäíåãî óñòàíîâëåííîãî òðèòà
	size_t cardinality(Trit);

	//àíàëîãè÷íî íî ñðàçó äëÿ âñåõ òèïîâ òðèòîâ
	// unordered_map< Trit, int, std::hash<int> > cardinality();
	friend ostream& operator <<(ostream &, TritSet&);
};

#endif
