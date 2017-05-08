package ru.nsu.ccfit.pyataev.factory;

import ru.nsu.ccfit.pyataev.factory.storage.Storage;
import ru.nsu.ccfit.pyataev.factory.storage.Storagable;
import ru.nsu.ccfit.pyataev.factory.storage.ControlledStorage;
import ru.nsu.ccfit.pyataev.factory.dealer.Dealer;
import ru.nsu.ccfit.pyataev.factory.detail.Car;
import ru.nsu.ccfit.pyataev.factory.detail.Body;
import ru.nsu.ccfit.pyataev.factory.detail.Engine;
import ru.nsu.ccfit.pyataev.factory.detail.Accessory;
import ru.nsu.ccfit.pyataev.factory.provider.BodyProvider;
import ru.nsu.ccfit.pyataev.factory.provider.EngineProvider;
import ru.nsu.ccfit.pyataev.factory.provider.AccessoryProvider;
import ru.nsu.ccfit.pyataev.factory.controller.CarStorageController;

import ru.nsu.ccfit.pyataev.factory.worker.Worker;
import ru.nsu.ccfit.pyataev.threadpool.ThreadPool;

public class Factory{
  public static void main(String[] args) throws InterruptedException{
    Storagable<Body> bodyStorage = new Storage<Body>(Body.class, 10);
    Storagable<Engine> engineStorage = new Storage<Engine>(Engine.class, 5);
    Storagable<Accessory> accessoryStorage = new Storage<Accessory>(Accessory.class, 2);
    ControlledStorage<Car> carStorage = new ControlledStorage<Car>(Car.class, 15);

    Worker[] workers = new Worker[5];
    for(int i = 0; i < 5; i++){
      workers[i] = new Worker(bodyStorage, engineStorage, accessoryStorage, carStorage);
    }

    for(int i = 0; i < 3; i++){
      Thread accessoryProvider = new Thread(new AccessoryProvider(accessoryStorage), "AProvider#" + i);
      accessoryProvider.start();
    }

    for(int i = 0; i < 4; i++){
      Thread bodyProvider = new Thread(new BodyProvider(bodyStorage), "BProvider#" + i);
      bodyProvider.start();
    }

    for(int i = 0; i < 5; i++){
      Thread engineProvider = new Thread(new EngineProvider(engineStorage), "EProvider#" + i);
      engineProvider.start();
    }

    for(int i = 0; i < 5; i++){
      Thread dealer = new Thread(new Dealer(carStorage), "Dealer#" + i);
      dealer.start();
    }

    ThreadPool tp = new ThreadPool(5, workers);
    CarStorageController csc = new CarStorageController(carStorage, tp);
    carStorage.addController(csc);

    while(carStorage.getOrders() != 5){}

    new Thread(csc, "Controller").start();

  }
}
