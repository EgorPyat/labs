package ru.nsu.ccfit.pyataev.logo.commandpost.commands;

import ru.nsu.ccfit.pyataev.logo.commandpost.CommInterface;
import ru.nsu.ccfit.pyataev.logo.area.Field;
import ru.nsu.ccfit.pyataev.logo.area.Position;

class Init implements CommInterface{
  public void doJob(String[] args, Field field){
    Position fieldSize = new Position();
    fieldSize.setX(Integer.parseInt(args[0]));
    fieldSize.setY(Integer.parseInt(args[1]));

    Position playerPos = new Position();
    fieldSize.setX(Integer.parseInt(args[2]));
    fieldSize.setY(Integer.parseInt(args[3]));

    field.create(playerPos, fieldSize);
  }
}
