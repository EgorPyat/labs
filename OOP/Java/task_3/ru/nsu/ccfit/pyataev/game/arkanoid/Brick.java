package ru.nsu.ccfit.pyataev.game.arkanoid;

import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Brick{
  private static final int WIDTH = 40;
  private static final int HEIGHT = 20;

  private int x = 0;
  private int y = 0;

  private Game game;

  private boolean destroyed = false;

  public Brick(Game game, int x, int y){
		this.game = game;
    this.x = x;
    this.y = y;
	}

  public void hide(){
    destroyed = true;
	}

  public void paint(Graphics2D g){
    if(destroyed == false) g.fillRect(x, y, WIDTH, HEIGHT);
  }

  public Rectangle getBounds(){
		if(destroyed == false) return new Rectangle(x, y, WIDTH, HEIGHT);
    return null;
  }
}
