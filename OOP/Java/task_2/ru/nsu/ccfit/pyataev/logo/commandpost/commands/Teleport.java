package ru.nsu.ccfit.pyataev.logo.commandpost.commands;

import ru.nsu.ccfit.pyataev.logo.commandpost.CommInterface;
import ru.nsu.ccfit.pyataev.logo.area.Field;
import ru.nsu.ccfit.pyataev.logo.area.Position;

/**
  * class for AE teleporting
  * @author EgorPyat
  */

public class Teleport implements CommInterface{
  /**
    * Teleport AE
    * @param args command args
    * @param field link to programm field
    * @exception IllegalArgumentException
    * @exception IllegalStateException
    */
  public void doJob(String[] args, Field field) throws IllegalArgumentException, IllegalStateException{
    if(!field.isExist()) throw new IllegalStateException("Field isn't already exist!");
    if(args.length != 2) throw new IllegalArgumentException("Bad args amount!");
    Position newPos = new Position();

    newPos.setX(Integer.parseInt(args[0]));
    newPos.setY(Integer.parseInt(args[1]));

    field.setPlayerPos(newPos);
  }
}
