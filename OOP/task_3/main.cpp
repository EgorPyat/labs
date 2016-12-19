#include "node.h"

int main(){
  ifstream in("test.txt");
  CSVparser<string, double, char, size_t> r(in);
  for (tuple<string, double, char, size_t> rs : r) {
    cout << rs << endl;
  }
  in.close();
}
