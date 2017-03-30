package ru.nsu.ccfit.pyataev.logo.commandpost;

import ru.nsu.ccfit.pyataev.logo.auxunits.ClassHunter;
import java.util.*;
import java.io.*;

/**
  * Class for creating commads objects
  * @author EgorPyat
  */

public class CommFactory{
  private Map<String, Class> commands = new HashMap<String, Class>();
  private ClassHunter agent = new ClassHunter();

  /**
    * Creates command objects
    * @param name Command name
    * @return CommandInterface for command
    * @exception InstantiationException
    * @exception ClassNotFoundException
    * @exception IllegalAccessException
    * @exception IOException
    * @exception NullPointerException
    */
  public CommInterface create(String name) throws InstantiationException, ClassNotFoundException, IllegalAccessException, IOException, NullPointerException{
    Object comm;

    if(!commands.containsKey(name)){
      commands.put(name, Class.forName(agent.find(name)));
    }

    comm = commands.get(name).newInstance();

    return (CommInterface)comm;
  }

}
