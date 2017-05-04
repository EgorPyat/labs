package ru.nsu.ccfit.pyataev.factory;

import ru.nsu.ccfit.pyataev.factory.storage.Storage;
import ru.nsu.ccfit.pyataev.factory.detail.Engine;
import ru.nsu.ccfit.pyataev.factory.detail.Body;
import ru.nsu.ccfit.pyataev.threadpool.ThreadPool;

public class Factory{
  public static void main(String[] args) throws InterruptedException{
    Storage<Body> bodyStorage = new Storage<Body>(Body.class, 10);
    Body a = new Body();
    Body q = new Body();
    Engine e = new Engine();
    Engine s = new Engine();
    Engine d = new Engine();

    System.out.println(d.getName());
    bodyStorage.put(a);
    bodyStorage.put(q);

    System.out.println(bodyStorage.get().getName() + " " + bodyStorage.get().getClass());
  }
}
