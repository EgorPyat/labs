package ru.nsu.ccfit.pyataev.logo.commandpost.commands;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import ru.nsu.ccfit.pyataev.logo.commandpost.CommInterface;
import ru.nsu.ccfit.pyataev.logo.area.Field;

/**
  * Class for drawing
  * @author EgorPyat
  */

public class Ward implements CommInterface{
  private static final Logger logger = LogManager.getLogger(Ward.class);
  /**
    * Make ability for drawing
    * @param args command args
    * @param field link to programm field
    * @exception IllegalArgumentException
    * @exception IllegalStateException
    */
  public void doJob(String[] args, Field field) throws IllegalArgumentException, IllegalStateException{
    if(!field.isExist()) throw new IllegalStateException("Field isn't already exist!");
    if(args.length > 0) throw new IllegalArgumentException("Too many args!");
    field.setDraw(false);
    logger.info("Field draw is FALSE now!");
  }
}
