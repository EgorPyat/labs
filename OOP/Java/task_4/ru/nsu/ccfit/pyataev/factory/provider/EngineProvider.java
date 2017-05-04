package ru.nsu.ccfit.pyataev.factory.provider;

import ru.nsu.ccfit.pyataev.factory.storage.Storage;
import ru.nsu.ccfit.pyataev.factory.detail.Engine;

public class EngineProvider extends Provider{
  private Storage<Engine> storage;

  public EngineProvider(Storage<Engine> storage){
    this.storage = storage;
  }

  @Override
  public void run(){
    this.storage.put(new Engine());
  }
}
