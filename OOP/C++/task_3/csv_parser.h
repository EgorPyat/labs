#ifndef _PARSER
#define _PARSER

class BadParse{
  int col;
  int row;
  int i;
public:
  BadParse(int col, int row, int i){
    this->col = col;
    this->row = row;
    this->i = i;
  }
  BadParse(int row, int i){
    this->row = row;
    this->i = i;
  }
  void print_error(){
    if(i == 1) cout << "Input error in row: " << this->row << " , col: " << this->col << "!" << endl;
    if(i == 2) cout << "Too many arguments error in row: " << this->row << "!" << endl;
    if(i == 3) cout << "Not enough arguments error in row: " << this->row << "!" << endl;
  }
};

template<typename... Types>
class CSVparser {
private:
  vector<tuple<Types...>> tv;
private:
  bool safe;
  bool valid_char;
  char screen;
  char splitter;
  int row_num;
  istream* in;
  string arr[sizeof...(Types)];
private:
  typedef tuple<Types...> tuple_type;
  template<size_t N, typename T> struct _parse;
  template<size_t N, typename A0, typename... An>
  struct _parse<N, tuple<A0, An...>> {
    void tuple_create(string* in, tuple_type& t) throw(unsigned long){
      stringstream ss;
      ss << in[N];
      if(typeid(get<N>(t)).name() == typeid(in[N]).name()){
        while(!ss.eof()){
          A0 y;
          ss >> y;
          y += ' ';
          get<N>(t) += y;
        }
        get<N>(t)=get<N>(t) + '\b';
      }
      else if (!(ss >> get<N>(t)))
      {
        throw N + 1;
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
      if(k != l){
        throw N + 1;
      }
      _parse<N + 1, tuple<An...>> _p;
      _p.tuple_create(in, t);
    }
  };
  template<size_t N >
  struct _parse<N, tuple<>> {
    void tuple_create(string* in, tuple_type& t) {}
  };

  string* gp_row(string line) throw(BadParse){
    int i = 0;
    int j = 0;
    int k = 0;
    string line_part = "";
    for (string::iterator it=line.begin(); it!=line.end(); ++it){
      if(valid_char == true){
        line_part.push_back(*it);
        valid_char = false;
      }
      else if(*it == screen){
        valid_char = true;
      }
      else if((*it == ' ' || *it == splitter) && safe == true){
        line_part.push_back(*it);
      }
      else if(isalpha(*it) || isdigit(*it) || *it == '.'){
        line_part.push_back(*it);
      }
      else if(*it == splitter){
        if(line_part == "") line_part = "0";
        this->arr[i] = line_part;
        line_part = "";
        ++i;
        if(i >= sizeof...(Types)){
          throw BadParse(this->row_num + 1, 2);
        }
      }
      if((it + 1) == line.end()){
        if(line_part == "") line_part = "0";
        this->arr[i] = line_part;
        ++i;
        if(i < sizeof...(Types)){
          throw BadParse(this->row_num + 1, 3);
        }
      }
    }
    ++(this->row_num);
    return this->arr;
  };
  tuple_type tuple_create(string* in) throw(unsigned long int){
    tuple<Types...> result;
    _parse<0, tuple_type> _p;
    _p.tuple_create(in, result);
    return result;
  };
  tuple_type parse_row(string line){
    string *arr;
    tuple_type result;
    try{
      arr = this->gp_row(line);
      result = this->tuple_create(arr);
    }
    catch(unsigned long int i){
      BadParse bad(i, this->row_num, 1);
      bad.print_error();
      exit(1);
    }
    catch(BadParse& bad){
      bad.print_error();
      exit(1);
    }
    this->tv.push_back(result);
    return result;
  };
public:
  CSVparser(istream& in): row_num(0), splitter(','), screen('"'), valid_char(0), safe(0){
    this->in = &in;
    string line;
    while(getline(*this->in, line)){
      if(line == "") continue;
      tuple_type tup = this->parse_row(line);
    }
  };
  CSVparser(istream& in, char splitter, char screen): row_num(0), valid_char(false), safe(false){
    this->in = &in;
    this->splitter = splitter;
    this->screen = screen;
    string line;
    while(getline(*this->in, line)){
      if(line == "") continue;
      tuple_type tup = this->parse_row(line);
    }
  };
  ~CSVparser(){};

  class CSViterator{
    int pos;
    const CSVparser* pars;
  public:
    CSViterator(int pos, CSVparser& pars){
      this->pos = pos;
      this->pars = &pars;
    };
    bool operator!=(const CSViterator& iter){
      if(this->pos == iter.pos) return 0;
      else return 1;
    };
    tuple_type operator*(){
      return pars->tv[this->pos];
    };
    CSViterator operator++(){
      (this->pos)++;
      return *this;
    };
  };

  CSViterator begin(){
    return CSViterator(0, *this);
  };
  CSViterator end(){
    return CSViterator(this->tv.size(), *this);
  };
};

#endif
