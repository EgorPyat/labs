package ru.nsu.ccfit.pyataev.logo.auxunits;

import java.io.*;
import java.util.*;

public class ClassHunter{
  public String find(String name) throws IOException, NullPointerException{
    Properties prop = new Properties();
    String path = null;

    try
    (
    InputStream inp = ClassLoader.getSystemResourceAsStream("commands.conf");
    )
    {
      if(inp == null) throw new IOException("Problems with config!");
      prop.load(inp);
      for(String pr : prop.stringPropertyNames()){
        if(pr.equals(name)){
          path = prop.getProperty(pr);
          break;
        }
      }
    }
    catch(IOException e){
      throw new IOException("Problems with config!", e);
    }

    if(path == null) throw new NullPointerException("No such class!");

    return path;
  }
}
