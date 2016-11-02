#include "../headers/node.h"

Robot::Robot(Map& m, ifstream& rm){
  this->hidmap = &m;

  int size = 0;
  int i;
  int j;
  string line;
  for (i = 0; getline(rm, line); i++) {
    this->map.push_back(vector<int> ());
    size += line.size();
    for (j = 0; j <= line.size(); j++) {
      this->map[i].push_back(line[j]);
      if(this->map[i][j] == 'F'){
        this->F = make_tuple(i, j);
      }
      else if(map[i][j] == 'S'){
        this->S = make_tuple(i, j);
      }
      cout << (char)this->map[i][j] << " ";
      if(j == line.size()) {
        cout << '\n' << '\n';
      }
    }
  }
  this->height = i;
  this->width = j - 1;
  cout << endl << "Landing sucsess!" << endl << endl;
}

uint Robot::move(Point point) throw(BadMove){
  return 0;
}

void Robot::step(){
  cout << this->height << " " << this->width << endl;
  Point p;
  p.x = get<0>(this->S);
  p.y = get<1>(this->S);
  unordered_map<int, int, hash<int>> m = this->hidmap->respond(p);

  for(int i = 0; i < this->height; i++){
    for(int j = 0; j < this->width; j++){
      if(i == (get<0>(this->S)) && j == (get<1>(this->S) + 1)){
        if (m[2] == '#'){
          // cout << '#' << ' ';
          this->map[i][j] = -1;
          cout << map[i][j];
        }
        else {
          int q = p.x;
          int u = get<0>(this->F);
          int l = p.y + 1;
          int h = get<1>(this->F);
          // cout << q - u << endl;
          map[i][j] = abs(q - u) + abs(l - h);
          cout << map[i][j];
          //cout << (char)this->map[i][j] << ' ';
        }
      }
      else if(i == (get<0>(this->S)) && j == (get<1>(this->S) - 1)){
        if (m[3] == '#'){
          // cout << '#' << ' ';
          this->map[i][j] = -1;
          cout << map[i][j];
        }
        else cout << (char)this->map[i][j] << ' ';
      }
      else if(i == (get<0>(this->S) + 1) && j == (get<1>(this->S))){
        if (m[4] == '#'){
          // cout << '#' << ' ';
          this->map[i][j] = -1;
          cout << map[i][j];
        }
        else cout << (char)this->map[i][j] << ' ';
      }
      else if(i == (get<0>(this->S) - 1) && j == (get<1>(this->S))){
        if (m[1] == '#'){
          // cout << '#' << ' ';
          this->map[i][j] = -1;
          cout << map[i][j];
        }
        else cout << (char)this->map[i][j] << ' ';
      }
      else{
        cout << (char)this->map[i][j] << ' ';
      }
      if(j == this->width - 1){
        cout << '\n' << '\n';
      }
    }
  }

  cout << endl;
}
