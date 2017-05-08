package ru.nsu.ccfit.pyataev.factory.storage;

import ru.nsu.ccfit.pyataev.factory.detail.Detail;
import java.lang.reflect.Array;

public class Storage<T extends Detail> implements Storagable<T>{
  private T[] details;
  private int capacity;
  private int detailsAmount;
  private String name;

  public Storage(Class<T> detail, int capacity){
    this.detailsAmount = 0;
    this.capacity = capacity;
    this.details = createPlace(detail, this.capacity);
    this.name = detail.toString().substring(42) + " storage";
  }

  public synchronized T get() throws InterruptedException{
    while(this.detailsAmount == 0){
        wait();
    }

    --detailsAmount;

    T detail = this.details[this.detailsAmount];

    this.details[this.detailsAmount] = null;

    notifyAll();

    return detail;
  };

  public synchronized void put(T detail) throws InterruptedException{
      while(this.detailsAmount == this.capacity){
          wait();
      }

      this.details[this.detailsAmount] = detail;

      ++detailsAmount;

      notifyAll();
  }

  private T[] createPlace(Class<T> clazz, int size){
    @SuppressWarnings("unchecked")
    T[] place = (T[])Array.newInstance(clazz, size);
    return place;
  }

  @Override
  public synchronized String toString(){
    return this.name;
  }
}
