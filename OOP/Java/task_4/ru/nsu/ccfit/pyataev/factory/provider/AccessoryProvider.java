package ru.nsu.ccfit.pyataev.factory.provider;

import ru.nsu.ccfit.pyataev.factory.storage.Storage;
import ru.nsu.ccfit.pyataev.factory.detail.Accessory;

public class AccessoryProvider extends Provider{
  private Storage<Accessory> storage;

  public AccessoryProvider(Storage<Accessory> storage){
    this.storage = storage;
  }

  @Override
  public void run(){
    this.storage.put(new Accessory());
  }
}
