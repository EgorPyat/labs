package ru.nsu.ccfit.pyataev.logo.commandpost.commands;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import ru.nsu.ccfit.pyataev.logo.commandpost.CommInterface;
import ru.nsu.ccfit.pyataev.logo.area.Field;
import ru.nsu.ccfit.pyataev.logo.area.Position;

/**
  * class for AE moving
  * @author EgorPyat
  */

public class Move implements CommInterface{
  private static final Logger logger = LogManager.getLogger(Move.class);
  /**
    * Move AE
    * @param args command args
    * @param field link to programm field
    * @exception IllegalArgumentException
    * @exception IllegalStateException
    */
  public void doJob(String[] args, Field field) throws IllegalArgumentException, IllegalStateException{
    if(!field.isExist()) throw new IllegalStateException("Field isn't already exist!");
    if(args.length != 2) throw new IllegalArgumentException("Not enough args!");
    int steps = Integer.parseInt(args[1]);
    if(steps < 0) throw new IllegalArgumentException("Bad way length!");
    Position playerPos = field.getPlayerPos();
    Position fieldSize = field.getFieldSize();
    boolean draw = field.isDraw();

    if(args[0].equals("L")){
      for(int i = 0; i < steps; i++){
        if(draw == true) field.setPositionMark(playerPos, true);

        playerPos.setX((playerPos.getX()) - 1);
        if(playerPos.getX() < 0) playerPos.setX((fieldSize.getX()) - 1);
      }
    }
    else if(args[0].equals("R")){
      for(int i = 0; i < steps; i++){
        if(draw == true) field.setPositionMark(playerPos, true);

        playerPos.setX((playerPos.getX()) + 1);
        if(playerPos.getX() == fieldSize.getX()) playerPos.setX(0);
      }
    }
    else if(args[0].equals("U")){
      for(int i = 0; i < steps; i++){
        if(draw == true) field.setPositionMark(playerPos, true);

        playerPos.setY((playerPos.getY()) - 1);
        if(playerPos.getY() < 0) playerPos.setY((fieldSize.getY()) - 1);
      }
    }
    else if(args[0].equals("D")){
      for(int i = 0; i < steps; i++){
        if(draw == true) field.setPositionMark(playerPos, true);

        playerPos.setY((playerPos.getY()) + 1);
        if(playerPos.getY() == fieldSize.getY()) playerPos.setY(0);
      }
    }
    else throw new IllegalArgumentException("Bad way arg!");
    logger.info("AE MOVE to " + args[0] + " by " + steps + " steps!");
  }
}
