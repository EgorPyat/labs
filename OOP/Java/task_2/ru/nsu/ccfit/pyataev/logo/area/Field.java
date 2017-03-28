package ru.nsu.ccfit.pyataev.logo.area;

public class Field{
  private boolean[][] field = null;
  private boolean draw = false;
  private boolean exist = false;
  private Position fieldSize = null;
  private Position playerPos = null;

  public boolean isExist(){
    return this.exist;
  }

  public boolean isDraw(){
    return this.draw;
  }

  public void setDraw(boolean flag){
    this.draw = flag;
  }

  public Position getPlayerPos(){
    return this.playerPos;
  }

  public void setPlayerPos(Position newPos){
    this.playerPos = newPos;
  }

  public Position getFieldSize(){
    return this.fieldSize;
  }

  public void setPositionMark(Position pos, boolean mark){
    this.field[pos.getY()][pos.getX()] = mark;
  }

  public void create(Position playerPos, Position fieldSize){
    this.playerPos = playerPos;
    this.fieldSize = fieldSize;

    this.field = new boolean[this.fieldSize.getY()][this.fieldSize.getX()];
    this.exist = true;
  }

  public void print(){
    for(int i = 0; i < this.fieldSize.getY(); i++){
      for(int j = 0; j < this.fieldSize.getX(); j++){
        if(this.playerPos.getY() == i && this.playerPos.getX() == j) System.out.print("P");
        else System.out.print(field[i][j]);
      }
      System.out.print("\n");
    }
  }

}
