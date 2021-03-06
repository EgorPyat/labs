package ru.nsu.ccfit.pyataev.logo.commandpost.commands;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import ru.nsu.ccfit.pyataev.logo.commandpost.CommInterface;
import ru.nsu.ccfit.pyataev.logo.area.Field;
import ru.nsu.ccfit.pyataev.logo.area.Position;

/**
  * Class for moving AE
  * @author EgorPyat
  */

public class Init implements CommInterface{
  private static final Logger logger = LogManager.getLogger(Init.class);
  /**
    * Create program field
    * @param args command args
    * @param field link to programm field
    * @exception IllegalArgumentException
    * @exception IllegalStateException
    */
  public void doJob(String[] args, Field field) throws IllegalArgumentException, IllegalStateException{
    if(field.isExist()) throw new IllegalStateException("Field is already exist!");
    if(args.length != 4) throw new IllegalArgumentException("Bad args amount!");
    Position fieldSize = new Position();
    fieldSize.setY(Integer.parseInt(args[0]));
    fieldSize.setX(Integer.parseInt(args[1]));

    Position playerPos = new Position();
    playerPos.setY(Integer.parseInt(args[2]));
    playerPos.setX(Integer.parseInt(args[3]));

    field.create(playerPos, fieldSize);
    logger.info("Field created with X: " + fieldSize.getX() + " Y: " + fieldSize.getY() + " !");
    logger.info("AE created with X: " + playerPos.getX() + " Y: " + playerPos.getY() + " !");
  }
}
