package ru.nsu.ccfit.pyataev.logo.auxunits;

/**
  * Class for commands transmissions
  * @author EgorPyat
  */

public class Transmitter{
  private String commName;
  private String[] commArgs;

  /**
    * parse command line
    * @param command command line
    */
  public Transmitter(String command){
    String[] strs = command.split(" ");
    this.commName = strs[0];
    String[] newSt = new String[strs.length - 1];
    for(int i = 0; i < strs.length - 1; i++){
      newSt[i] = strs[i + 1];
    }
    this.commArgs = newSt;
  }
  /**
    * Return command name
    * @return command name
    */
  public String getName(){
    return this.commName;
  }
  /**
    * Return command args
    * @return command args
    */
  public String[] getArgs(){
    return this.commArgs;
  }
}
