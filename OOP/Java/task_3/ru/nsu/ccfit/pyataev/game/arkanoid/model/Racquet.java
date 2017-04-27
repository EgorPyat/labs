package ru.nsu.ccfit.pyataev.game.arkanoid.model;

import java.awt.Rectangle;

public class Racquet{
	private final int WIDTH = 80;
	private final int HEIGHT = 10;

	private int x;
	private final int y;
	private int xa = 0;

	private World space;

  Racquet(World space){
		this.space = space;
		this.y = space.getHeight() - 100;
		this.x = space.getWidth()/2 - WIDTH / 2;
	}

  void move(){
		if (x + xa > 0 && x + xa < space.getWidth() - WIDTH) x = x + xa;
	}

  public int getHeight(){
    return this.HEIGHT;
  }

  public int getWidth(){
    return this.WIDTH;
  }

	public int getX(){
    return this.x;
  }

  public int getY(){
    return this.y;
  }

	void stop(){
		xa = 0;
	}

	void setWay(String way){
		if(way.equals("L")) xa = -1;
		if(way.equals("R")) xa = 1;
	}

	Rectangle getBounds(){
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}
}
