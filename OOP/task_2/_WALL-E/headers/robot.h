#ifndef _ROBOT
#define _ROBOT

class Robot : public Surface {
  vector<vector<int>> map;
  int height;
  int width;
  tuple<int, int> F;
  tuple<int, int> S;
  Map* hidmap;
public:
  Robot(Map&, ifstream&);
  ~Robot(){};
  virtual uint move(Point) throw(BadMove);
  // virtual void lookup();
  void step();
};

#endif
