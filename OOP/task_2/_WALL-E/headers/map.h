#ifndef _MAP
#define _MAP

template<typename P, typename M> class Map : public Mapper<P,M>{
  int topology;
public:
  Map(ifstream& in, int topology) : Mapper<P,M>(in){
    this->topology = topology;
  };
  Map(int topology){
    this->height = 0;
    this->width = 0;
    this->topology = topology;
  };
  void write(istream& is){
    M i;
    M j;
    string line;
    if(!this->map.empty()) {
      cout << "Map configuration is already uploaded!" << endl;
      return;
    }
    is.clear();
    is.seekg(0);
    for (i = 0; getline(is, line); i++) {
      this->map.push_back(vector<M>());
      for (j = 0; j <= line.size(); j++) {
        if(line[j] == 'R' || line[j] == 'F'){
          this->map[i].push_back('.');
        }
        else this->map[i].push_back(line[j]);
        cout << (char)this->map[i][j] << ' ' << ' ';
        if(j == line.size()) {
          cout << '\n' << '\n';
        }
      }
    }
    cout << endl;
    this->height = i;
    this->width = j - 1;
    cout << this->height << ' ' << this->width << endl;

    cout << "Map is done!" << endl;
  };
  ~Map(){};
  unordered_map<M, M, hash<M>> respond(P);
};

template<typename M> class Map<string, M> : public Mapper<string, M>{
public:
  Map(ifstream& in, int topology) : Mapper<string,M>(in){};
  Map(int topology){
    this->start = "";
    this->finish = "";
  };
  void write(istream& is){
    if(!this->map.empty()){
      cout << "Map configuration is already uploaded!" << endl;
      return;
    }
    is.clear();
    is.seekg(0);
    M i;
    for(i = 0; getline(is, this->finish); i++){
      if(i == 0) this->start = this->finish;
      this->map.push_back(this->finish);
    }
    this->finish = this->map.back();
  };
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
