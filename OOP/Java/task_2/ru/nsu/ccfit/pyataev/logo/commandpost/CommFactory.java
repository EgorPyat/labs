package ru.nsu.ccfit.pyataev.logo.commandpost;

import ru.nsu.ccfit.pyataev.logo.auxunits.ClassHunter;
import java.util.*;

public class CommFactory{
  private Map<String, Class> commands = new HashMap<String, Class>();
  private ClassHunter agent = new ClassHunter();

  public CommInterface create(String name) throws InstantiationException, ClassNotFoundException, IllegalAccessException{
    Object comm;

    if(!commands.containsKey(name)){
      commands.put(name, Class.forName(agent.find(name)));
    }

    comm = commands.get(name).newInstance();

    return (CommInterface)comm;
  }

}
