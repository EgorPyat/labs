#ifndef _ROBOT
#define _ROBOT
#define _BLOCK -1
#define _IMPMOVE 1

class Robot : public Mapper {
  tuple<int, int> F;
  tuple<int, int> S;
  tuple<int, int> prev;
  Map* hidmap;
  char tunnel; // 2 - робот пришел не из туннеля, 1 - робот пришел из тунеля, 0 - робот пришел из тупика
  int nook;
  bool step();
public:
  Robot(ifstream&, Map&);
  ~Robot(){};
  void explore();
};

#endif
