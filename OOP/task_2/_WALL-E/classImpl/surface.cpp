#include "../headers/node.h"

Explore::Explore(ifstream& in, ifstream& rm){
  this->m = new Map(in);
  this->r = new Robot(rm, *this->m);
}
void Explore::start(){
  this->r->explore();
}
