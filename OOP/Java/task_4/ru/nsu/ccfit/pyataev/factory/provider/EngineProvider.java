package ru.nsu.ccfit.pyataev.factory.provider;

import ru.nsu.ccfit.pyataev.factory.storage.Storagable;
import ru.nsu.ccfit.pyataev.factory.detail.Engine;

public class EngineProvider extends Provider{
  private Storagable<Engine> storage;
  private static int TIME = 3000;

  public EngineProvider(Storagable<Engine> storage){
    this.storage = storage;
  }

  @Override
  public void run(){
    System.out.println(Thread.currentThread().getName() + " started.");
    while(true){
      try{
        Thread.sleep(TIME);
        Engine e = new Engine();
        this.storage.put(e);
        System.out.println(Thread.currentThread().getName() + " puts a detail: " + e + " to: " + this.storage + ".");
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
