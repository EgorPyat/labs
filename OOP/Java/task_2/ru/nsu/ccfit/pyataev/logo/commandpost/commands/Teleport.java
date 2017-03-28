package ru.nsu.ccfit.pyataev.logo.commandpost.commands;

import ru.nsu.ccfit.pyataev.logo.commandpost.CommInterface;
import ru.nsu.ccfit.pyataev.logo.area.Field;
import ru.nsu.ccfit.pyataev.logo.area.Position;

class Teleport implements CommInterface{
  public void doJob(String[] args, Field field){
    Position newPos = new Position();

    newPos.setX(Integer.parseInt(args[0]));
    newPos.setY(Integer.parseInt(args[1]));

    field.setPlayerPos(newPos);
  }
}
