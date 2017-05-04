package ru.nsu.ccfit.pyataev.threadpool;

public interface Executor implements Runnable{
  public void addTaskQueue(List list);
  private void performTask(ThreadPoolTask t);
  @Override
  public void run();
}
