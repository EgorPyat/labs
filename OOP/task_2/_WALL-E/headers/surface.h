#ifndef _SURFACE
#define _SURFACE

template<typename P, typename M> class Explore{
  Robot<P,M>* r;
  Map<P,M>* m;
public:
  Explore(ifstream&, ifstream&);
  void start();
  virtual ~Explore(){};
};

// template<typename M> class Explore<string, M>{};

template<typename P, typename M> Explore<P, M>::Explore(ifstream& in, ifstream& rm){
  this->m = new Map<P,M>(in);
  this->r = new Robot<P,M>(rm, *this->m);
}
template<typename P, typename M> void Explore<P, M>::start(){
  this->r->explore();
}

#endif
