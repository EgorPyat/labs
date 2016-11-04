#include "headers/node.h"

int main( int argc, char* argv[] ){
  ifstream in;
  ifstream rm;
  ofstream out;
  parse(argc, argv, in, rm, out);

  Map m(in);
  Robot r(rm, m);
  r.explore();
  in.close();
  out.close();
  return 0;
}
