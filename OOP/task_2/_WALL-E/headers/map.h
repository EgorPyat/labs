#ifndef _MAP
#define _MAP

class Map /*: public Surface*/{
  vector<vector<unsigned char>> map;
  // tuple<int, int> S;
  int height;
  int width;
public:
  Map(){};
  Map(ifstream&);
  ~Map(){};
  unordered_map<int, int, hash<int>> respond(Point);
  // virtual uint move(Point) throw(BadMove);
  // virtual vector<tuple<Point, uint>> lookup();
};

#endif
