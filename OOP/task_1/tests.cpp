#include "header.h"

TEST(TritTest, CapacityTest) {
  TritSet a(1000);
  EXPECT_EQ(1000, a.capacity());
}

TEST(TritTest, AssigmentTest){
  TritSet b(10);
  b[100] = True;
  EXPECT_EQ(True, b[100]);
  EXPECT_EQ(101, b.capacity());
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
  d[56] = False;
  EXPECT_EQ(57, d.length());
}

TEST(TritSet, EqualTest){
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
