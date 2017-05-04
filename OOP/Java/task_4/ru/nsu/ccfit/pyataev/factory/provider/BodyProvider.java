package ru.nsu.ccfit.pyataev.factory.provider;

import ru.nsu.ccfit.pyataev.factory.storage.Storage;
import ru.nsu.ccfit.pyataev.factory.detail.Body;

public class BodyProvider extends Provider{
  private Storage<Body> storage;

  public BodyProvider(Storage<Engine> storage){
    this.storage = storage;
  }

  @Override
  public void run(){
    this.storage.put(new Body());
  }
}
