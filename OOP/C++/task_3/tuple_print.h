#ifndef _TUP_PR
#define _TUP_PR

template<typename Ch, typename Tr, typename T>
auto print(basic_ostream<Ch,Tr>& out, const T& what) -> decltype(out){
  return out << what;
}

template<typename Ch, typename Tr, typename T, typename... Types>
auto print(basic_ostream<Ch,Tr>& out, const T& what, const Types& ... other) -> decltype(out) {
  return print(out << what << ' ', other...);
}

template<int...>
struct seq {};

template<int N, int... S>
struct make_range : make_range<N-1, N-1, S...> {};

template<int ...S>
struct make_range<0, S...> {
  typedef seq<S...> result;
};

template<typename Ch, typename Tr, typename... Types, int... range>
auto tuple_print(basic_ostream<Ch,Tr>& out, const std::tuple<Types...>& what, const seq<range...>) -> decltype(out){
  return print(out, std::get<range>(what)...);
}

template<typename Ch, typename Tr, typename... Types>
auto operator<<(basic_ostream<Ch,Tr>& out, const std::tuple<Types...>& what) -> decltype(out) {
  return tuple_print(out, what, typename make_range<sizeof...(Types)>::result());
}

#endif
