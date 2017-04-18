package ru.nsu.ccfit.pyataev.game.model;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Platform{
  public Platform(){
    try{
      this.frame = ImageIO.read(getClass().getResource("/plat.png"));
    }
    catch(IOException e){

    }
    this.platform = new Rectangle(curX, 600, width, height);
  }

  public void move(int direction){
    switch(direction){
      case KeyEvent.VK_LEFT:
        if(curX == 0) curX = 0;
        this.curX -= 4;
        this.platform.setLocation(curX, 600);
        break;
      case KeyEvent.VK_RIGHT:
        if(curX == 950) curX = 950;
        this.curX += 4;
        break;
    }
  }

  private Rectangle platform;

  private BufferedImage frame;

  public BufferedImage getFrame(){
    return this.frame;
  }

  private int curX = 442;

  public int getCurrentX(){
    return curX;
  }

  private int height = 30;
  private int width = 140;
}
