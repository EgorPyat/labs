package ru.nsu.ccfit.pyataev.logo.auxunits;

import java.io.*;
import java.util.*;

public class ClassHunter{
  public String find(String name){
    Properties prop = new Properties();
    String path = null;

    try
    (
      InputStream inp = ClassLoader.getSystemResourceAsStream("commands.conf");
    )
    {
      prop.load(inp);
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
