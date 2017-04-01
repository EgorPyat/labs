package ru.nsu.ccfit.pyataev.logo.commandpost.commands;

import ru.nsu.ccfit.pyataev.logo.area.Field;
import ru.nsu.ccfit.pyataev.logo.area.Position;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class InitTest{
  @Test(expected = IllegalArgumentException.class)
  public void initTest(){
    Field field = new Field();
    Init init = new Init();
    String[] args = {"f", "f", "f", "f"};
    init.doJob(args, field);
  }
  @Test(expected = IllegalStateException.class)
  public void initTest2(){
    Field field = new Field();
    Position pos = new Position();
    pos.setX(4);
    pos.setY(1);
    Position size = new Position();
    size.setX(7);
    size.setY(6);
    field.create(pos, size);
    Init init = new Init();
    String[] args = {"2", "2", "0", "0"};
    init.doJob(args, field);
  }
}
