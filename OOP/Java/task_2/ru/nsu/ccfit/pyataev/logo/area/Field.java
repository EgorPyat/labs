package ru.nsu.ccfit.pyataev.logo.area;

/**
  * Class contain prgramm field
  * @author EgorPyat
  */
public class Field{
  private boolean[][] field = null;
  private boolean draw = false;
  private boolean exist = false;
  private Position fieldSize = null;
  private Position playerPos = null;
  /**
    * Return field state
    * @return field state
    */
  public boolean isExist(){
    return this.exist;
  }
  /**
    * Return draw state
    * @return draw state
    */
  public boolean isDraw(){
    return this.draw;
  }
  /**
    * Set draw state
    * @param flag state
    */
  public void setDraw(boolean flag){
    this.draw = flag;
  }
  /**
    * Return playerPos
    * @return playerPos
    */
  public Position getPlayerPos(){
    return this.playerPos;
  }
  /**
    * Set playerPos
    * @param newPos new playerPos
    * @exception IllegalArgumentException
    */
  public void setPlayerPos(Position newPos) throws IllegalArgumentException{
    if(newPos.getX() < 0 || newPos.getY() < 0 || newPos.getX() > this.fieldSize.getX() - 1 || newPos.getY() > this.fieldSize.getY() - 1) throw new IllegalArgumentException("Bad coordinates!");
    this.playerPos = newPos;
  }
  /**
    * Return fieldSize
    * @return fieldSize
    */
  public Position getFieldSize(){
    return this.fieldSize;
  }
  /**
    * Set position mark
    * @param pos new pos
    * @param mark flag
    */
  public void setPositionMark(Position pos, boolean mark){
    this.field[pos.getY()][pos.getX()] = mark;
  }
  /**
    * Create field
    * @param playerPos set playerPos
    * @param fieldSize set fieldSize
    * @exception IllegalArgumentException
    */
  public void create(Position playerPos, Position fieldSize) throws IllegalArgumentException{
    if(fieldSize.getX() <= 0 || fieldSize.getY() <= 0) throw new IllegalArgumentException("Bad field size!");
    if(playerPos.getX() < 0 || playerPos.getY() < 0 || playerPos.getX() > fieldSize.getX() - 1 || playerPos.getY() > fieldSize.getY() - 1) throw new IllegalArgumentException("Bad coordinates!");

    this.playerPos = playerPos;
    this.fieldSize = fieldSize;

    this.field = new boolean[this.fieldSize.getY()][this.fieldSize.getX()];
    this.exist = true;
  }
  /**
    * print field
    */
  public void print(){
    for(int i = 0; i < this.fieldSize.getY(); i++){
      for(int j = 0; j < this.fieldSize.getX(); j++){
        if(this.playerPos.getY() == i && this.playerPos.getX() == j) System.out.print(" P");
        else if(field[i][j]) System.out.print(" #");
        else System.out.print(" *");
      }
      System.out.print("\n\n");
    }
  }

}
