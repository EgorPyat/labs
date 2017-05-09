package ru.nsu.ccfit.pyataev.factory.storage;

import ru.nsu.ccfit.pyataev.factory.detail.Detail;

public interface Storagable<T extends Detail>{
  public T get() throws InterruptedException;
  public void put(T detail) throws InterruptedException;
  public int getDetNum();
  public int getAllNum();
}
