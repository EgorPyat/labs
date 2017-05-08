package ru.nsu.ccfit.pyataev.threadpool;

public class ThreadPoolTask {
   private TaskListener listener;
   private Task task;

   public ThreadPoolTask(Task t, TaskListener l){
      listener = l;
      task = t;
   }

   public void prepare(){
      listener.taskStarted(task);
   }

   public void finish(){
      listener.taskFinished(task);
   }

   public void interrupted(){
      listener.taskInterrupted(task);
   }

   public void go() throws InterruptedException{
      task.performWork();
   }

   public String getName(){
      return task.getName();
   }
}
