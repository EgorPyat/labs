package ru.nsu.ccfit.pyataev.logo.auxunits;

public class Transmitter{
  private String commName;
  private String[] commArgs;

  public Transmitter(String command){
    String[] strs = command.split(" ");
    this.commName = strs[0];
    String[] newSt = new String[strs.length - 1];
    for(int i = 0; i < strs.length - 1; i++){
      newSt[i] = strs[i + 1];
    }
    this.commArgs = newSt;
  }

  public String getName(){
    return this.commName;
  }

  public String[] getArgs(){
    return this.commArgs;
  }
}
