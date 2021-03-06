import java.awt.Polygon;
import java.awt.Point;

public class Hexahedron extends Polygon{
  private Point center;
  private int[] xc;
  private int[] yc;
  private int sideThick;
  private boolean alive = false;
  private boolean survive;
  private double impact = 0.0;

  public void construct(int x, int y, int radius, int sideThick){
    this.xc = new int[6];
    this.yc = new int[6];
    this.sideThick = sideThick;
    this.center = new Point(x, y);
    int R = radius;
    double a = 0;
    int angle = 60;
    a = Math.sqrt(3)/2;
    int yl = (int)(a * R);
    int xl = R / 2;
    xc[0] = x + R;  yc[0] = y;
    xc[1] = x + xl; yc[1] = y - yl;
    xc[2] = x - xl; yc[2] = y - yl;
    xc[3] = x - R;  yc[3] = y;
    xc[4] = x - xl; yc[4] = y + yl;
    xc[5] = x + xl; yc[5] = y + yl;
  }

  public Point getCenter(){
    return this.center;
  }

  public int getSideThick(){
    return this.sideThick;
  }

  public void setSideThick(int s){
    this.sideThick = s;
  }

  public int[] getXCoords(){
    return this.xc;
  }

  public int[] getYCoords(){
    return this.yc;
  }

  public boolean isAlive(){
    return this.alive;
  }

  public void setAlive(boolean alive){
    this.alive = alive;
  }

  public void changeStatus(){
    this.alive = !this.alive;
  }

  public void setSurvive(){
    this.survive = true;
  }

  public boolean getSurvive(){
    return this.survive;
  }

  public void setDead(){
    this.survive = false;
  }

  public double getImpact(){
    return this.impact;
  }

  public void setImpact(double impact){
    this.impact = impact;
  }
}
