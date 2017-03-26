package ru.nsu.ccfit.pyataev.logo.area;

public class Field{
  private boolean[][] field = new boolean[0][0];
  private boolean draw = false;
  private boolean exist = false;
  private Position fieldSize = new Position();
  private Position playerPos = new Position();

  public boolean isExist(){
    return this.exist;
  }

  public boolean isDraw(){
    return this.draw;
  }

  public void setDraw(boolean flag){
    this.draw = flag;
  }
  
  public void create(Position playerPos, Position fieldSize){

  }
}
