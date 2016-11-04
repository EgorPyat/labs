#include "../headers/node.h"

Robot::Robot(ifstream& in, Map& m) : Mapper(in){
  int i;
  int j;

  for(i = 0; i < this->height; i++){
    for(j = 0; j < this->width; j++){
      if(this->map[i][j] == 'F'){
        this->F = make_tuple(i, j);
      }
      else if(map[i][j] == 'R'){
        this->S = make_tuple(i, j);
      }
    }
  }
  this->hidmap = &m;
  cout << "Landing success!" << endl << endl;
  cout << "Press any button to start exploring." << endl;
  getchar();
  cout << string( 100, '\n' );
};

bool Robot::step(){
  // cout << endl << "Analyzing..." << endl << endl;
  Point p;
  p.x = get<0>(this->S);
  p.y = get<1>(this->S);
  // usleep(500000);
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
          cout << this->map[i][j] << ' ';
        }
        else {
          int q = p.x;
          int u = get<0>(this->F);
          int l = p.y + 1;
          int h = get<1>(this->F);
          if(this->map[i][j] != -2 && this->map[i][j] != -4) this->map[i][j] = abs(q - u) + abs(l - h);
          cout << this->map[i][j] << ' ' << ' ';
        }
      }
      else if(i == (get<0>(this->S)) && j == (get<1>(this->S) - 1)){
        if (m[3] == '#'){
          this->map[i][j] = _BLOCK;
          cout << this->map[i][j] << ' ';
        }
        else {
          int q = p.x;
          int u = get<0>(this->F);
          int l = p.y - 1;
          int h = get<1>(this->F);
          if(this->map[i][j] != -2 && this->map[i][j] != -4) this->map[i][j] = abs(q - u) + abs(l - h);
          cout << this->map[i][j] << ' ' << ' ';
        }
      }
      else if(i == (get<0>(this->S) + 1) && j == (get<1>(this->S))){
        if (m[4] == '#'){
          this->map[i][j] = _BLOCK;
          cout << this->map[i][j] << ' ';
        }
        else {
          int q = p.x + 1;
          int u = get<0>(this->F);
          int l = p.y;
          int h = get<1>(this->F);
          if(this->map[i][j] != -2 && this->map[i][j] != -4) this->map[i][j] = abs(q - u) + abs(l - h);
          cout << this->map[i][j] << ' ' << ' ';
        }
      }
      else if(i == (get<0>(this->S) - 1) && j == (get<1>(this->S))){
        if (m[1] == '#'){
          this->map[i][j] = _BLOCK;
          cout << this->map[i][j] << ' ';
        }
        else {
          int q = p.x - 1;
          int u = get<0>(this->F);
          int l = p.y;
          int h = get<1>(this->F);
          if(this->map[i][j] != -2 && this->map[i][j] != -4) this->map[i][j] = abs(q - u) + abs(l - h);
          cout << this->map[i][j] << ' ' << ' ';
        }
      }
      else{
        if(this->map[i][j] == '.' || this->map[i][j] == 'R' || this->map[i][j] == 'F'){
          cout << (char)this->map[i][j] << ' ' << ' ';
        }
        else cout << this->map[i][j] << ' ';
      }
      if(j == this->width - 1){
        cout << endl << endl;
      }
    }
  }

  usleep(500000);
  cout << string( 100, '\n' );
  // cout << "Moving... ";

  int move_flag = 4;
  int min = this->height + this->width;
  int up = min;
  int right = min;
  int left = min;
  int down = min;
  int x_min = this->height;
  int y_min = this->width;
  int x = get<0>(this->S);
  int y = get<1>(this->S);
  if((x != 0) && ((this->map[x - 1][y] != -1) && (this->map[x - 1][y] != -2) && (this->map[x - 1][y] != -4))) up = this->map[x - 1][y];
  else {
    --move_flag;
  }
  if((y != this->width - 1) && ((this->map[x][y + 1] != -1) && (this->map[x][y + 1] != -2) && (this->map[x][y + 1] != -4))) right = this->map[x][y + 1];
  else {
    --move_flag;
  }
  if((y != 0) && ((this->map[x][y - 1] != -1) && (this->map[x][y - 1] != -2) && (this->map[x][y - 1] != -4))) left = this->map[x][y - 1];
  else {
    --move_flag;
  }
  if((x != (this->height - 1)) && ((this->map[x + 1][y] != -1) && (this->map[x + 1][y] != -2) && (this->map[x + 1][y] != -4))) down = this->map[x + 1][y];
  else {
    --move_flag;
  }
  if(up < min) {
    min = up;
    x_min = x - 1;
    y_min = y;
  }
  if(right < min) {
    min = right;
    x_min = x;
    y_min = y + 1;
  }
  if(left < min) {
    min = left;
    x_min = x;
    y_min = y - 1;
  }
  if(down < min) {
    min = down;
    x_min = x + 1;
    y_min = y;
  }
  if(this->map[get<0>(this->F)][get<1>(this->F)] == -1){
    // usleep(1000000);
    cout << "Aim is unattainable! Expedition failed!" << endl;
    return 1;
  }

  if(move_flag == 0){
    this->map[x][y] = -4;
    if(x!=0 && this->map[x - 1][y] == -2){
      this->S = make_tuple(x - 1, y);
      this->map[x - 1][y] = 'R';
    }
    else if(y != this->width - 1 && this->map[x][y + 1] == -2){
      this->S = make_tuple(x, y + 1);
      this->map[x][y + 1] = 'R';
    }
    else if(y != 0 && this->map[x][y - 1] == -2){
     this->S = make_tuple(x, y - 1);
     this->map[x][y - 1] = 'R';
    }
    else if(x != this->height -1 && this->map[x + 1][y] == -2){
     this->S = make_tuple(x + 1, y);
     this->map[x + 1][y] = 'R';
    }
  }
  else{
    this->map[x][y] = -2;
    this->S = make_tuple(x_min, y_min);
    this->map[x_min][y_min] = 'R';
  }
  cout << endl;
  for(int i = 0; i < this->height; i++){
    for(int j = 0; j < this->width; j++){
      if(this->map[i][j] == '.' || this->map[i][j] == 'R' || this->map[i][j] == 'F'){
        cout << (char)this->map[i][j] << ' ' << ' ';
      }
      else cout << this->map[i][j] << ' ';
      if(j == this->width - 1) {
        cout << '\n' << '\n';
      }
    }
  }
  usleep(500000);
  cout << string( 100, '\n' );
  // cout << "           OK!" << endl;
  if(min == 0){
    // usleep(500000);
    cout << "Expedition success!" << endl;
    return 1;
  }
  // usleep(500000);
  return 0;
}

void Robot::explore(){
  bool k;
  while(!(k = this->step())){}
}
