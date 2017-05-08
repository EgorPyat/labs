package ru.nsu.ccfit.pyataev.threadpool;

import java.util.*;

public abstract class Executor implements Runnable{
  public abstract void addTaskQueue(List<ThreadPoolTask> list);
  public abstract void performTask(ThreadPoolTask t);
}
