package ru.nsu.ccfit.pyataev.threadpool;

public interface TaskListener{
  public void taskInterrupted(Task t);
  public void taskFinished(Task t);
  public void taskStarted(Task t);
}
