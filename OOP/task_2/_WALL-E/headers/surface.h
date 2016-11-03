#ifndef _SURFACE
#define _SURFACE

class BadMove : public std::exception{};

struct Point{
  uint x;
  uint y;
};

class Surface{
public:
  /*Возвращает окружение точки, где находится робот*/
  virtual unordered_map<int, int, hash<int>> respond(Point) = 0;
};

#endif
