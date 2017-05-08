package ru.nsu.ccfit.pyataev.factory.storage;

import ru.nsu.ccfit.pyataev.factory.detail.Detail;
import ru.nsu.ccfit.pyataev.factory.controller.CarStorageController;
import java.lang.reflect.Array;
import java.io.*;
import java.time.LocalTime;

public class ControlledStorage<T extends Detail> implements Storagable<T>{
  private T[] details;
  private int capacity;
  private int detailsAmount;
  private int orders = 0;
  private CarStorageController csc;
  private String name;

  private boolean logger;
  private BufferedWriter log;

  public ControlledStorage(Class<T> detail, int capacity, boolean logger){
    this.detailsAmount = 0;
    this.capacity = capacity;
    this.details = createPlace(detail, this.capacity);
    this.name = detail.toString().substring(42) + " storage";

    this.logger = logger;
  }

  public synchronized T get() throws InterruptedException{
    while(this.detailsAmount == 0){
        wait();
    }

    --detailsAmount;

    T detail = this.details[this.detailsAmount];

    this.details[this.detailsAmount] = null;

    --orders;

    csc.analyze();
    System.out.println(this + " analyzed.");

    if(this.logger == true){
      try{
        this.log = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("logger.log", true)));
        this.log.write(LocalTime.now().toString() + " " + Thread.currentThread().getName() + " " + detail.toString() + "\n");
        this.log.close();
      }
      catch(IOException e){
        System.out.println(e.getMessage());
      }
    }

    notifyAll();

    return detail;
  };

  public synchronized void makeOrder(){
      ++this.orders;
  }

  public synchronized int getOrders(){
    int r = orders - detailsAmount;
    return r > 0 ? r : 0;
  }

  public synchronized void put(T detail) throws InterruptedException{
      while(this.detailsAmount == this.capacity){
          wait();
      }

      this.details[this.detailsAmount] = detail;

      ++detailsAmount;
      System.out.println(this + " got car: " + detail + ".");
      notifyAll();
  }

  private synchronized T[] createPlace(Class<T> clazz, int size){
    @SuppressWarnings("unchecked")
    T[] place = (T[])Array.newInstance(clazz, size);
    return place;
  }

  public void addController(CarStorageController csc){
    this.csc = csc;
  }

  public synchronized boolean orderCondition(){
    if(orders > detailsAmount) return true;
    if(orders == 0 && detailsAmount == 0) return true;
    else return false;
  }

  @Override
  public String toString(){
    return this.name;
  }
}
