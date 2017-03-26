package ru.nsu.ccfit.pyataev.logo.auxunits;

import java.io.*;
import java.util.*;

public class ClassHunter{
  public String find(String name){
    Properties prop = new Properties();
    ImputStream io;
    String path;

    try
    (
      io = ClassLoader.getResourceAsStream("commands.conf");
    )
    {
      prop.load(io);
      for(String pr : prop.stringPropertyNames()){
        if(pr.equals(name)){
          path = prop.getProperty(pr);
          break;
        }
      }
    }
    catch(IOException e){
      System.err.println("Error while writing file: " + e.getLocalizedMessage());
    }

    return path;
  }
}
