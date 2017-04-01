package ru.nsu.ccfit.pyataev.logo.area;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PositionTest{
  @Test
  public void setGetTest(){
    Position pos = new Position();
    assertEquals(pos.getX(), 0);
    assertEquals(pos.getY(), 0);
    pos.setX(10);
    pos.setY(2);
    assertEquals(pos.getX(), 10);
    assertEquals(pos.getY(), 2);
  }
}
