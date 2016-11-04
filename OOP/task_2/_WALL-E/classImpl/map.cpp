#include "../headers/node.h"

unordered_map<int, int, hash<int>> Map::respond(Point p){
  int up = -1;
  int right = -1;
  int left = -1;
  int down = -1;
  int center = -1;
  unordered_map<int, int, hash<int>> m = {};

  center = this->map[p.x][p.y];
  if (p.x != 0) up = this->map[p.x - 1][p.y];
  if (p.y != (this->width - 1)) right = this->map[p.x][p.y + 1];
  if (p.y != 0) left = this->map[p.x][p.y - 1];
  if (p.x != (this->height - 1)) down = this->map[p.x + 1][p.y];
  m = {
    {0, center},
    {1, up},
    {2, right},
    {3, left},
    {4, down}
  };

  return m;
}
