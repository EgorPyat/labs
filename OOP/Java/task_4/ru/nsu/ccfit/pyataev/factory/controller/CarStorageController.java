package ru.nsu.ccfit.pyataev.factory.controller;

import ru.nsu.ccfit.pyataev.factory.storage.Storage;
import ru.nsu.ccfit.pyataev.threadpool.ThreadPool;

public class CarStorageController implements Runnable{
  private Storage<Car> storage;
  private ThreadPool tp;

  public CarStorageController(Storage<Car> storage, ThreadPool tp){
    this.storage = storage;
    this.tp = tp;
  }

  @Override
  public void run(){
    while(true){
      if(sended != true){
        continue;
      }
      else{
        
      }
    }
  }
}
