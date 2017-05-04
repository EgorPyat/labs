package ru.nsu.ccfit.pyataev.factory.detail;

import java.util.concurrent.atomic.AtomicInteger;

public class Body extends Detail{
  private static final AtomicInteger NEXT_ID = new AtomicInteger(0);
  private final int id;

  public Body(){
    this.id = NEXT_ID.getAndIncrement();
  }

  public String getName(){
    return "B#" + id;
  }

  public int getID(){
    return this.id;
  }
}
