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

import java.io.*;
import java.util.*;

public class Factory{
  private int factoryWorkers;
  private int carDealers;
  private int bodyProviders;
  private int engineProviders;
  private int accessoryProviders;
  private int engineStorageCapacity;
  private int bodyStorageCapacity;
  private int carStorageCapacity;
  private int accessoryStorageCapacity;
  private boolean logger;

  private static volatile Factory instance;

  private Factory(){}

  public static Factory getInstance(){
    Factory localInstance = instance;
    if(localInstance == null){
      synchronized(Factory.class){
        localInstance = instance;
        if(localInstance == null){
          instance = localInstance = new Factory();
        }
      }
    }
    return localInstance;
  }

  public void getWorkPlan(String config){
    Properties prop = new Properties();
    try
    (
    InputStream inp = ClassLoader.getSystemResourceAsStream(config);
    )
    {
      prop.load(inp);
      for(String pr : prop.stringPropertyNames()){
        if(pr.equals("FW")){
          this.factoryWorkers = new Integer(prop.getProperty(pr));
        }
        else if(pr.equals("CD")){
          this.carDealers = new Integer(prop.getProperty(pr));
        }
        else if(pr.equals("BP")){
          this.bodyProviders = new Integer(prop.getProperty(pr));
        }
        else if(pr.equals("EP")){
          this.engineProviders = new Integer(prop.getProperty(pr));
        }
        else if(pr.equals("AP")){
          this.accessoryProviders = new Integer(prop.getProperty(pr));
        }
        else if(pr.equals("ESC")){
          this.engineStorageCapacity = new Integer(prop.getProperty(pr));
        }
        else if(pr.equals("BSC")){
          this.bodyStorageCapacity = new Integer(prop.getProperty(pr));
        }
        else if(pr.equals("ASC")){
          this.accessoryStorageCapacity = new Integer(prop.getProperty(pr));
        }
        else if(pr.equals("CSC")){
          this.carStorageCapacity = new Integer(prop.getProperty(pr));
        }
        else if(pr.equals("L")){
          this.logger = new Boolean(prop.getProperty(pr));
        }
      }
    }
    catch(IOException e){
      System.out.println(e.getMessage());
    }
  }

  public void startWork(){
    Storagable<Body> bodyStorage = new Storage<Body>(Body.class, bodyStorageCapacity);
    Storagable<Engine> engineStorage = new Storage<Engine>(Engine.class, engineStorageCapacity);
    Storagable<Accessory> accessoryStorage = new Storage<Accessory>(Accessory.class, accessoryStorageCapacity);
    ControlledStorage<Car> carStorage = new ControlledStorage<Car>(Car.class, carStorageCapacity, logger);

    Worker[] workers = new Worker[factoryWorkers];
    for(int i = 0; i < factoryWorkers; i++){
      workers[i] = new Worker(bodyStorage, engineStorage, accessoryStorage, carStorage);
    }

    for(int i = 0; i < accessoryProviders; i++){
      Thread accessoryProvider = new Thread(new AccessoryProvider(accessoryStorage), "AProvider#" + i);
      accessoryProvider.start();
    }

    for(int i = 0; i < bodyProviders; i++){
      Thread bodyProvider = new Thread(new BodyProvider(bodyStorage), "BProvider#" + i);
      bodyProvider.start();
    }

    for(int i = 0; i < engineProviders; i++){
      Thread engineProvider = new Thread(new EngineProvider(engineStorage), "EProvider#" + i);
      engineProvider.start();
    }

    for(int i = 0; i < carDealers; i++){
      Thread dealer = new Thread(new Dealer(carStorage), "Dealer#" + i);
      dealer.start();
    }

    ThreadPool tp = new ThreadPool(factoryWorkers, workers);
    CarStorageController csc = new CarStorageController(carStorage, tp);
    carStorage.addController(csc);

    while(carStorage.getOrders() != factoryWorkers){}

    new Thread(csc, "Controller").start();
  }
}
