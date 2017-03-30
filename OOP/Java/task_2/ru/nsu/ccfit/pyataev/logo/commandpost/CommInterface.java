package ru.nsu.ccfit.pyataev.logo.commandpost;

import ru.nsu.ccfit.pyataev.logo.area.Field;

/**
  * Interface for program commands
  * @author EgorPyat
  */

public interface CommInterface{
  /**
    * Execute command
    * @param args command arguments
    * @param field link to program field
    * @exception IllegalArgumentException
    * @exception IllegalStateException
    */
  public void doJob(String[] args, Field field) throws IllegalArgumentException, IllegalStateException;
}
