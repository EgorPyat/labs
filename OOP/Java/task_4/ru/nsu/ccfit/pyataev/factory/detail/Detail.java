package ru.nsu.ccfit.pyataev.factory.detail;

public abstract class Detail{
  public abstract String getName();
  public abstract int getID();

  @Override
  public String toString(){
    return this.getName();
  }
}
