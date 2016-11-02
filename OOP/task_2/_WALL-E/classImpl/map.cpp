#include "../headers/node.h"

Map::Map(ifstream& in){
  int size = 0;
  int i;
  int j;
  string line;
  for (i = 0; getline(in, line); i++) {
    this->map.push_back(vector<unsigned char> ());
    size += line.size();
    for (j = 0; j <= line.size(); j++) {
      this->map[i].push_back(line[j]);
      cout << map[i][j] << " ";
      if(j == line.size()) {
        cout << '\n' << '\n';
      }
    }
  }
  cout << endl;
  this->height = i;
  this->width = j - 1;
}

// uint Map::move(Point point) throw(BadMove){
//   return 0;
// }
//
// virtual vector<tuple<Point, uint>> Map::lookup(){
//   vector<tuple<Point, uint>> v;
//   for(int i = 0; i < 4; i++){
//     v.push_back(make_tuple())
//   }
//
// }

unordered_map<int, int, hash<int>> Map::respond(Point p){
  int up = -1;
  int right = -1;
  int left = -1;
  int down = -1;
  // cout << this->height << " " << this->width << endl;
  unordered_map<int, int, hash<int>> m;
  // this->S = make_tuple(p.x, p.y);
  // vector<tuple<Point, uint>> t = this->lookup();
  if(this->map[p.x][p.y] == '#'){
    cout << "Moving is impossible! Goodbye!" << endl;
    exit(1);
  }
  if (p.x != 0) up = this->map[p.x - 1][p.y];
  if (p.y != (this->width - 1)) right = this->map[p.x][p.y + 1];
  if (p.y != 0) left = this->map[p.x][p.y - 1];
  if (p.x != (this->height - 1)) down = this->map[p.x + 1][p.y];
  m = {
    {1, up},
    {2, right},
    {3, left},
    {4, down}
  };

  return m;
}
