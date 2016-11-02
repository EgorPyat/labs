#include "headers/node.h"

int main( int argc, char* argv[] ){
  ifstream in;
  ifstream rm;
  ofstream out;
  parse(argc, argv, in, rm, out);

  Map m(in);
  Robot r(m, rm);
  r.step();
  in.close();
  out.close();

  return 0;
}
