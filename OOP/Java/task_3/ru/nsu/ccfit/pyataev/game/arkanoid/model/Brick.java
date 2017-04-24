package ru.nsu.ccfit.pyataev.game.arkanoid.model;

import java.awt.Rectangle;

public class Brick{
  private final int WIDTH = 40;
  private final int HEIGHT = 20;

  private final int x;
  private final int y;

  private World space;

  private boolean destroyed = false;

  Brick(World space, int x, int y){
		this.space = space;
    this.x = x;
    this.y = y;
	}

  void hide(){
    destroyed = true;
	}

  public int getX(){
    return this.x;
  }

  public int getY(){
    return this.y;
  }

  public int getHeight(){
    return this.HEIGHT;
  }

  public int getWidth(){
    return this.WIDTH;
  }

  public boolean getDestr(){
    return destroyed;
  }

  Rectangle getBounds(){
		if(destroyed == false) return new Rectangle(x, y, WIDTH, HEIGHT);
    return null;
  }
}
