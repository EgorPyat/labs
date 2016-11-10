#ifndef _SURFACE
#define _SURFACE

class Explore{
  Robot* r;
  Map* m;
public:
  Explore(ifstream&, ifstream&);
  void start();
  virtual ~Explore(){};
};

#endif
