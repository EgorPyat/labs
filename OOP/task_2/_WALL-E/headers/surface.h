#ifndef _SURFACE
#define _SURFACE

template<typename P, typename M> class Explore{
  Robot<P,M>* r;
  Map<P,M>* m;
public:
  Explore(ifstream&, ifstream&, int, int);
  Explore(int limit, int topology){
    this->m = new Map<P,M>(topology);
    this->r = new Robot<P,M>(*this->m, limit);
  }
  void start();
  virtual ~Explore(){};
  friend ostream& operator <<(ostream &os, const Explore<P,M>& xe){
    xe.r->print(os);
  };
  friend istream& operator >>(istream &is, const Explore<P,M>& xe){
    xe.m->write(is);
    xe.r->write(is);
  };
};

template<typename P, typename M> Explore<P, M>::Explore(ifstream& in, ifstream& rm, int limit, int topology){
  this->m = new Map<P,M>(in, topology);
  this->r = new Robot<P,M>(rm, *this->m, limit);
}
template<typename P, typename M> void Explore<P, M>::start(){
  this->r->explore();
}

#endif
