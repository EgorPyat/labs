package ru.nsu.ccfit.pyataev.logo.auxunits;

public class Transmitter{
  private String commName;
  private String[] commArgs;

  public Transmitter(String command){
    this.commName = command[0];
    command.replaceFirst(this.commName + " ", "");
    this.commArgs = command.split(" ");
  }

  public String getName(){
    return this.commName;
  }

  public String[] getArgs(){
    return this.commArgs;
  }
}
