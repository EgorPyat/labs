package ru.nsu.ccfit.pyataev.game.arkanoid.model;

import java.awt.Rectangle;
import java.util.Random;
import java.time.LocalTime;

public class Ball{
	private final int DIAMETER = 30;

  private Random random = new Random(LocalTime.now().hashCode());

  private int x = 160;
	private int y = 330;
	private int xa;
	private int ya;
  private int bn;
	private boolean falled = false;
	private World space;

	Ball(World space){
		this.space = space;
    ya = 1;
    xa = (random.nextBoolean() == true ? -1 : 1);
	}

	void move(){
		if (x + xa < 0)	xa = 1;
		else if (x + xa > space.getWidth() - DIAMETER) xa = -1;
		else if (y + ya < 0) ya = 1;
		else if (y + ya > space.getHeight() - DIAMETER)	falled = true;
		else if (racquetCollision()){
			ya = -1;
			y = space.getRacquet().getY() - DIAMETER;
			space.incScore(1);
    }
    else if(brickCollision()){
      space.getBricks()[bn].hide();
      space.incScore(10);
      space.decrAliveBricksNum();
      ya = (random.nextBoolean() == false ? 1 : -1);
      xa = (random.nextBoolean() == true ? -1 : 1);
    }

		x = x + xa;
		y = y + ya;
	}

	private boolean racquetCollision(){
		return space.getRacquet().getBounds().intersects(getBounds());
	}

  private boolean brickCollision(){
    Rectangle br;
    for(int i = 0; i < space.getBricksNum(); i++){
      br = space.getBricks()[i].getBounds();
      if(br == null) continue;
      if(br.intersects(getBounds()) == true){
        bn = i;
        return true;
      }
    }
    return false;
  }

	public int getX(){
		return this.x;
	}

	public int getY(){
		return this.y;
	}

	public int getDiam(){
		return this.DIAMETER;
	}

	boolean getFall(){
		return this.falled;
	}

	Rectangle getBounds(){
		return new Rectangle(x + 5, y + 5, DIAMETER - 10, DIAMETER - 10);
	}
}
