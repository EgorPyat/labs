#include "headers/node.h"

int main( int argc, char* argv[] ){
  ifstream in;
  ifstream rm;
  ofstream out;
  parse(argc, argv, in, rm, out);

  Explore ex(in, rm);
  ex.start();

  in.close();
  out.close();
  return 0;
}
