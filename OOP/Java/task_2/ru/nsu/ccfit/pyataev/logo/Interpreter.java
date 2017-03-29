package ru.nsu.ccfit.pyataev.logo;

import ru.nsu.ccfit.pyataev.logo.commandpost.CommFactory;
import ru.nsu.ccfit.pyataev.logo.commandpost.CommInterface;
import ru.nsu.ccfit.pyataev.logo.area.Field;
import ru.nsu.ccfit.pyataev.logo.auxunits.Transmitter;

import java.io.*;
import java.util.*;

public class Interpreter{
  public static void main(String[] argv){
    Field field = new Field();
    CommFactory factory = new CommFactory();
    Scanner scan = new Scanner(System.in);

    while(true){
      try{
        System.out.print("> ");
        Transmitter commDiler = new Transmitter(scan.nextLine());
        if(commDiler.getName().equals("END")) return;
        System.out.println("transmit");
        CommInterface command = factory.create(commDiler.getName());
        System.out.println("factory");
        command.doJob(commDiler.getArgs(), field);
        System.out.println("diler");
        field.print();
        System.out.println("print");
      }
      catch(ClassNotFoundException e){
        e.getMessage();
      }
      catch(InstantiationException e){
        e.getMessage();
      }
      catch(IllegalAccessException e){
        e.getMessage();
      }
    }

  }
}
