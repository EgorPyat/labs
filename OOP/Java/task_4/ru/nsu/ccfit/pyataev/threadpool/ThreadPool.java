package ru.nsu.ccfit.pyataev.threadpool;

import java.util.*;
import ru.nsu.ccfit.pyataev.factory.worker.Worker;

public class ThreadPool implements TaskListener{
   private static final int threadsNum;
   private List taskQueue = new LinkedList();
   private Executors[] availableThreads;

   public ThreadPool(int size, Executors[] execs){
     this.threadsNum = size;
     this.availableThreads = execs;

     for(int i = 0; i < threadsNum; i++){
       availableThreads[i].addTaskQueue(taskQueue);
       new Thread(availableThreads[i]).start();
     }
   }

   public void addTask(Task t){
      synchronized(taskQueue){
         taskQueue.add(new ThreadPoolTask(t, this));
         taskQueue.notify();
      }
   }

   public void taskStarted(Task t){
     System.out.println("Started: " + t.getName());
   }

   public void taskFinished(Task t){
     System.out.println("Finished: " + t.getName());
   }

   public void taskInterrupted(Task t){
     System.out.println("Interrupted: " + t.getName());
   }
}
