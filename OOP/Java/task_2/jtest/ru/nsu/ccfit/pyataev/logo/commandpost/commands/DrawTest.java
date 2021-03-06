package ru.nsu.ccfit.pyataev.logo.commandpost.commands;

import ru.nsu.ccfit.pyataev.logo.area.Field;
import ru.nsu.ccfit.pyataev.logo.area.Position;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DrawTest{
  @Test(expected = IllegalStateException.class)
  public void isDrawTest(){
    String[] arr = new String[0];
    Draw dr = new Draw();
    Field field = new Field();
    dr.doJob(arr, field);
    Position pos = new Position();
    pos.setX(4);
    pos.setY(1);
    Position size = new Position();
    size.setX(7);
    size.setY(6);
    field.create(pos, size);
    dr.doJob(arr, field);
    assertTrue(field.isDraw());
  }
}
