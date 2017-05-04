package ru.nsu.ccfit.pyataev.factory.worker;

import java.util.*;

import ru.nsu.ccfit.pyataev.threadpool.Executor;
import ru.nsu.ccfit.pyataev.factory.storage.Storage;
import ru.nsu.ccfit.pyataev.factory.detail.Body;
import ru.nsu.ccfit.pyataev.factory.detail.Engine;
import ru.nsu.ccfit.pyataev.factory.detail.Accessory;
import ru.nsu.ccfit,pyataev.factory.detail.Car;

public class Worker implements Executor{
  private List taskQueue;

  private Storage<Body> bst;
  private Body body;

  private Storage<Engine> est;
  private Engine engine;

  private Storage<Accessory> ast;
  private Accessory accessory;

  private Storage<Car> cst;

  public Worker(Storage<Body> bst, Storage<Engine> est, Storage<Accessory> ast, Storage<Car> cst){
    this.bst = bst;
    this.est = est;
    this.ast = ast;
    this.cst = cst;
  }

  public void addTaskQueue(List list){
    this.taskQueue = list;
  }

  private void performTask(ThreadPoolTask t){
    t.prepare();
    try{
      //  t.go();
       body = bst.get();
       engine = est.get();
       accessory = ast.get();

       Car car = new Car();
       System.out.println("Create car: " + car.getName() + " | Engine: " + engine.getName() + " | Body: " + body.getName() + " | Accessory: " + accessory.getName());

       cst.put(car);
    }
    catch(InterruptedException ex) {
       t.interrupted();
    }
    t.finish();
  }

  @Override
  public void run(){
    ThreadPoolTask toExecute = null;
    while(true){
     synchronized(taskQueue){
        if(taskQueue.isEmpty()){
           try{
              taskQueue.wait();
           }
           catch(InterruptedException ex){
              System.err.println("Thread was interrupted:"+getName());
           }

           continue;
        }
        else{
           toExecute = (ThreadPoolTask)taskQueue.remove(0);
        }
     }
     System.out.println(getName() + " got the job: "+toExecute.getName());
     performTask(toExecute);
   }
 }
}
