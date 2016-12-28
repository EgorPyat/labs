#include "header.h"

int main (int argc, char **argv){
  string line;
  list<string> lines;
  list<string>::iterator r;//const_iterator
  list<string>::iterator l;

  if (argc == 3){
    ifstream in(argv[1]);
    ofstream out(argv[2]);
    ostream_iterator<string> st (out, "\n");

    if (!in ) {
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

    sort_strings(lines);

    /*while(lines.size()){
      out << lines.front() << "\n";
      lines.pop_front();
    }*/
    /*for(l = lines.begin(), r = lines.end(); l != r; l++){
      out << *l << endl;
    }*/
    copy(lines.begin(), lines.end(), st);

    in.close();
    out.close();
  }
  else cout << "Bad arguments" << endl;

  return 0;
}
