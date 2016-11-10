#ifndef _ROBOT
#define _ROBOT
#define _BLOCK -1
#define _IMPMOVE 1

class Robot : public Mapper {
  tuple<int, int> F;
  tuple<int, int> S;
  tuple<int, int> R;
  tuple<int, int> min_aval;
  Map* hidmap;
  bool step();
  int length;
  // void analize(Point);
public:
  Robot(ifstream&, Map&);
  ~Robot(){};
  void explore();
};

#endif
