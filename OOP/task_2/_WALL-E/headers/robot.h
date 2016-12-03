#ifndef _ROBOT
#define _ROBOT
#define _BLOCK -1
#define _IMPMOVE 1

template<typename P, typename M> class Robot : public Mapper<P,M> {
  tuple<M,M> F;
  tuple<M,M> S;
  tuple<M,M> R;
  tuple<M,M> min_aval;
  Map<P,M>* hidmap;
  M length;
  int limit;
  bool step();
public:
  Robot(ifstream&, Map<P,M>&, int);
  ~Robot(){};
  void explore();
};

template<typename M> class Robot<string,M> : public Mapper<string, M>{
  ifstream dict;
  list<string> dictionary;
  Map<string,M>* hidmap;
  M length;
  int limit;
  bool step();
public:
  Robot(ifstream&, Map<string,M>&, int);
  ~Robot(){};
  void explore();
};

template<typename M> Robot<string,M>::Robot(ifstream& in, Map<string,M>& m, int limit) : Mapper<string,M>(in){
  this->limit = limit;
  this->hidmap = &m;
  this->length = 0;
  try{
    this->dict.open("dictionary.map");
    if(!this->dict) throw &this->dict;
  }
  catch(ifstream*){
    cout << "No such dictionary!" << endl;
    exit(1);
  }
  M i;
  string line;
  for(i = 0; getline(this->dict, line); i++){
    this->dictionary.push_back(line);
  }
  cout << "Landing success!" << endl << endl;
  cout << "Press any button to start exploring." << endl;
  getchar();
  cout << string(100, '\n');
}

template<typename M> bool Robot<string, M>::step(){
  M min = 100;
  M t;
  string mins = "";
  list<string>::iterator l;
  list<string>::iterator r = this->map.end();
  list<string>::iterator ds;
  list<string>::iterator df = this->dictionary.end();
  string s = "";
  M flag = 0;
  --r;
  unordered_map<M,string, hash<M>> m = this->hidmap->respond(this->start);
  unordered_map<M,string, hash<M>> u = m;
  if(m.empty()){
    cout << "No strings to godd! Expedition failed!" << endl;
    return 1;
  }
  cout << endl;
  int count ;
  cout << "start = " << this->start << endl;
  // cout << m.size() << endl;
  for(M i = 0; i < m.size(); i++){
    count = 0;
    for(ds = this->dictionary.begin(), df; ds != df; ds++){
      if(m[i] == *ds){
        count++;
      }
    }
    if(count == 0){
      for(l = this->map.begin(), r; l != r; l++){
        if(m[i] == *l || (s = m[i] + "#") == *l){
          flag = 1;
          break;
        }
      }
      if(flag == 1){
        flag = 0;
        continue;
      }
      m[i] += "#";
      string l = this->map.back();
      this->map.back() = m[i];
      m[i] = l;
      this->map.push_back(m[i]);
      // flag = 1;
      continue;
    }
    m = u;
    flag = 0;
    if((t = edit_distance(this->finish, m[i])) <= min){
      // cout << "min " << m[i] << endl;
      for(l = this->map.begin(), r; l != r; l++){
        if(m[i] == *l || (s = m[i] + "#") == *l){
          flag = 1;
          break;
        }
      }
      if(flag == 1){
        flag = 0;
        continue;
      }
      if(mins != ""){
        string l = this->map.back();
        this->map.back() = mins;
        mins = l;
        this->map.push_back(mins);
      }
      mins = m[i];
      min = t;
      // cout << "ins: " << m[i] << endl;
    }
    else {
      for(l = this->map.begin(), r; l != r; l++){
        if(m[i] == *l || (s = m[i] + "#") == *l){
          flag = 1;
          break;
        }
      }
      if(flag == 1){
        flag = 0;
        continue;
      }
      string l = this->map.back();
      this->map.back() = m[i];
      m[i] = l;
      this->map.push_back(m[i]);
    }
  }

  if(mins == ""){
    *(this->map.begin()) += "#";
    min = 100;
    for(l = this->map.begin(), r; l != r; l++){
      if(((*l)[(*l).length() - 1 ] != '#') && (t = edit_distance(this->finish, *l)) <= min){
        mins = *l;
        min = t;
      }
    }

    if(mins == ""){
      for(l = this->map.begin(), r = this->map.end(); l != r; l++){
        cout << *l << endl;
      }
      cout << endl << "No stings to go! Expedition failed!" << endl;
      return 1;
    }
    else{
      this->start = mins;
      for(l = this->map.begin(), r = this->map.end(); l != r; l++){
        if(*l == mins){
          string k = this->map.front();
          this->map.front() = mins;
          *l = k;
        }
      }

      for(l = this->map.begin(), r = this->map.end(); l != r; l++){
          cout << *l << endl;
      }
      cout << endl;
      getchar();
      this->length++;
      if(this->limit != -1){
        if (this->length >= this->limit){
          cout << "This robot is too simple or you want too much! Limit is exceeded!" << endl;
          return 1;
        }
      }
      return 0;
    }
  }
  // cout << string( 100, '\n' );

  if(mins == this->finish){
    cout << "Expedition success!" << endl;
    this->length++;
    cout << "L: " << this->length << endl;
    *(this->map.begin()) += "#";
    for(l = this->map.begin(), r = this->map.end(); l != r; l++){
      cout << *l << endl;
    }
    return 1;
  }

  *(this->map.begin()) += "#";
  this->map.push_front(mins);
  this->start = mins;

  for(l = this->map.begin(), r = this->map.end(); l != r; l++){
    cout << *l << endl;
  }
  cout << endl;
  getchar();
  this->length++;
  if(this->limit != -1){
    if (this->length >= this->limit){
      cout << "This robot is too simple or you want too much! Limit is exceeded!" << endl;
      return 1;
    }
  }
  return 0;
}

template<typename M> void Robot<string,M>::explore(){
  bool k;
  while(!(k = this->step())){}
}

template<typename P, typename M> Robot<P,M>::Robot(ifstream& in, Map<P,M>& m, int limit) : Mapper<P,M>(in){
  M i;
  M j;
  this->length = 0;
  this->limit = limit;
  for(i = 0; i < this->height; i++){
    for(j = 0; j < this->width; j++){
      if(this->map[i][j] == 'F'){
        this->F = make_tuple(i, j);
      }
      else if(this->map[i][j] == 'R'){
        this->S = make_tuple(i, j);
        this->R = this->S;
      }
    }
  }

  this->hidmap = &m;
  cout << "Landing success!" << endl << endl;
  cout << "Press any button to start exploring." << endl;
  getchar();
  cout << string( 100, '\n' );
};

template<typename P, typename M> bool Robot<P, M>::step(){
  P p;
  p.x = get<0>(this->S);
  p.y = get<1>(this->S);
  unordered_map<M,M, hash<M>> m = this->hidmap->respond(p);

  if(m[0] == '#'){
    cout << "Moving is impossible! Goodbye!" << endl;
    exit(_IMPMOVE);
  }
  for(M i = 0; i < this->height; i++){
    for(M j = 0; j < this->width; j++){
      if(i == (get<0>(this->S)) && j == (get<1>(this->S) + 1)){
        if (m[2] == '#'){
          this->map[i][j] = _BLOCK;
          cout << this->map[i][j] << ' ';
        }
        else {
         M q = p.x;
         M u = get<0>(this->F);
         M l = p.y + 1;
         M h = get<1>(this->F);
          if(this->map[i][j] != -2) this->map[i][j] = abs(q - u) + abs(l - h);
          cout << this->map[i][j] << ' ' << ' ';
        }
      }
      else if(i == (get<0>(this->S)) && j == (get<1>(this->S) - 1)){
        if (m[3] == '#'){
          this->map[i][j] = _BLOCK;
          cout << this->map[i][j] << ' ';
        }
        else {
         M q = p.x;
         M u = get<0>(this->F);
         M l = p.y - 1;
         M h = get<1>(this->F);
          if(this->map[i][j] != -2) this->map[i][j] = abs(q - u) + abs(l - h);
          cout << this->map[i][j] << ' ' << ' ';
        }
      }
      else if(i == (get<0>(this->S) + 1) && j == (get<1>(this->S))){
        if (m[4] == '#'){
          this->map[i][j] = _BLOCK;
          cout << this->map[i][j] << ' ';
        }
        else {
         M q = p.x + 1;
         M u = get<0>(this->F);
         M l = p.y;
         M h = get<1>(this->F);
          if(this->map[i][j] != -2) this->map[i][j] = abs(q - u) + abs(l - h);
          cout << this->map[i][j] << ' ' << ' ';
        }
      }
      else if(i == (get<0>(this->S) - 1) && j == (get<1>(this->S))){
        if (m[1] == '#'){
          this->map[i][j] = _BLOCK;
          cout << this->map[i][j] << ' ';
        }
        else {
         M q = p.x - 1;
         M u = get<0>(this->F);
         M l = p.y;
         M h = get<1>(this->F);
         if(this->map[i][j] != -2) this->map[i][j] = abs(q - u) + abs(l - h);
         cout << this->map[i][j] << ' ' << ' ';
        }
      }
      /*____________________________DOWN____________________________*/
      else if(i == 0 && j == (get<1>(this->S)) && (get<0>(this->S) == this->height - 1) && m[4] != -1){ // TOR DOWN
        if(m[4] == '#'){
          this->map[i][j] = _BLOCK;
          cout << this->map[i][j] << ' ';
        }
        else{
          M q = i;
          M u = get<0>(this->F);
          M l = j;
          M h = get<1>(this->F);
          if(this->map[i][j] != -2) this->map[i][j] = abs(q - u) + abs(l - h);
          cout << this->map[i][j] << ' ' << ' ';
        }
      }
      /*_____________________________UP_____________________________*/
      else if((i == this->height - 1) && j == (get<1>(this->S)) && (get<0>(this->S) == 0) && m[1] != -1){ // TOR UP
        if(m[1] == '#'){
          this->map[i][j] = _BLOCK;
          cout << this->map[i][j] << ' ';
        }
        else{
          M q = i;
          M u = get<0>(this->F);
          M l = j;
          M h = get<1>(this->F);
          if(this->map[i][j] != -2) this->map[i][j] = abs(q - u) + abs(l - h);
          cout << this->map[i][j] << ' ' << ' ';
        }
      }
      /*____________________________LEFT_____________________________*/
      else if(i == (get<0>(this->S)) && (j == this->width - 1) && (get<1>(this->S) == 0) && m[3] != -1){ // TOR / CYLINDER LEFT
        if(m[3] == '#'){
          this->map[i][j] = _BLOCK;
          cout << this->map[i][j] << ' ';
        }
        else{
          M q = i;
          M u = get<0>(this->F);
          M l = j;
          M h = get<1>(this->F);
          if(this->map[i][j] != -2) this->map[i][j] = abs(q - u) + abs(l - h);
          cout << this->map[i][j] << ' ' << ' ';
        }
      }
      /*____________________________RIGHT____________________________*/
      else if(i == (get<0>(this->S)) && j == 0 && (get<1>(this->S) == this->width - 1) && m[2] != -1){ // TOR / CYLINDER RIGHT
        if(m[2] == '#'){
          this->map[i][j] = _BLOCK;
          cout << this->map[i][j] << ' ';
        }
        else{
          M q = i;
          M u = get<0>(this->F);
          M l = j;
          M h = get<1>(this->F);
          if(this->map[i][j] != -2) this->map[i][j] = abs(q - u) + abs(l - h);
          cout << this->map[i][j] << ' ' << ' ';
        }
      }
      /*____________________________________________________________*/
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
  if(this->map[get<0>(this->F)][get<1>(this->F)] == -1){
    cout << "Aim is unattainable! Expedition failed!" << endl;
    return 1;
  }

  // usleep(500000);
  cout << string( 100, '\n' );

  M move_flag = 4;
  M min = this->height + this->width + 1;
  M chk = min;
  M up;
  M right;
  M left;
  M down;
  double u_pr = this->height + this->width + 1;
  double r_pr = this->height + this->width + 1;
  double l_pr = this->height + this->width + 1;
  double d_pr = this->height + this->width + 1;
  M x_min = this->height;
  M y_min = this->width;
  M x = get<0>(this->S);
  M y = get<1>(this->S);

  if((x != 0) && ((this->map[x - 1][y] != -1) && (this->map[x - 1][y] != -2) && (this->map[x - 1][y] != -3))) {
    up = this->map[x - 1][y];
    u_pr = sqrt(pow((get<0>(this->F) - (x - 1)), 2) + pow((get<1>(this->F) - y), 2));
  }
  else if(x == 0 && m[1] != -1 && this->map[this->height - 1][y] != -1 && this->map[this->height - 1][y] != -2){ // tor up
    up = this->map[this->height - 1][y];
    u_pr = sqrt(pow((get<0>(this->F) - (this->height - 1)), 2) + pow((get<1>(this->F) - y), 2));
  }
  else {
    up = min;
    --move_flag;
  }
  if((y != this->width - 1) && ((this->map[x][y + 1] != -1) && (this->map[x][y + 1] != -2) && (this->map[x][y + 1] != -3))) {
    right = this->map[x][y + 1];
    r_pr = sqrt(pow((get<0>(this->F) - (x)), 2) + pow((get<1>(this->F) - (y + 1)), 2));
  }
  else if(y == (this->width - 1) && m[2] != -1 && this->map[x][0] != -1 && this->map[x][0] != -2){ // tor / cylinder right
    right = this->map[x][0];
    r_pr = sqrt(pow((get<0>(this->F) - (x)), 2) + pow((get<1>(this->F) - (0)), 2));
  }
  else {
    right = min;
    --move_flag;
  }
  if((y != 0) && ((this->map[x][y - 1] != -1) && (this->map[x][y - 1] != -2) && (this->map[x][y - 1] != -3))) {
    left = this->map[x][y - 1];
    l_pr = sqrt(pow((get<0>(this->F) - (x)), 2) + pow((get<1>(this->F) - (y - 1)), 2));
  }
  else if(y == 0 && m[3] != -1 && this->map[x][this->width - 1] != -1 && this->map[x][this->width - 1] != -2){ // tor / cylinder left
    left = this->map[x][this->width - 1];
    l_pr = sqrt(pow((get<0>(this->F) - (x)), 2) + pow((get<1>(this->F) - (this->width - 1)), 2));
  }
  else {
    left = min;
    --move_flag;
  }
  if((x != (this->height - 1)) && ((this->map[x + 1][y] != -1) && (this->map[x + 1][y] != -2) && (this->map[x + 1][y] != -3))) {
    down = this->map[x + 1][y];
    d_pr = sqrt(pow((get<0>(this->F) - (x + 1)), 2) + pow((get<1>(this->F) - y), 2));
  }
  else if(x == (this->height - 1) && m[4] != -1 && (this->map[0][y] != -1) && (this->map[0][y] != -2)){ // tor down
    down = this->map[0][y];
    d_pr = sqrt(pow((get<0>(this->F) - (0)), 2) + pow((get<1>(this->F) - y), 2));
  }
  else {
    down = min;
    --move_flag;
  }

  double pr = std::min(std::min(std::min(u_pr, r_pr), l_pr), d_pr);
  M pr_fl = 0;
  if(up < min && pr == u_pr && pr_fl == 0) {
    min = up;
    if(x == 0 && m[1] != -1) x_min = this->height - 1; // for tor up
    else  x_min = x - 1;
    y_min = y;
    pr_fl = 1;
  }
  if(right < min && pr == r_pr && pr_fl == 0) {
    min = right;
    x_min = x;
    if(y == this->width - 1 && m[2] != -1) y_min = 0; // for tor / cylinder right
    else y_min = y + 1;
    pr_fl = 1;
  }
  if(left < min && pr == l_pr && pr_fl == 0) {
    min = left;
    x_min = x;
    if(y == 0 && m[3] != -1) y_min = this->width - 1; // for tor /cylinder left
    else y_min = y - 1;
    pr_fl = 1;
  }
  if(down < min && pr == d_pr && pr_fl == 0) {
    min = down;
    if((x == this->height - 1) && m[4] != -1) x_min = 0; // for tor down
    else x_min = x + 1;
    y_min = y;
    pr_fl = 1;
  }
 M min_aval_route = this->height + this->width + 1;
  if(move_flag == 0){
    for(M i = 0; i < this->height; i++){
      for(M j = 0; j < this->width; j++){
        if(this->map[i][j] == '.' || this->map[i][j] == 'R' || this->map[i][j] == 'F'){
          continue;
        }
        else if(this->map[i][j] >= 0 && this->map[i][j] < min_aval_route){
          min_aval_route = this->map[i][j];
          this->min_aval = make_tuple(i, j);
        }
      }
    }
    if(min_aval_route == (this->height + this->width + 1)){
      cout << "No way out! Aim is unattainable! Expedition failed!" << endl;
      return 1;
    }
    else{
      this->length++;
      this->map[x][y] = -2;
      this->S = this->min_aval;
      this->map[get<0>(this->min_aval)][get<1>(this->min_aval)] = 'R';
    }
  }
  else{
    this->map[x][y] = -2;
    this->length++;
    this->S = make_tuple(x_min, y_min);
    this->map[x_min][y_min] = 'R';
  }

  cout << endl;
  for(M i = 0; i < this->height; i++){
    for(M j = 0; j < this->width; j++){
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
    cout << this->length << endl;
    this->map[get<0>(this->R)][get<1>(this->R)] = 0;
    cout << endl;
    for(M i = 0; i < this->height; i++){
      for(M j = 0; j < this->width; j++){
        if(this->map[i][j] == '.' || this->map[i][j] == 'R' || this->map[i][j] == 'F'){
          cout << (char)this->map[i][j] << ' ' << ' ';
        }
        else if(this->map[i][j] == -2 || this->map[i][j] == 0){
          this->map[i][j] = '*';
          cout << (char)this->map[i][j] << ' ' << ' ';
        }
        else cout << this->map[i][j] << ' ';
        if(j == this->width - 1) {
          cout << '\n' << '\n';
        }
      }
    }
    return 1;
  }
  if(this->limit != -1){
    if (this->length >= this->limit){
      cout << "This robot is too simple or you want too much! Limit is exceeded!" << endl;
      return 1;
    }
  }
  return 0;
}

template<typename P, typename M> void Robot<P,M>::explore(){
  bool k;
  while(!(k = this->step())){}
}

#endif
