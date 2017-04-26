package ru.nsu.ccfit.pyataev.game.arkanoid.model;

import ru.nsu.ccfit.pyataev.game.arkanoid.view.Panel;

public class World{
  private Panel view;

  final private int WIDTH = 300;
  final private int HEIGHT = 400;

  private Ball ball = new Ball(this);
  private Racquet racquet = new Racquet(this);

  private int bn = 3;
  private int abn = 3;

  private int score = 0;

  private Brick bricks[] = new Brick[bn];
  {
    for(int i = 0; i < bn; i++){
      bricks[i] = new Brick(this, 50 + i * 80, 20);
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
