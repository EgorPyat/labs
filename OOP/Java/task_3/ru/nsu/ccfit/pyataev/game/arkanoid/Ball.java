package ru.nsu.ccfit.pyataev.game.arkanoid;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Random;
import java.time.LocalTime;

public class Ball{
	private static final int DIAMETER = 30;

  private Random random = new Random(LocalTime.now().hashCode());

  private int x = 160;
	private int y = 330;
	private int xa;
	private int ya;
  private int bn;

	private Game game;

	public Ball(Game game){
		this.game = game;
    ya = 1;
    xa = (random.nextBoolean() == true ? -1 : 1);
	}

	void move(){
		if (x + xa < 0)
			xa = 1;
		else if (x + xa > game.getWidth() - DIAMETER)
			xa = -1;
		else if (y + ya < 0)
			ya = 1;
		else if (y + ya > game.getHeight() - DIAMETER)
			game.gameOver();
		else if (racquetCollision()){
			ya = -1;
			y = game.racquet.getTopY() - DIAMETER;
			game.speed++;
    }
    else if(brickCollision()){
      game.bricks[bn].hide();
      game.speed += 10;
      --game.lbn;
      ya = (random.nextBoolean() == false ? 1 : -1);
      xa = (random.nextBoolean() == true ? -1 : 1);
    }

		x = x + xa;
		y = y + ya;
	}

	private boolean racquetCollision(){
		return game.racquet.getBounds().intersects(getBounds());
	}

  private boolean brickCollision(){
    Rectangle br;
    for(int i = 0; i < game.bn; i++){
      br = game.bricks[i].getBounds();
      if(br == null) continue;
      if(br.intersects(getBounds()) == true){
        bn = i;
        return true;
      }
    }
    return false;
  }

	public void paint(Graphics2D g){
		g.fillOval(x, y, DIAMETER, DIAMETER);
	}

	public Rectangle getBounds(){
		return new Rectangle(x + 5, y + 5, DIAMETER - 10, DIAMETER - 10);
	}
}
