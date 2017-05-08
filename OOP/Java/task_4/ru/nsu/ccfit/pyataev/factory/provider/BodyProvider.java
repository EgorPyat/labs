package ru.nsu.ccfit.pyataev.factory.provider;

import ru.nsu.ccfit.pyataev.factory.storage.Storagable;
import ru.nsu.ccfit.pyataev.factory.detail.Body;

public class BodyProvider extends Provider{
  private Storagable<Body> storage;

  public BodyProvider(Storagable<Body> storage){
    this.storage = storage;
  }

  @Override
  public void run(){
    System.out.println(Thread.currentThread().getName() + " started.");
    while(true){
      try{
        Thread.sleep(2000);
        Body b = new Body();
        this.storage.put(b);
        System.out.println(Thread.currentThread().getName() + " puts a detail: " + b + " to: " + this.storage + ".");
      }
      catch(InterruptedException e){
        System.out.println("Oops! " + e.getMessage());
      }
    }
  }
}
