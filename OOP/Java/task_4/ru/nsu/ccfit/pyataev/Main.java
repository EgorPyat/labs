package ru.nsu.ccfit.pyataev;

import ru.nsu.ccfit.pyataev.factory.Factory;
import ru.nsu.ccfit.pyataev.gui.SliderTestFrame;

public class Main{
  public static void main(String[] args){
    Factory factory = Factory.getInstance();
    factory.getWorkPlan("production.conf");
    SliderTestFrame.start(factory);
    factory.startWork();
  }
}
