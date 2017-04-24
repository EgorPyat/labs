package ru.nsu.ccfit.pyataev.game.arkanoid.controller;

import ru.nsu.ccfit.pyataev.game.arkanoid.model.World;
import ru.nsu.ccfit.pyataev.game.arkanoid.model.Ball;
import ru.nsu.ccfit.pyataev.game.arkanoid.model.Brick;
import ru.nsu.ccfit.pyataev.game.arkanoid.model.Racquet;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

public class Space{
  World w;

  public void addWorld(World w){
    this.w = w;
  }

  public void printBricks(Graphics2D g){
    for(int i = 0; i < w.getBricksNum(); i++){
      if(w.getBricks()[i].getDestr() == false) g.fillRect(w.getBricks()[i].getX(), w.getBricks()[i].getY(), w.getBricks()[i].getWidth(), w.getBricks()[i].getHeight());
    }
  }

  public void printBall(Graphics2D g){
    g.fillOval(w.getBall().getX(), w.getBall().getY(), w.getBall().getDiam(), w.getBall().getDiam());
  }

  public void printRacquet(Graphics2D g){
    g.fillRect(w.getRacquet().getX(), w.getRacquet().getY(), w.getRacquet().getWidth(), w.getRacquet().getHeight());
  }

  public void paint(Graphics2D g){
    this.printBall(g);
    this.printRacquet(g);
    this.printBricks(g);
  }

  public void change(){
    w.change();
  }

  public void keyReleased(KeyEvent e){
    w.stopRac();
  }

  public void keyPressed(KeyEvent e){
    if(e.getKeyCode() == KeyEvent.VK_LEFT) w.setRacWay("L");
    if(e.getKeyCode() == KeyEvent.VK_RIGHT) w.setRacWay("R");
  }

  public int getScore(){
    return w.getScore();
  }

  public boolean checkEnd(){
    return w.checkEnd();
  }

  public boolean checkSave(){
    return w.checkSave();
  }
}
