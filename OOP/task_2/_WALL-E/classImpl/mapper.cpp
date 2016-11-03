#include "../headers/node.h"

Mapper::Mapper(ifstream& in){
  int size = 0;
  int i;
  int j;
  string line;
  for (i = 0; getline(in, line); i++) {
    this->map.push_back(vector<int>());
    size += line.size();
    for (j = 0; j <= line.size(); j++) {
      this->map[i].push_back(line[j]);
      cout << (char)this->map[i][j] << ' ';
      if(j == line.size()) {
        cout << '\n' << '\n';
      }
    }
  }
  cout << endl;
  this->height = i;
  this->width = j - 1;
}
