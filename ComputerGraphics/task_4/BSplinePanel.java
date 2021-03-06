import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import java.net.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.Arrays;
import javax.swing.border.*;

class BSplinePanel extends JPanel{
  private final int WIDTH  = 600;
  private final int HEIGHT = 450;
  private final int X0 = WIDTH  / 2;
  private final int Y0 = HEIGHT / 2;
  private int cx, cy;

  private Rectangle[] P;
  private Point2D.Double[] ps;
  private Point2D.Double[] po;
  private Point2D.Double[] psNk;

  public BSplinePanel(Rectangle[] points){
    P = points;
    ps = new Point2D.Double[P.length];
    for(int i = 0; i < P.length; i++){
      ps[i] = new Point2D.Double(P[i].x + 6, P[i].y + 6);
    }
    setPreferredSize(new Dimension(600, 450));

    addMouseMotionListener(new MouseMotionAdapter(){
      @Override
      public void mouseDragged(MouseEvent e){
        Point ep = e.getPoint();
        for(int i = 0; i < P.length; i++){
          if(P[i].contains(ep)){
            P[i].setLocation(ep.x - 7, ep.y - 7);
            ps[i].x = ep.x;
            ps[i].y = ep.y;
            break;
          }
        }
        repaint();
      }
    });
  }

  private Point2D.Double calcBSplineInPoint(double t, Point2D.Double[] points) throws Exception{
    int ORDER = 3;
    int N = points.length;
    double[] result = {0, 0};

    int[] weights = new int[N];
    for(int i = 0; i < N; i++) {
      weights[i] = 1;
    }

    double[] knots = new double[N + ORDER + 1];
    for(int i = 0; i < knots.length; i++){
      knots[i] = i;
    }

    int[] domain = {ORDER, knots.length - 1 - ORDER};

    double low  = knots[domain[0]];
    double high = knots[domain[1]];

    t = t * (high - low) + low;
    if(t < low || t > high) throw new Exception("Out of bounds!");

    int s;
    for(s = domain[0]; s < domain[1]; s++){
      if(t >= knots[s] && t <= knots[s + 1]){
        break;
      }
    }

    double[][] v = new double[N][3];
    for(int i = 0; i < N; i++) {
      v[i][0] = points[i].x * weights[i];
      v[i][1] = points[i].y * weights[i];
      v[i][2] = weights[i];
    }
    double alpha;
    for(int l = 1; l <= ORDER + 1; l++) {
      for(int i = s; i > s - ORDER - 1 + l; i--) {
        alpha = (t - knots[i]) / (knots[i + ORDER + 1 - l] - knots[i]);
        for(int j = 0; j < 2 + 1; j++) {
          v[i][j] = (1. - alpha) * v[i-1][j] + alpha * v[i][j];
        }
      }
    }

    for(int i = 0; i < 2; i++) {
      result[i] = v[s][i] / v[s][2];
    }
    // System.out.println(result[0] + " " + result[1]);
    return new Point2D.Double((result[0] - X0) / 100, (result[1] - Y0) / 100);
  }

  private Point2D.Double[] getBSpline() throws Exception{
    Point2D.Double[] points = new Point2D.Double[100];

    int i = 1;

    points[0] = calcBSplineInPoint(0, ps);

    for(double t = 0.01; t < 1.; t += 0.01){
      points[i] = calcBSplineInPoint(t, ps);
      ++i;
    }

    return points;
  }

  public Point2D.Double[] getFigurePoints(int N, int k){
    psNk = new Point2D.Double[(N * k) + 1];
    int c = 0;
    for(int i = 0; i < 100; i++){
      if(i == 0 || ((i + 1) % (100 / (N * k)) == 0)){
        psNk[c++] = po[i];
      }
    }

    return psNk;
  }

  @Override
  public void paint(Graphics g){
    super.paint(g);

    g.setColor(new Color(98, 68, 109));
    g.drawLine(X0, 8, X0, getHeight() - 4);
    g.drawLine(3, Y0, getWidth() - 4, Y0);
    try{
      Graphics2D g2d = (Graphics2D)g;
      g2d.setColor(new Color(138, 99, 255));
      g2d.setStroke(new BasicStroke(2));
      g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

      po = getBSpline();

      for(int i = 1; i < po.length; i++){
        g2d.drawLine((int)(po[i - 1].x * 100. + X0), (int)(po[i - 1].y * 100. + Y0), (int)(po[i].x * 100. + X0), (int)(po[i].y * 100. + Y0));
      }

      g2d.setStroke(new BasicStroke(1));
    }
    catch(Exception e){
      e.printStackTrace();
      System.out.println(e.getMessage());
    }

    g.setColor(Color.RED);

    Point p = P[0].getLocation();
    g.drawOval(p.x, p.y, 12, 12);

    for(int i = 1; i < P.length; i++){
      p = P[i].getLocation();
      g.drawOval(p.x, p.y, 12, 12);
      Point p1 = P[i - 1].getLocation();
      g.drawLine(p.x + 6, p.y + 6, p1.x + 6, p1.y + 6);
    }
  }
}
