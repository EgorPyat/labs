package ru.nsu.ccfit.pyataev.threadpool;

public interface Task{
   public String getName();
   public void performWork() throws InterruptedException;
}
