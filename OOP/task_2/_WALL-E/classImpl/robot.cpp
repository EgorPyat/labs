#include "../headers/node.h"

Robot::Robot(ifstream& in, Map& m) : Mapper(in){
  int i;
  int j;

  for(i = 0; i < this->height; i++){
    for(j = 0; j < this->width; j++){
      if(this->map[i][j] == 'F'){
        this->F = make_tuple(i, j);
      }
      else if(map[i][j] == 'S'){
        this->S = make_tuple(i, j);
      }
    }
  }
  this->hidmap = &m;
  cout << "Landing success!" << endl << endl;
};

void Robot::step(){
  cout << endl << "Analyzing..." << endl << endl;
  Point p;
  p.x = get<0>(this->S);
  p.y = get<1>(this->S);
  usleep(1000000);
  unordered_map<int, int, hash<int>> m = this->hidmap->respond(p);

  if(m[0] == '#'){
    cout << "Moving is impossible! Goodbye!" << endl;
    exit(_IMPMOVE);
  }
  for(int i = 0; i < this->height; i++){
    for(int j = 0; j < this->width; j++){
      if(i == (get<0>(this->S)) && j == (get<1>(this->S) + 1)){
        if (m[2] == '#'){
          this->map[i][j] = _BLOCK;
          cout << this->map[i][j];
        }
        else {
          int q = p.x;
          int u = get<0>(this->F);
          int l = p.y + 1;
          int h = get<1>(this->F);
          map[i][j] = abs(q - u) + abs(l - h);
          cout << this->map[i][j] << ' ';
        }
      }
      else if(i == (get<0>(this->S)) && j == (get<1>(this->S) - 1)){
        if (m[3] == '#'){
          this->map[i][j] = _BLOCK;
          cout << this->map[i][j];
        }
        else {
          int q = p.x;
          int u = get<0>(this->F);
          int l = p.y - 1;
          int h = get<1>(this->F);
          map[i][j] = abs(q - u) + abs(l - h);
          cout << this->map[i][j] << ' ';
        }
      }
      else if(i == (get<0>(this->S) + 1) && j == (get<1>(this->S))){
        if (m[4] == '#'){
          this->map[i][j] = _BLOCK;
          cout << this->map[i][j];
        }
        else {
          int q = p.x + 1;
          int u = get<0>(this->F);
          int l = p.y;
          int h = get<1>(this->F);
          map[i][j] = abs(q - u) + abs(l - h);
          cout << this->map[i][j] << ' ';
        }
      }
      else if(i == (get<0>(this->S) - 1) && j == (get<1>(this->S))){
        if (m[1] == '#'){
          this->map[i][j] = _BLOCK;
          cout << this->map[i][j];
        }
        else {
          int q = p.x - 1;
          int u = get<0>(this->F);
          int l = p.y;
          int h = get<1>(this->F);
          map[i][j] = abs(q - u) + abs(l - h);
          cout << this->map[i][j] << ' ';
        }
      }
      else{
        cout << (char)this->map[i][j] << ' ';
      }
      if(j == this->width - 1){
        cout << '\n' << '\n';
      }
    }
  }
  cout << "Moving..." << endl;
  cout << endl;
  usleep(1000000);
}
