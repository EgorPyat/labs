package ru.nsu.ccfit.pyataev.logo.commandpost.commands;

import ru.nsu.ccfit.pyataev.logo.area.Field;
import ru.nsu.ccfit.pyataev.logo.area.Position;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MoveTest{
  @Test(expected = IllegalStateException.class)
  public void stateExceptTest(){
    Move mv = new Move();
    Field field = new Field();
    String[] args = {"D", "10"};
    mv.doJob(args, field);
  }

  @Test(expected = IllegalArgumentException.class)
  public void argsExceptTest(){
    Move mv = new Move();
    Field field = new Field();
    Position pos = new Position();
    pos.setX(4);
    pos.setY(1);
    Position size = new Position();
    size.setX(7);
    size.setY(6);
    field.create(pos, size);
    String[] args = { "10"};
    mv.doJob(args, field);
  }

  @Test
  public void moveTest(){
    Move mv = new Move();
    Field field = new Field();
    Position pos = new Position();
    pos.setX(4);
    pos.setY(1);
    Position size = new Position();
    size.setX(7);
    size.setY(6);
    field.create(pos, size);
    String[] args = {"D", "3"};
    mv.doJob(args, field);
    assertEquals(field.getPlayerPos().getY(), 4);
  }
}
