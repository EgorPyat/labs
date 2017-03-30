package ru.nsu.ccfit.pyataev.logo.commandpost.commands;

import ru.nsu.ccfit.pyataev.logo.commandpost.CommInterface;
import ru.nsu.ccfit.pyataev.logo.area.Field;

/**
  * Class for undrawing
  * @author EgorPyat
  */
  
public class Draw implements CommInterface{
  /**
    * Make disability for drawing
    * @param args command args
    * @param field link to programm field
    * @exception IllegalArgumentException
    * @exception IllegalStateException
    */
  public void doJob(String[] args, Field field) throws IllegalArgumentException, IllegalStateException{
    if(!field.isExist()) throw new IllegalStateException("Field isn't already exist!");
    if(args.length > 0) throw new IllegalArgumentException("Too many args!");
    field.setDraw(true);
  }
}
