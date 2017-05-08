package ru.nsu.ccfit.pyataev;

import ru.nsu.ccfit.pyataev.factory.Factory;

public class Main{
  public static void main(String[] args){
    Factory factory = Factory.getInstance();
    factory.getWorkPlan("production.conf");
    factory.startWork();
  }
}
