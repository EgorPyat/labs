import java.awt.Polygon;
import java.awt.Point;

public class Hexahedron extends Polygon{
  private Point center;
  private int[] xc;
  private int[] yc;

  public void constructHexahedron(int x, int y, int radius){
    this.xc = new int[6];
    this.yc = new int[6];
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

  public int[] getXCoords(){
    return xc;
  }

  public int[] getYCoords(){
    return yc;
  }

}
