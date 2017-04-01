package ru.nsu.ccfit.pyataev.logo.area;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FieldTest{
  @Test
  public void isExistTest(){
    Field field = new Field();
    assertTrue(!field.isExist());
    Position pos = new Position();
    pos.setX(4);
    pos.setY(1);
    Position size = new Position();
    size.setX(7);
    size.setY(6);
    field.create(pos, size);
    assertTrue(field.isExist());
  }

  @Test
  public void isDrawTest(){
    Field field = new Field();
    assertTrue(!field.isDraw());
    field.setDraw(true);
    assertTrue(field.isDraw());
    field.setDraw(false);
    assertTrue(!field.isDraw());
  }

  @Test(expected = IllegalArgumentException.class)
  public void setGetPlayerPosCreateTest(){
    Field field = new Field();
    Position pos = new Position();
    pos.setX(4);
    pos.setY(1);
    Position size = new Position();
    size.setX(7);
    size.setY(6);
    field.create(pos, size);
    assertEquals(field.getFieldSize().getX(), size.getX());
    assertEquals(field.getFieldSize().getY(), size.getY());
    pos.setX(0);
    pos.setY(0);
    field.setPlayerPos(pos);
    assertEquals(field.getPlayerPos().getX(), pos.getX());
    assertEquals(field.getPlayerPos().getY(), pos.getY());
    pos.setY(-10);
    field.setPlayerPos(pos);
  }

  @Test
  public void positionMarkTest(){
    Field field = new Field();
    Position pos = new Position();
    pos.setX(4);
    pos.setY(1);
    Position size = new Position();
    size.setX(7);
    size.setY(6);
    field.create(pos, size);
    Position pos2 = new Position();
    pos2.setX(2);
    pos2.setY(3);
    field.setPositionMark(pos2, true);
  }
}
