package ru.nsu.ccfit.pyataev.factory.controller;

import ru.nsu.ccfit.pyataev.factory.storage.ControlledStorage;
import ru.nsu.ccfit.pyataev.threadpool.ThreadPool;
import ru.nsu.ccfit.pyataev.factory.detail.Car;
import ru.nsu.ccfit.pyataev.threadpool.Task;

public class CarStorageController implements Runnable{
  private ControlledStorage<Car> storage;
  private ThreadPool tp;
  private boolean isSuspended = false;

  public CarStorageController(ControlledStorage<Car> storage, ThreadPool tp){
    this.storage = storage;
    this.tp = tp;
  }

  @Override
  public void run(){
    System.out.println(Thread.currentThread().getName() + " started.");

    while(true){
      synchronized(this){
        while(isSuspended == true){
          try{
            wait();
          }
          catch(InterruptedException e){
            System.out.println("Oops! " + e.getMessage());
          }
        }
      }
        if(storage.orderCondition() == true){
          for(int i = 0; i < (storage.getOrders() > tp.getTasksNum() ? (storage.getOrders() - tp.getTasksNum()) : 0) + 1; i++){
            tp.addTask(new Task(){
              @Override
              public String getName(){
                return "TASK!";
              }
              @Override
              public void performWork() throws InterruptedException{
                System.out.println("Work done!");
              }
            });
          }
          this.ssuspend();
        }
        else{
          this.ssuspend();
        }
    }

  }

  public void analyze(){
    this.rresume();
  }

  private void ssuspend(){
    this.isSuspended = true;
    System.out.println("Controller suspended.");
  }

  private void rresume(){
    this.isSuspended = false;
    synchronized(this){
      this.notify();
    }
    System.out.println("Controller resumed.");
  }
}
