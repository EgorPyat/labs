package ru.nsu.ccfit.pyataev.factory.storage;

import ru.nsu.ccfit.pyataev.factory.detail.Detail;
import ru.nsu.ccfit.pyataev.factory.controller.CarStorageController;
import java.lang.reflect.Array;

public class ControlledStorage<T extends Detail> implements Storagable<T>{
  private T[] details;
  private int capacity;
  private int detailsAmount;
  private int orders = 0;
  private CarStorageController csc;

  public ControlledStorage(Class detail, int capacity){
    this.detailsAmount = 0;
    this.capacity = capacity;
    this.details = createPlace(detail, this.capacity);
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

    notifyAll();

    return detail;
  };

  public void makeOrder(){
    synchronized(this){
      ++orders;
    }
  }

  public synchronized void put(T detail) throws InterruptedException{
      while(this.detailsAmount == this.capacity){
          wait();
      }

      this.details[this.detailsAmount] = detail;

      ++detailsAmount;

      notifyAll();
  }

  private T[] createPlace(Class clazz, int size){
    @SuppressWarnings("unchecked")
    T[] place = (T[])Array.newInstance(clazz, size);
    return place;
  }

  public void addController(CarStorageController csc){
    this.csc = csc;
  }

  public boolean orderCondition(){
    if(orders > detailsAmount) return true;
    else return false;
  }
}
