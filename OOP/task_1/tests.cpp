#include "header.h"

TEST(TritTest, CapacityTest) {
  TritSet a(1000);
  EXPECT_EQ(1000, a.capacity());
}

TEST(TritTest, AssigmentTest){
  TritSet b(10);
  b[100] = True;
  EXPECT_EQ(True, b[100]);
}
