package ru.nsu.ccfit.pyataev.logo.commandpost.commands;

import ru.nsu.ccfit.pyataev.logo.area.Field;
import ru.nsu.ccfit.pyataev.logo.area.Position;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TeleportTest{
  @Test(expected = IllegalArgumentException.class)
  public void telepTest(){
    Field field = new Field();
    Teleport tele = new Teleport();
    Position pos = new Position();
    pos.setX(4);
    pos.setY(1);
    Position size = new Position();
    size.setX(7);
    size.setY(6);
    field.create(pos, size);
    String[] args = {"f", "y"};
    tele.doJob(args, field);
  }
  
  @Test(expected = IllegalStateException.class)
  public void telepTest2(){
    Field field = new Field();
    Teleport tele = new Teleport();
    String[] args = {"2", "2"};
    tele.doJob(args, field);
  }
}
