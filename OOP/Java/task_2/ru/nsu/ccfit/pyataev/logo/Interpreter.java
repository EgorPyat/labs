package ru.nsu.ccfit.pyataev.logo;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

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
  private static final Logger logger = LogManager.getLogger(Interpreter.class);
  /**
    * Main method of LogoWorld Interpreter program for starting program
    * @param argv cmd args
    */
  public static void main(String[] argv){
    logger.info("Start execution!");
    Field field = new Field();
    CommFactory factory = new CommFactory();
    Scanner scan = new Scanner(System.in);

    while(true){
      try{
        logger.info("Getting command!");
        Transmitter commDiler = new Transmitter(scan.nextLine());
        logger.info("Command parsed!");
        if(commDiler.getName().equals("END")){
          logger.info("Program finished!");
          return;
        }
        logger.info("Start command object creating!");
        CommInterface command = factory.create(commDiler.getName());
        logger.info("Command created!");
        logger.info("Run command!");
        command.doJob(commDiler.getArgs(), field);
        logger.info("Command finished!");
        logger.info("Start printing field!");
        field.print();
        logger.info("Printing completed!");
      }
      catch(ClassNotFoundException e){
        logger.warn(e.getMessage());
        System.out.println(e.getMessage());
      }
      catch(InstantiationException e){
        logger.warn(e.getMessage());
        System.out.println(e.getMessage());
      }
      catch(IllegalAccessException e){
        logger.warn(e.getMessage());
        System.out.println(e.getMessage());
      }
      catch(IllegalArgumentException e){
        logger.warn(e.getMessage());
        System.out.println(e.getMessage());
      }
      catch(IllegalStateException e){
        logger.warn(e.getMessage());
        System.out.println(e.getMessage());
      }
      catch(IOException e){
        logger.error(e.getMessage());
        System.out.println(e.getMessage());
      }
      catch(NullPointerException e){
        logger.warn(e.getMessage());
        System.out.println(e.getMessage());
      }
    }

  }
}
