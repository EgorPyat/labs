#ifndef _MAP
#define _MAP

class Map : public Mapper, public Surface{
public:
  Map(ifstream& in) : Mapper(in){};
  ~Map(){};
  unordered_map<int, int, hash<int>> respond(Point);
};

#endif
