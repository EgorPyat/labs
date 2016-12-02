#ifndef _SURFACE
#define _SURFACE

template<typename P, typename M> class Explore{
  Robot<P,M>* r;
  Map<P,M>* m;
public:
  Explore(ifstream&, ifstream&, int, int);
  void start();
  virtual ~Explore(){};
};

// template<typename M> class Explore<string, M>{};

template<typename P, typename M> Explore<P, M>::Explore(ifstream& in, ifstream& rm, int limit, int topology){
  this->m = new Map<P,M>(in, topology);
  this->r = new Robot<P,M>(rm, *this->m, limit);
}
template<typename P, typename M> void Explore<P, M>::start(){
  this->r->explore();
}

#endif
