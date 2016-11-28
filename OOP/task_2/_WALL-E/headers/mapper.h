#ifndef _MAPPER
#define _MAPPER

template<typename P, typename M> class Mapper{
protected:
  vector<vector<M>> map;
  M height;
  M width;
public:
  Mapper(ifstream& im);
  virtual ~Mapper(){};
};

template<typename M> class Mapper<string,M>{
protected:
  list<string> map;
  string start;
  string finish;
public:
  Mapper(ifstream& im);
  virtual ~Mapper(){};
};

template<typename P, typename M> Mapper<P,M>::Mapper(ifstream& in){
  // M size = 0;
  M i;
  M j;
  string line;

  for (i = 0; getline(in, line); i++) {
    this->map.push_back(vector<M>());
    // size += line.size();
    for (j = 0; j <= line.size(); j++) {
      this->map[i].push_back(line[j]);
      cout << (char)this->map[i][j] << ' ' << ' ';
      if(j == line.size()) {
        cout << '\n' << '\n';
      }
    }
  }
  cout << endl;
  this->height = i;
  this->width = j - 1;
}

template<typename M> Mapper<string,M>::Mapper(ifstream& in){
  M i;

  for(i = 0; getline(in, this->finish); i++){
    if(i == 0) this->start = this->finish;
    this->map.push_back(this->finish);
  }
  // map.pop_back();

  this->finish = map.back();
  // cout << this->finish << endl;
  // while(map.size()){
  //   cout << map.front() << endl;
  //   map.pop_front();
  // }
  // cout << this->start << " "<< this->finish << endl;
}

#endif
