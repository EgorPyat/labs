package ru.nsu.ccfit.pyataev.logo;

import ru.nsu.ccfit.pyataev.logo.commandpost.CommFactory;
import ru.nsu.ccfit.pyataev.logo.commandpost.CommInterface;
import ru.nsu.ccit.pyataev.logo.area.Field;
import java.io.*;
import java.util.*;

class Interpreter{
  public static void main(String[] argv){
    Field field = new Field();
    CommFactory factory = new CommFactory();
    Scanner scan = new Scanner(System.in);

    while(true){
      Transmitter commDiler = new Transmitter(scan.nextLine());
      CommInterface command = factory.create(commDiler.getName());
      command.doJob(commDiler.getArgs(), field);
      field.print();
    }

  }
}
