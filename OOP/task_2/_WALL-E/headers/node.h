#ifndef _NODE
#define _NODE
#include <iostream>
#include <fstream>
#include <string>
#include <cstdlib>
#include <vector>
#include <tuple>
#include <unordered_map>
#include <cmath>
#include <algorithm>
#include <unistd.h>
using namespace std;

class BadMove : public std::exception{};

struct Point{
  uint x;
  uint y;
};

#include "../headers/parser.h"
#include "../headers/mapper.h"
#include "../headers/map.h"
#include "../headers/robot.h"
#include "../headers/surface.h"

#endif
