#ifndef _TUP_PR
#define _TUP_PR

template<typename T>
std::ostream& print(std::ostream& out, const T& what) {
  return out << what;
}

template<typename T, typename... Types>
std::ostream& print(std::ostream& out, const T& what, const Types& ... other) {
  return print(out << what << ' ', other...);
}

template<int ...>
struct seq { };

template<int N, int... S>
struct make_range : make_range<N-1, N-1, S...> { };

template<int ...S>
struct make_range<0, S...> {
  typedef seq<S...> result;
};

template<typename... Types, int... range>
std::ostream& tuple_print(std::ostream& out, const std::tuple<Types...>& what, const seq<range...>) {
  return print(out, std::get<range>(what)...);
}

template<typename... Types>
std::ostream& operator<<(std::ostream& out, const std::tuple<Types...>& what) {
  return tuple_print(out, what, typename make_range<sizeof...(Types)>::result());
}

#endif
