#ifndef _PARSER
#define _PARSER

class BadParse{
  int col;
  int row;
public:
  BadParse(int col, int row){
    this->col = col;
    this->row = row;
  }
};

template<typename... Types>
class CSVparser {
private:
  istream* in;
  string arr[sizeof...(Types)];
private:
  typedef tuple<Types...> tuple_type;
  template<size_t N, typename T> struct _parse;
  template<size_t N, typename A0, typename... An>
  struct _parse<N, tuple<A0, An...>> {
    void tuple_create(string* in, tuple_type& t) throw(BadParse){
      stringstream ss;
      ss << in[N];
      if (!(ss >> get<N>(t)))
      {
        cout << "Bad input!\n";
      }
      A0 y = get<N>(t);

      ss.seekg(0);
      ss.seekp(0);

      string k;
      stringstream hh;
      hh << y;
      hh >> k;
      string l;
      ss << in[N];
      ss >> l;
      // cout << "k: " << k << ' '<< "l: " << l << endl;
      if(k != l){
        cout << "Bad input" << endl;
      }
      _parse<N+1, tuple<An...>> _p;
      _p.tuple_create(in, t);
    }
  };
  template<size_t N >
  struct _parse<N, tuple<>> {
    void tuple_create(string* in, tuple_type& t) {}
  };
public:
  CSVparser(istream& in){
    this->in = &in;
  }
  ~CSVparser(){};

  string* gp_row() throw(BadParse){
    int i = 0;
    string line;
    string line_part;
    getline(*this->in, line);
      // cout << "Line: " << line << '|' << endl;
      for (string::iterator it=line.begin(); it!=line.end(); ++it){
        if(isalpha(*it) || isdigit(*it) || *it == '.'){
          line_part.push_back(*it);
        }
        else if(*it == ','){
          this->arr[i] = line_part;
          line_part = "";
          ++i;
        }
        if((it + 1) == line.end() && line_part != ""){
          this->arr[i] = line_part;
          ++i;
        }
      }
      // for(int j = 0; j < i; j++){
      //   cout << "Part[" << j << "]: " << this->arr[j] << endl;
      // }

    return this->arr;
  }

  tuple_type tuple_create(string* in) throw(BadParse){
    tuple<Types...> result;
    _parse<0, tuple_type> _p;
    _p.tuple_create(in, result);
    return result;
  };
};

#endif
