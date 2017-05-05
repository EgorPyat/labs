package ru.nsu.ccfit.pyataev.storage;

import ru.nsu.ccfit.pyataev.detail.Detail;

public interface Storagable<T extends Detail>{
  public T get() throws InterruptedException;
  public void put(T detail) throws InterruptedException;
  private T[] createPlace(Class clazz, int size);
}
