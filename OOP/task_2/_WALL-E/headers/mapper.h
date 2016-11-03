#ifndef _MAPPER
#define _MAPPER

class Mapper{
protected:
  vector<vector<int>> map;
  int height;
  int width;
public:
  Mapper(ifstream& im);
  virtual ~Mapper(){};
};

#endif
