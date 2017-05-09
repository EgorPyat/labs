package ru.nsu.ccfit.pyataev.factory.provider;

import ru.nsu.ccfit.pyataev.factory.storage.Storagable;
import ru.nsu.ccfit.pyataev.factory.detail.Accessory;

public class AccessoryProvider extends Provider{
  private Storagable<Accessory> storage;
  private static int TIME = 5000;

  public AccessoryProvider(Storagable<Accessory> storage){
    this.storage = storage;
  }

  @Override
  public void run(){
    System.out.println(Thread.currentThread().getName() + " started.");
    while(true){
      try{
        Thread.sleep(TIME);
        Accessory a = new Accessory();
        this.storage.put(a);
        System.out.println(Thread.currentThread().getName() + " puts a detail: " + a + " to: " + this.storage + ".");
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
