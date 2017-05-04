package ru.nsu.ccfit.pyataev.factory.storage;

import ru.nsu.ccfit.pyataev.factory.detail.Detail;
import java.lang.reflect.Array;

public class Storage<T extends Detail>{
  private T[] details;
  private int capacity;
  private int detailsAmount;

  public Storage(Class detail, int capacity){
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

  private T[] createPlace(Class clazz, int size){
    @SuppressWarnings("unchecked")
    T[] place = (T[])Array.newInstance(clazz, size);
    return place;
  }
}
