package ru.nsu.ccfit.pyataev.factory.detail;

import java.util.concurrent.atomic.AtomicInteger;

public class Engine extends Detail{
  private static final AtomicInteger NEXT_ID = new AtomicInteger(0);
  private final int id;

  public Engine(){
    this.id = NEXT_ID.getAndIncrement();
  }

  public String getName(){
    return "E#" + id;
  }

  public int getID(){
    return this.id;
  }
}
