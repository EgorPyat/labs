#include "node.h"

int main(){
  tuple<double, string, char> tup = make_tuple(3.56, "Rocket", 'W');
  cout << tup << endl;
  return 0;
}
