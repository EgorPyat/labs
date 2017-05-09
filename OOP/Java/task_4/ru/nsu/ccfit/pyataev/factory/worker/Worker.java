package ru.nsu.ccfit.pyataev.factory.worker;

import java.util.*;

import ru.nsu.ccfit.pyataev.threadpool.Executor;
import ru.nsu.ccfit.pyataev.threadpool.ThreadPoolTask;

import ru.nsu.ccfit.pyataev.factory.storage.Storagable;
import ru.nsu.ccfit.pyataev.factory.detail.Body;
import ru.nsu.ccfit.pyataev.factory.detail.Engine;
import ru.nsu.ccfit.pyataev.factory.detail.Accessory;
import ru.nsu.ccfit.pyataev.factory.detail.Car;

public class Worker extends Executor{
  private List<ThreadPoolTask> taskQueue;

  private Storagable<Body> bst;
  private Body body;

  private Storagable<Engine> est;
  private Engine engine;

  private Storagable<Accessory> ast;
  private Accessory accessory;

  private Storagable<Car> cst;

  public Worker(Storagable<Body> bst, Storagable<Engine> est, Storagable<Accessory> ast, Storagable<Car> cst){
    this.bst = bst;
    this.est = est;
    this.ast = ast;
    this.cst = cst;
  }

  public void addTaskQueue(List<ThreadPoolTask> list){
    this.taskQueue = list;
  }

  public void performTask(ThreadPoolTask t){
    t.prepare();
    try{
       body = bst.get();
       engine = est.get();
       accessory = ast.get();

       Car car = new Car();
       System.out.println(Thread.currentThread().getName() + " made car: " + car.getName() + " | Engine: " + engine.getName() + " | Body: " + body.getName() + " | Accessory: " + accessory.getName());
       cst.put(car);
       System.out.println(Thread.currentThread().getName() + " put car: " + car.getName() + " in: " + cst);

    }
    catch(InterruptedException ex) {
       t.interrupted();
    }
    t.finish();
  }

  @Override
  public void run(){
    System.out.println(Thread.currentThread().getName() + " started.");
    ThreadPoolTask toExecute = null;
    while(true){
     synchronized(taskQueue){
        if(taskQueue.isEmpty()){
           try{
              taskQueue.wait();
           }
           catch(InterruptedException ex){
              System.err.println("Thread was interrupted:" + Thread.currentThread().getName());
           }

           continue;
        }
        else{
           toExecute = taskQueue.remove(0);
        }
     }
     System.out.println(Thread.currentThread().getName() + " got the job: " + toExecute.getName());
     performTask(toExecute);
   }
 }
}
