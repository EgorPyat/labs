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
  Point p;
  p.x = get<0>(this->S);
  p.y = get<1>(this->S);
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
          if(this->map[i][j] != -2 && this->map[i][j] != -4 && this->map[i][j] != -3) this->map[i][j] = abs(q - u) + abs(l - h);
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
          if(this->map[i][j] != -2 && this->map[i][j] != -4 && this->map[i][j] != -3) this->map[i][j] = abs(q - u) + abs(l - h);
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
          if(this->map[i][j] != -2 && this->map[i][j] != -4 && this->map[i][j] != -3) this->map[i][j] = abs(q - u) + abs(l - h);
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
          if(this->map[i][j] != -2 && this->map[i][j] != -4 && this->map[i][j] != -3) this->map[i][j] = abs(q - u) + abs(l - h);
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

  // usleep(500000);
  cout << string( 100, '\n' );

  int move_flag = 4;
  int min = this->height + this->width;
  int up;
  int right;
  int left;
  int down;
  double u_pr = this->height + this->width;
  double r_pr = this->height + this->width;
  double l_pr = this->height + this->width;
  double d_pr = this->height + this->width;
  int x_min = this->height;
  int y_min = this->width;
  int x = get<0>(this->S);
  int y = get<1>(this->S);

  if((x != 0) && ((this->map[x - 1][y] != -1) && (this->map[x - 1][y] != -2) && (this->map[x - 1][y] != -4) && (this->map[x - 1][y] != -3))) {
    up = this->map[x - 1][y];
    u_pr = sqrt(pow((get<0>(this->F) - (x - 1)), 2) + pow((get<1>(this->F) - y), 2));
  }
  else {
    up = min;
    --move_flag;
  }
  if((y != this->width - 1) && ((this->map[x][y + 1] != -1) && (this->map[x][y + 1] != -2) && (this->map[x][y + 1] != -4) && (this->map[x][y + 1] != -3))) {
    right = this->map[x][y + 1];
    r_pr = sqrt(pow((get<0>(this->F) - (x)), 2) + pow((get<1>(this->F) - (y + 1)), 2));
  }
  else {
    right = min;
    --move_flag;
  }
  if((y != 0) && ((this->map[x][y - 1] != -1) && (this->map[x][y - 1] != -2) && (this->map[x][y - 1] != -4) && (this->map[x][y - 1] != -3))) {
    left = this->map[x][y - 1];
    l_pr = sqrt(pow((get<0>(this->F) - (x)), 2) + pow((get<1>(this->F) - (y - 1)), 2));
  }
  else {
    left = min;
    --move_flag;
  }
  if((x != (this->height - 1)) && ((this->map[x + 1][y] != -1) && (this->map[x + 1][y] != -2) && (this->map[x + 1][y] != -4) && (this->map[x + 1][y] != -3))) {
    down = this->map[x + 1][y];
    d_pr = sqrt(pow((get<0>(this->F) - (x + 1)), 2) + pow((get<1>(this->F) - y), 2));
  }
  else {
    down = min;
    --move_flag;
  }
  double pr = std::min(std::min(std::min(u_pr, r_pr), l_pr), d_pr);
  // cout << "u " << u_pr << endl;
  // cout << "r " << r_pr << endl;
  // cout << "l " << l_pr << endl;
  // cout << "d " << d_pr << endl;
  // cout << "m " << pr << endl;
  // getchar();
  int pr_fl = 0;
  if(up <= min && pr == u_pr && pr_fl == 0) {
    min = up;
    x_min = x - 1;
    y_min = y;
    // pr_fl = 1;
  }
  if(right <= min && pr == r_pr && pr_fl == 0) {
    min = right;
    x_min = x;
    y_min = y + 1;
    // pr_fl = 1;
  }
  if(left <= min && pr == l_pr && pr_fl == 0) {
    min = left;
    x_min = x;
    y_min = y - 1;
    // pr_fl = 1;
  }
  if(down <= min && pr == d_pr && pr_fl == 0) {
    min = down;
    x_min = x + 1;
    y_min = y;
    // pr_fl = 1;
  }

  if(this->map[get<0>(this->F)][get<1>(this->F)] == -1){
    cout << "Aim is unattainable! Expedition failed!" << endl;
    return 1;
  }

  // if(move_flag > 1){
  //   this->map[x][y] = -3;
  //   this->S = make_tuple(x_min, y_min);
  //   this->map[x_min][y_min] = 'R';
  // }
  // else if(move_flag == 0){
  //   this->map[x][y] = -4;
  //   if(x!=0 && (this->map[x - 1][y] == -3 || this->map[x - 1][y] == -2)){
  //     this->S = make_tuple(x - 1, y);
  //     this->map[x - 1][y] = 'R';
  //   }
  //   else if(y != this->width - 1 && (this->map[x][y + 1] == -3 || this->map[x][y + 1] == -2)){
  //     this->S = make_tuple(x, y + 1);
  //     this->map[x][y + 1] = 'R';
  //   }
  //   else if(y != 0 && (this->map[x][y - 1] == -3 || this->map[x][y - 1] == -2)){
  //    this->S = make_tuple(x, y - 1);
  //    this->map[x][y - 1] = 'R';
  //   }
  //   else if(x != this->height -1 && (this->map[x + 1][y] == -3 || this->map[x + 1][y] == -2)){
  //    this->S = make_tuple(x + 1, y);
  //    this->map[x + 1][y] = 'R';
  //   }
  //   else{
  //     cout << "No way out! Aim is unattainable! Expedition failed!" << endl;
  //     return 1;
  //   }
  // }
  // else{
  //   this->map[x][y] = -2;
  //   this->S = make_tuple(x_min, y_min);
  //   this->map[x_min][y_min] = 'R';
  // }

  int route;
  int dead_end = 0;
  int stick;

  if (x==0 || (this->map[x-1][y] == -1 || this->map[x-1][y] == -4)){
  dead_end++;
  }
  if (y==(this->width-1) || (this->map[x][y+1] == -1 || this->map[x][y+1] == -4)){
  dead_end++;
  }
  if (y==0 || (this->map[x][y-1] == -1 || this->map[x][y-1] == -4)){
  dead_end++;
  }
  if (x==(this->height-1) || (this->map[x+1][y] == -1 || this->map[x+1][y] == -4)){
  dead_end++;
  }

  if(dead_end == 3){ // 1 ход доступен
    this->tunnel = 0;
    if(move_flag == 1){
      this->prev = this->S;
      this->map[x][y] = -4;
      this->S = make_tuple(x_min, y_min);
      this->map[x_min][y_min] = 'R';
    }
    else if(move_flag == 0){
      this->map[x][y] = -4;
      if(x!=0 && (this->map[x - 1][y] == -3 || this->map[x - 1][y] == -2)){
        this->prev = this->S;
        this->S = make_tuple(x - 1, y);
        this->map[x - 1][y] = 'R';
      }
      else if(y != (this->width - 1) && (this->map[x][y + 1] == -3 || this->map[x][y + 1] == -2)){
        this->prev = this->S;
        this->S = make_tuple(x, y + 1);
        this->map[x][y + 1] = 'R';
      }
      else if(y != 0 && (this->map[x][y - 1] == -3 || this->map[x][y - 1] == -2)){
        this->prev = this->S;
        this->S = make_tuple(x, y - 1);
        this->map[x][y - 1] = 'R';
      }
      else if(x != (this->height -1) && (this->map[x + 1][y] == -3 || this->map[x + 1][y] == -2)){
        this->prev = this->S;
        this->S = make_tuple(x + 1, y);
        this->map[x + 1][y] = 'R';
      }
    }
  }
  else if(dead_end == 2){ // 2 хода доступно это либо туннель, либо угол, если угол то можно -4
  /*#
    .
    #*/
    if(((x!=0) && (x!=this->height-1) && (this->map[x-1][y]==-1||this->map[x-1][y]==-4) && (this->map[x+1][y]==-1||this->map[x+1][y]==-4)) || \
       (x==0&&x==this->height-1) || \
       ((x==0&&x!=this->height-1)&&(this->map[x+1][y]==-1||this->map[x+1][y]==-4)) || \
       ((x!=0&&x==this->height-1)&&(this->map[x-1][y]==-1||this->map[x-1][y]==-4))\
    )
    {
      if(this->tunnel == 2 && get<0>(this->prev)!=x){
        this->map[get<0>(this->prev)][get<1>(this->prev)] = -2;
        this->tunnel = 1;
      }
      else this->tunnel = 1;
      if(move_flag > 0){
        this->prev = this->S;
        this->map[x][y] = -2;
        this->S = make_tuple(x_min, y_min);
        this->map[x_min][y_min] = 'R';
      }
      else if(move_flag == 0){
        int route2;
        int u = get<0>(this->F);
        int h = get<1>(this->F);
        int q = x;
        int l = y-1;
        route = abs(q - u) + abs(l - h);
        l = y + 1;
        route2 = abs(q - u) + abs(l - h);
        if(route <= route2){
          this->prev = this->S;
          this->map[x][y] = -2;
          this->S = make_tuple(x, y-1);
          this->map[x][y-1] = 'R';
        }
        else{
          this->prev = this->S;
          this->map[x][y] = -2;
          this->S = make_tuple(x, y+1);
          this->map[x][y+1] = 'R';
        }
      }
    }
  /*#.#*/
    else if(((y!=0) && (y!=this->width-1) && (this->map[x][y-1]==-1||this->map[x][y-1]==-4) && (this->map[x][y+1]==-1||this->map[x][y+1]==-4)) || \
       (y==0&&y==this->width-1) || \
       ((y==0&&y!=this->width-1)&&(this->map[x][y+1]==-1||this->map[x][y+1]==-4)) || \
       ((y!=0&&y==this->width-1)&&(this->map[x][y-1]==-1||this->map[x][y-1]==-4))\
    )
    {
      if(this->tunnel == 2 && get<1>(this->prev)!=y){
        this->map[get<0>(this->prev)][get<1>(this->prev)] = -2;
        this->tunnel = 1;
      }
      else this->tunnel = 1;
      if(move_flag > 0){
        this->prev = this->S;
        this->map[x][y] = -2;
        this->S = make_tuple(x_min, y_min);
        this->map[x_min][y_min] = 'R';
      }
      else if(move_flag == 0){
        int route2;
        int u = get<0>(this->F);
        int h = get<1>(this->F);
        int q = x-1;
        int l = y;
        route = abs(q - u) + abs(l - h);
        q = x+1;
        route2 = abs(q - u) + abs(l - h);
        if(route <= route2){
          this->prev = this->S;
          this->map[x][y] = -2;
          this->S = make_tuple(x-1, y);
          this->map[x-1][y] = 'R';
        }
        else{
          this->prev = this->S;
          this->map[x][y] = -2;
          this->S = make_tuple(x+1, y);
          this->map[x+1][y] = 'R';
        }
      }
    }
    else{ // угол
      if(move_flag > 0){
        this->prev = this->S;
        this->tunnel = 2;
        this->map[x][y] = -4;
        this->S = make_tuple(x_min, y_min);
        this->map[x_min][y_min] = 'R';
      }
      else if(move_flag == 0){
        this->prev = this->S;
        this->tunnel = 2;
        this->map[x][y] = -4;
        if((x!=0 && (this->map[x - 1][y] == -2 || this->map[x-1][y]==-3))){ // up
          if((y!=0 && (this->map[x][y - 1]==-2 || this->map[x][y - 1]==-3))){ // right
            int route2;
            int u = get<0>(this->F);
            int h = get<1>(this->F);
            int q = x - 1;
            int l = y;
            route = abs(q - u) + abs(l - h);
            q = x;
            l = y - 1;
            route2 = abs(q - u) + abs(l - h);
            if(route <= route2){
              this->S = make_tuple(x-1, y);
              this->map[x-1][y] = 'R';
            }
            else{
              this->S = make_tuple(x, y-1);
              this->map[x][y-1] = 'R';
            }
          }
          else{ // left
            int route2;
            int u = get<0>(this->F);
            int h = get<1>(this->F);
            int q = x - 1;
            int l = y;
            route = abs(q - u) + abs(l - h);
            q = x;
            l = y + 1;
            route2 = abs(q - u) + abs(l - h);
            if(route <= route2){
              this->S = make_tuple(x-1, y);
              this->map[x-1][y] = 'R';
            }
            else{
              this->S = make_tuple(x, y+1);
              this->map[x][y+1] = 'R';
            }
          }
        }
        else{ //down
          if((y!=0 && (this->map[x][y - 1]==-2 || this->map[x][y - 1]==-3))){ //right
            int route2;
            int u = get<0>(this->F);
            int h = get<1>(this->F);
            int q = x + 1;
            int l = y;
            route = abs(q - u) + abs(l - h);
            q = x;
            l = y - 1;
            route2 = abs(q - u) + abs(l - h);
            if(route <= route2){
              this->S = make_tuple(x+1, y);
              this->map[x+1][y] = 'R';
            }
            else{
              this->S = make_tuple(x, y-1);
              this->map[x][y-1] = 'R';
            }
          }
          else{ // left
            int route2;
            int u = get<0>(this->F);
            int h = get<1>(this->F);
            int q = x + 1;
            int l = y;
            route = abs(q - u) + abs(l - h);
            q = x;
            l = y + 1;
            route2 = abs(q - u) + abs(l - h);
            if(route <= route2){
              this->S = make_tuple(x+1, y);
              this->map[x+1][y] = 'R';
            }
            else{
              this->S = make_tuple(x, y+1);
              this->map[x][y+1] = 'R';
            }
          }
        }
      }
    }
  }
  else if(dead_end == 1){ // 3 хода доступно
    if(move_flag > 0){
      this->prev = this->S;
      this->map[x][y] = -2;
      this->S = make_tuple(x_min, y_min);
      this->map[x_min][y_min] = 'R';
    }
    else if(move_flag == 0){
      int u = get<0>(this->F);
      int h = get<1>(this->F);
      int route1;
      int route2;
      int route3;
      int rt;
      int q;
      int l;
      if(x==0 || (x!=0 && (this->map[x - 1][y]==-1 || this->map[x-1][y]==-4))) q = this->height + this->width; else q = x - 1;
      l = y;
      route = abs(q - u) + abs(l - h);
      if(x==this->height - 1 || (x!=this->height - 1 && (this->map[x + 1][y]==-1 || this->map[x+1][y]==-4))) q = this->height + this->width; else q = x + 1;
      route1 = abs(q - u) + abs(l - h);
      q = x;
      if(y==0 || (y != 0 && (this->map[x][y-1]==-1 || this->map[x][y-1]==-4))) l = this->height + this->width; else l = y - 1;
      route2 = abs(q - u) + abs(l - h);
      if(y == this->width - 1 || (y != this->width -1 && (this->map[x][y+1]==-1 || this->map[x][y+1]==-4))) l = this->height + this->width; else l = y + 1;
      route3 = abs(q - u) + abs(l - h);
      rt = std::min(std::min(std::min(route, route1), route2), route3);
      cout << route << ' ' << route1 << ' ' << route2 << ' ' << route3 << ' ' << rt << endl;
      if(route == rt){
        this->prev = this->S;
        this->map[x][y] = -2;
        this->S = make_tuple(x - 1, y);
        this->map[x - 1][y] = 'R';
      }
      else if(route1 == rt){
        this->prev = this->S;
        this->map[x][y] = -2;
        this->S = make_tuple(x + 1, y);
        this->map[x + 1][y] = 'R';
      }
      else if(route2 == rt){
        this->prev = this->S;
        this->map[x][y] = -2;
        this->S = make_tuple(x, y - 1);
        this->map[x][y - 1] = 'R';
      }
      else{
        this->prev = this->S;
        this->map[x][y] = -2;
        this->S = make_tuple(x, y + 1);
        this->map[x][y + 1] = 'R';
      }
    }
  }
    else if(dead_end == 0){ // 4 хода доступно
      if(move_flag > 0){
        this->prev = this->S;
        this->map[x][y] = -2;
        this->S = make_tuple(x_min, y_min);
        this->map[x_min][y_min] = 'R';
      }
      else if(move_flag == 0){
        int u = get<0>(this->F);
        int h = get<1>(this->F);
        int route1;
        int route2;
        int route3;
        int rt;
        int q = x - 1;
        int l = y;
        route = abs(q - u) + abs(l - h);
        q = x + 1;
        route1 = abs(q - u) + abs(l - h);
        q = x;
        l = y - 1;
        route2 = abs(q - u) + abs(l - h);
        l = y + 1;
        route3 = abs(q - u) + abs(l - h);
        rt = std::min(std::min(std::min(route, route1), route2), route3);
        if(route == rt){
          this->prev = this->S;
          this->map[x][y] = -2;
          this->S = make_tuple(x - 1, y);
          this->map[x - 1][y] = 'R';
        }
        else if(route1 == rt){
          this->prev = this->S;
          this->map[x][y] = -2;
          this->S = make_tuple(x + 1, y);
          this->map[x + 1][y] = 'R';
        }
        else if(route2 == rt){
          this->prev = this->S;
          this->map[x][y] = -2;
          this->S = make_tuple(x, y - 1);
          this->map[x][y - 1] = 'R';
        }
        else{
          this->prev = this->S;
          this->map[x][y] = -2;
          this->S = make_tuple(x, y + 1);
          this->map[x][y + 1] = 'R';
        }
      }
    }

  else if(dead_end == 4){ // 0 ходов доступно
    cout << "No way out! Aim is unattainable! Expedition failed!" << endl;
    return 1;
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
  // usleep(500000);
  cout << string( 100, '\n' );
  if(min == 0){
    cout << "Expedition success!" << endl;
    return 1;
  }
  return 0;
}

void Robot::explore(){
  bool k;
  while(!(k = this->step())){}
}
