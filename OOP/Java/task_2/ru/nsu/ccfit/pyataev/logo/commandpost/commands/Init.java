package ru.nsu.ccfit.pyataev.logo.commandpost.commands;

import ru.nsu.ccfit.pyataev.logo.commandpost.CommInterface;
import ru.nsu.ccfit.pyataev.logo.area.Field;
import ru.nsu.ccfit.pyataev.logo.area.Position;

public class Init implements CommInterface{
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
    System.out.println("init");
  }
}
