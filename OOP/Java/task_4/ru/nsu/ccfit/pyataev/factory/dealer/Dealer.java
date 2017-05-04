package ru.nsu.ccfit.pyataev.factory.dealer;

import ru.nsu.ccfit.pyataev.factory.storage.Storage;
import ru.nsu.ccfit,pyataev.factory.detail.Car;

public class Dealer implements Runnable{
  private Storage<Car> storage;

  public Dealer(Storage<Car> storage){
    this.storage = storage;
  }
  
  @Override
  public void run(){
    this.storage.get();
  }
}
