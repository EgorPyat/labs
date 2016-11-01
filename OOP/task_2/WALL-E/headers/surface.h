#ifndef _SURFACE
#define _SURFACE

class BadMove : public std::exception{};

struct Point{
  uint x;
  uint y;
};

class  Surface{
public:
  /* Продвижение в новую соседнюю точку и возвращает расстояние до цели в новой точке */
  virtual uint move(Point point) throw (BadMove) = 0;
  /* Расстояние до цели для соседних  точек  в которые возможно перемещение */
  virtual std::vector<tuple<Point, uint>> analyze() = 0;
};

#endif
