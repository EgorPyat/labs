package ru.nsu.ccfit.pyataev.logo.area;

/**
  * Class for saving position
  * @author EgorPyat
  */
public class Position{
  private int x = 0;
  private int y = 0;
  /**
    * Return X coordinate
    * @return x coordinate
    */
  public int getX(){
    return this.x;
  }
  /**
    * Return Y coordinate
    * @return y coordinate
    */
  public int getY(){
    return this.y;
  }
  /**
    * Set X coordinate
    * @param x x coordinate
    */
  public void setX(int x){
    this.x = x;
  }
  /**
    * Set Y coordinate
    * @param y y coordinate
    */
  public void setY(int y){
    this.y = y;
  }

}
