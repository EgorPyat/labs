package ru.nsu.ccfit.pyataev.game.arkanoid.model;

import ru.nsu.ccfit.pyataev.game.arkanoid.view.Panel;

import java.io.*;
import java.util.*;

public class World{
  private Panel view;

  private int WIDTH;
  private int HEIGHT;

  private Ball ball;
  private Racquet racquet;
  private Brick bricks[];

  private int bn;
  private int abn;

  private int score = 0;

  public World(String config){
    try
    (
      InputStreamReader conf = new InputStreamReader(new FileInputStream(config));
    )
    {
      int ch;
      int w = 0;
      int h = 0;

      while((ch = conf.read()) != -1){
        if(ch == 'b'){
          ++bn;
          ++abn;
        }
        if(ch == '\n') ++h;
        ++w;
      }
      w /= h;

      this.WIDTH = (w-1)*40;
      this.HEIGHT = h*20 + 300;

      this.racquet = new Racquet(this);
      this.ball = new Ball(this);

    }
    catch(IOException e){
      System.out.println(e.getMessage());
    }

    try
    (
      InputStreamReader conf = new InputStreamReader(new FileInputStream(config));
    )
    {
      this.bricks = new Brick[bn];
      int w = 0;
      int h = 0;
      int ch;

      int i = 0;
      while((ch = conf.read()) != -1){
        if(ch == 'b'){
          this.bricks[i] = new Brick(this, w*40, h*20);
          ++i;
        }
        ++w;
        if(ch == '\n'){
          ++h;
          w = 0;
        }
      }
    }
    catch(IOException e){
      System.out.println(e.getMessage());
    }
  }

  public Ball getBall(){
    return this.ball;
  }

  public Racquet getRacquet(){
    return this.racquet;
  }

  public Brick[] getBricks(){
    return this.bricks;
  }

  public int getBricksNum(){
    return bn;
  }

  public int getAliveBricksNum(){
    return abn;
  }

  public int getScore(){
    return score - 1;
  }

  public void incScore(int score){
    this.score += score;
  }

  public void decrAliveBricksNum(){
    --this.abn;
  }

  public int getHeight(){
    return this.HEIGHT;
  }

  public int getWidth(){
    return this.WIDTH;
  }

  public void moveBall(){
    this.ball.move();
  }

  public void moveRacquet(){
    this.racquet.move();
  }

  public void change(){
    this.moveBall();
    this.moveRacquet();
    view.setModelCondition();
  }

  public void setRacWay(String way){
    racquet.setWay(way);
  }

  public void stopRac(){
    racquet.stop();
  }

  public boolean checkSave(){
    if(this.getAliveBricksNum() == 0) return true;
    else return false;
  }

  public boolean checkEnd(){
    if(ball.getFall() == true) return true;
    else return false;
  }

  public void addPanel(Panel view){
    this.view = view;
  }
}
