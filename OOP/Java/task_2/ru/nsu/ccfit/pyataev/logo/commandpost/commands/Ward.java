package ru.nsu.ccfit.pyataev.logo.commandpost.commands;

import ru.nsu.ccfit.pyataev.logo.commandpost.CommInterface;
import ru.nsu.ccfit.pyataev.logo.area.Field;

public class Ward implements CommInterface{
  public void doJob(String[] args, Field field){
    field.setDraw(false);
  }
}
