#ifndef _ROBOT
#define _ROBOT

class Robot : public Mapper {
  tuple<int, int> F;
  tuple<int, int> S;
  Map* hidmap;
public:
  Robot(ifstream&, Map&);
  ~Robot(){};
  // virtual uint move(Point) throw(BadMove);
  // virtual void lookup();
  void step();
};

#endif
