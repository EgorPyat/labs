package ru.nsu.ccfit.pyataev.logo;

import ru.nsu.ccfit.pyataev.logo.commandpost.CommFactory;
import ru.nsu.ccfit.pyataev.logo.commandpost.CommInterface;
import ru.nsu.ccfit.pyataev.logo.area.Field;
import ru.nsu.ccfit.pyataev.logo.auxunits.Transmitter;

import java.io.*;
import java.util.*;

/**
  * Main class of LogoWorld Interpreter program
  * @author EgorPyat
  */

public class Interpreter{
  /**
    * Main method of LogoWorld Interpreter program for starting program
    * @param argv cmd args
    */
  public static void main(String[] argv){
    Field field = new Field();
    CommFactory factory = new CommFactory();
    Scanner scan = new Scanner(System.in);

    while(true){
      try{
        Transmitter commDiler = new Transmitter(scan.nextLine());
        if(commDiler.getName().equals("END")) return;
        CommInterface command = factory.create(commDiler.getName());
        command.doJob(commDiler.getArgs(), field);
        field.print();
      }
      catch(ClassNotFoundException e){
        System.out.println(e.getMessage());
      }
      catch(InstantiationException e){
        System.out.println(e.getMessage());
      }
      catch(IllegalAccessException e){
        System.out.println(e.getMessage());
      }
      catch(IllegalArgumentException e){
        System.out.println(e.getMessage());
      }
      catch(IllegalStateException e){
        System.out.println(e.getMessage());
      }
      catch(IOException e){
        System.out.println(e.getMessage());
      }
      catch(NullPointerException e){
        System.out.println(e.getMessage());
      }
    }

  }
}
