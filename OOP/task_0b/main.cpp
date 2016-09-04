#include "header.h"

int main (int argc, char **argv){
  string line;
  list<string> lines;
  cout << argv[0] << endl;
  if (argc > 1){
    ifstream in(argv[1]);
    ofstream out(argv[2]);

    if (!in) {
      cout << "No such file" << endl;
      exit(FILE_ERR);
    }

    if (!out) {
      cout << "No such file" << endl;
      exit(FILE_ERR);
    }

    while(!in.eof()){
      getline(in, line);
      lines.push_back(line);
    }

    lines.pop_back();
    lines.sort();

    while(lines.size()){
      out << lines.front() << "\n";
      lines.pop_front();
    }

    in.close();
    out.close();
  } else cout << "No arguments" << endl;

  return 0;
}
