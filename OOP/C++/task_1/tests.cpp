#include "header.h"

TEST(TritTest, OrEqTest){
  TritSet f(5);
  f[0] = True;
  f[1] = False;
  f[4] = True;
  TritSet q = ~f;
  q |= f;
  EXPECT_EQ(True, q[0]);
  EXPECT_EQ(True, q[1]);
  EXPECT_EQ(1, q[4] == True);
  EXPECT_EQ(Unknown, q[2]);
}

TEST(TritTest, AndEqTest){
  TritSet f(5);
  f[0] = True;
  f[1] = False;
  f[4] = True;
  TritSet q = ~f;
  q &= f;
  EXPECT_EQ(False, q[0]);
  EXPECT_EQ(False, q[1]);
  EXPECT_EQ(0, q[4] == True);
  EXPECT_EQ(Unknown, q[2]);
}

TEST(TritTest, OrTest){
  TritSet f(5);
  f[0] = True;
  f[1] = False;
  f[4] = True;
  TritSet q = ~f;
  TritSet u(5);
  u = f | q;
  EXPECT_EQ(True, u[0]);
  EXPECT_EQ(True, u[1]);
  EXPECT_EQ(1, u[4] == True);
  EXPECT_EQ(Unknown, u[2]);
}

TEST(TritTest, AndTest){
  TritSet f(5);
  f[0] = True;
  f[1] = False;
  f[4] = True;
  TritSet q = ~f;
  TritSet u(5);
  u = f & q;
  EXPECT_EQ(False, u[0]);
  EXPECT_EQ(False, u[1]);
  EXPECT_EQ(0, u[4] == True);
  EXPECT_EQ(Unknown, u[2]);
}

TEST(TritTest, TrimTest){
  TritSet s(87);
  EXPECT_EQ(87, s.capacity());
  s[45] = False;
  s[46] = True;
  EXPECT_EQ(False, s[45]);
  s.trim(45);
  EXPECT_EQ(46, s.capacity());
  EXPECT_EQ(False, s[45]);
  s[77] = True;
  EXPECT_EQ(Unknown, s[46]);
}

TEST(TritTest, ShrinkTest){
  TritSet l(43);
  l.shrink();
  EXPECT_EQ(43, l.capacity());
  l[3] = True;
  l.shrink();
  EXPECT_EQ(4, l.capacity());
  l[33] = False;
  EXPECT_EQ(34, l.capacity());
  l.shrink();
  EXPECT_EQ(34, l.capacity());
  l[3] = Unknown;
  l.shrink();
  EXPECT_EQ(4, l.capacity());
}

TEST(TritTest, NotTest){
  TritSet f(5);
  f[0] = True;
  f[1] = False;
  f[4] = True;
  EXPECT_EQ(True, f[0]);
  EXPECT_EQ(False, f[1]);
  EXPECT_EQ(1, f[4] == True);
  EXPECT_EQ(Unknown, f[2]);
  TritSet q = ~f;
  EXPECT_EQ(False, q[0]);
  EXPECT_EQ(True, q[1]);
  EXPECT_EQ(0, q[4] == True);
  EXPECT_EQ(Unknown, q[2]);
  TritSet u(5);
  u = ~f;
  EXPECT_EQ(False, u[0]);
  EXPECT_EQ(True, u[1]);
  EXPECT_EQ(0, u[4] == True);
  EXPECT_EQ(Unknown, u[2]);
}

TEST(TritTest, FlipTest){
  TritSet f(5);
  f[0] = True;
  f[1] = False;
  f[4] = True;
  EXPECT_EQ(True, f[0]);
  EXPECT_EQ(False, f[1]);
  EXPECT_EQ(1, f[4] == True);
  EXPECT_EQ(Unknown, f[2]);
  f.flip();
  EXPECT_EQ(False, f[0]);
  EXPECT_EQ(True, f[1]);
  EXPECT_EQ(0, f[4] == True);
  EXPECT_EQ(Unknown, f[2]);
}

TEST(TritTest, CapacityTest) {
  TritSet a(1000);
  EXPECT_EQ(1000, a.capacity());
  a[1200] = False;
  EXPECT_EQ(1201, a.capacity());
}

TEST(TritTest, AssigmentTest){
  TritSet a(100);
  TritSet b(10);
  TritSet c(45);
  b[100] = True;
  a[32] = b[100];
  c[33] = False;
  EXPECT_EQ(True, a[32]);
  EXPECT_EQ(True, b[100]);
  EXPECT_EQ(False, c[33]);
}

TEST(TritTest, CardinalityTest){
  TritSet c(290);
  c[65] = True;
  c[32] = False;
  EXPECT_EQ(1, c.cardinality(True));
  EXPECT_EQ(1, c.cardinality(False));
  EXPECT_EQ(288, c.cardinality(Unknown));
}

TEST(TritTest, MapCardinalityTest){
  TritSet c(17);
  c[5] = True;
  EXPECT_EQ(16 , c.cardinality()[Unknown]);
  EXPECT_EQ(1 , c.cardinality()[True]);
  EXPECT_EQ(0, c.cardinality()[False]);

}

TEST(TritTest, LengthTest){
  TritSet d(100);
  EXPECT_EQ(0, d.length());
  d[56] = False;
  EXPECT_EQ(57, d.length());
}

TEST(TritTest, EqualTest){
  TritSet f(18);
  f[0] = True;
  f[4] = True;
  f[5] = False;
  f[11] = False;
  EXPECT_EQ(1, f[5] == f[11]);
  EXPECT_EQ(1, f[5] == False);
  EXPECT_EQ(1, f[0] == f[4]);
  EXPECT_EQ(1, f[17] == Unknown);
  EXPECT_EQ(0, f[0] == f[11]);
  EXPECT_EQ(0, f[2] == f[5]);
  EXPECT_EQ(0, f[10] == f[4]);
  EXPECT_EQ(0, f[7] == True);
}

TEST(TritTest, OpIntTest){
  TritSet u(23);
  u[7] = True;
  u[17] = False;
  EXPECT_EQ(True, u[7]);
  EXPECT_EQ(False, u[17]);
  EXPECT_EQ(Unknown, u[22]);
}
