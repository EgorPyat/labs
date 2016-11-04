#ifndef _ROBOT
#define _ROBOT
#define _BLOCK -1
#define _IMPMOVE 1

class Robot : public Mapper {
  tuple<int, int> F;
  tuple<int, int> S;
  Map* hidmap;
public:
  Robot(ifstream&, Map&);
  ~Robot(){};
  void step();
};

#endif
