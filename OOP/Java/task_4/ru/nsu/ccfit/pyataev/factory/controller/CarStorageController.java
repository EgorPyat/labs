package ru.nsu.ccfit.pyataev.factory.controller;

import ru.nsu.ccfit.pyataev.factory.storage.ControlledStorage;
import ru.nsu.ccfit.pyataev.threadpool.ThreadPool;

public class CarStorageController implements Runnable{
  private ControlledStorage<Car> storage;
  private ThreadPool tp;
  private isSuspended;

  public CarStorageController(ControlledStorage<Car> storage, ThreadPool tp){
    this.storage = storage;
    this.tp = tp;
  }

  @Override
  public void run(){
    while(true){
      synchronized(this){
        while(isSuspended == true){
          wait();
        }
        if(storage.orderCondition() == true){
          tp.addTask(new Task());
        }
        else{
          this.ssuspend();
        }
      }
    }
  }

  public void analyze(){
    this.rresume();
  }

  private void ssuspend(){
    this.isSuspended = true;
  }

  private void rresume(){
    this.isSuspended = false;
    this.notyfy();
  }
}
