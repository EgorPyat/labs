package ru.nsu.ccfit.pyataev.factory.dealer;

import ru.nsu.ccfit.pyataev.factory.storage.ControlledStorage;
import ru.nsu.ccfit.pyataev.factory.detail.Car;

public class Dealer implements Runnable{
  private ControlledStorage<Car> storage;
  private static int TIME = 10000;

  public Dealer(ControlledStorage<Car> storage){
    this.storage = storage;
  }

  @Override
  public void run(){
    System.out.println(Thread.currentThread().getName() + " started.");
    while(true){
      try{
        Thread.sleep(TIME);
        this.storage.makeOrder();
        System.out.println(Thread.currentThread().getName() + " made order.");
        Car c = this.storage.get();
        System.out.println(Thread.currentThread().getName() + " get a car " + c + ".");
      }
      catch(InterruptedException e){
        System.out.println("Oops! " + e.getMessage());
      }
    }
  }

  public static void setTime(int time){
    TIME = time;
  }
}
