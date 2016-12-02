#ifndef _MAP
#define _MAP

template<typename P, typename M> class Map : public Mapper<P,M>{
  int topology;
public:
  Map(ifstream& in, int topology) : Mapper<P,M>(in){
    this->topology = topology;
  };
  ~Map(){};
  unordered_map<M, M, hash<M>> respond(P);
};

template<typename M> class Map<string, M> : public Mapper<string, M>{
public:
  Map(ifstream& in, int topology) : Mapper<string,M>(in){};
  ~Map(){};
  unordered_map<M, string, hash<M>> respond(string);
};

template<typename M> unordered_map<M,string, hash<M>> Map<string,M>::respond(string p){
  unordered_map<M,string, hash<M>> m = {};
  M i;
  M j = 0;
  list<string>::iterator r;
  list<string>::iterator l;
  for(l = this->map.begin(), r = this->map.end(); l != r; l++){
      if(edit_distance(*l, p) == 1){
      m.emplace(j, *l);
      j++;
    }
  }
  return m;
}

template<typename P, typename M> unordered_map<M,M, hash<M>> Map<P,M>::respond(P p){
  M up = -1;
  M right = -1;
  M left = -1;
  M down = -1;
  M center = -1;
  unordered_map<M,M, hash<M>> m = {};
  center = this->map[p.x][p.y];

  if(this->topology == 0){
    if (p.x != 0) up = this->map[p.x - 1][p.y];
    if (p.y != (this->width - 1)) right = this->map[p.x][p.y + 1];
    if (p.y != 0) left = this->map[p.x][p.y - 1];
    if (p.x != (this->height - 1)) down = this->map[p.x + 1][p.y];
  }
  else if(this->topology == 1){
    if (p.x != 0) up = this->map[p.x - 1][p.y];
    if (p.y != (this->width - 1)) right = this->map[p.x][p.y + 1];
    else right = this->map[p.x][0];
    if (p.y != 0) left = this->map[p.x][p.y - 1];
    else left = this->map[p.x][this->width - 1];
    if (p.x != (this->height - 1)) down = this->map[p.x + 1][p.y];
  }
  else if(this->topology == 2){
    if (p.x != 0) up = this->map[p.x - 1][p.y];
    else up = this->map[this->height - 1][p.y];
    if (p.y != (this->width - 1)) right = this->map[p.x][p.y + 1];
    else right = this->map[p.x][0];
    if (p.y != 0) left = this->map[p.x][p.y - 1];
    else left = this->map[p.x][this->width - 1];
    if (p.x != (this->height - 1)) down = this->map[p.x + 1][p.y];
    else down = this->map[0][p.y];
  }
  m = {
    {0, center},
    {1, up},
    {2, right},
    {3, left},
    {4, down}
  };

  return m;
}

#endif
