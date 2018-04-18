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
  // private Point[] points;

  public BSplinePanel(Rectangle[] points){
    P = points;

    setPreferredSize(new Dimension(600, 450));
    setBackground(new Color(221, 224, 220));

    addMouseMotionListener(new MouseMotionAdapter(){
      @Override
      public void mouseDragged(MouseEvent e){
        Point ep = e.getPoint();
        for(int i = 0; i < P.length; i++){
          if(P[i].contains(ep)){
            P[i].setLocation(ep.x - 7, ep.y - 7);
            break;
          }
        }
        repaint();
      }
    });
    addMouseListener(new MouseAdapter(){
      @Override
      public void mousePressed(MouseEvent e){
        Point ep = e.getPoint();
        for(int i = 0; i < P.length; i++){
          if(P[i].contains(ep)){
            System.out.println(ep.x + " " + ep.y);
          }
        }
      }
    });
  }

  private Point calcBSplineInPoint(int t, Point[] points) throws Exception{
    int ORDER = 3;
    int N = points.length;
    int[] result = {0, 0};
    int[] knots = new int[N + ORDER + 1];
    int[] domain = {ORDER, knots.length - 1 - ORDER};
    int low  = knots[domain[0]];
    int high = knots[domain[1]];

    int[] weights = new int[N];
    for(int i = 0; i < N; i++) {
      weights[i] = 1;
    }

    t = t * (high - low) + low;
    t = t * (high - low) + low;

    if(t < low || t > high) throw new Exception("Out of bounds!");

    for(int i = 0; i < knots.length; i++){
      knots[i] = i;
    }

    int s;

    for(s = domain[0]; s < domain[1]; s++){
      if(t >= knots[s] && t <= knots[s + 1]){
        break;
      }
    }

    int[][] v = new int[N][3];
    for(int i = 0; i < N; i++) {
      // v[i] = [];
      // for(int j = 0; j < 2; j++) {
      //   v[i][j] = points[i][j] * weights[i];
      // }
      v[i][0] = points[i].x * weights[i];
      v[i][1] = points[i].y * weights[i];
      v[i][2] = weights[i];
    }

    int alpha;
    for(int l = 1; l <= ORDER + 1; l++) {
      // build level l of the pyramid
      for(int i = s; i > s - ORDER - 1 + l; i--) {
        alpha = (t - knots[i]) / (knots[i + ORDER + 1 - l] - knots[i]);

        // interpolate each component
        for(int j = 0; j < 2 + 1; j++) {
          v[i][j] = (1 - alpha) * v[i-1][j] + alpha * v[i][j];
        }
      }
    }

    for(int i = 0; i < 2; i++) {
      result[i] = v[s][i] / v[s][2];
    }

    return new Point(result[0], result[1]);
  }

  @Override
  public void paint(Graphics g){
    super.paint(g);

    g.setColor(new Color(98, 68, 109));
    g.drawLine(X0, 0, X0, getHeight());
    g.drawLine(0, Y0, getWidth(), Y0);
    // for(double t = 0; t < 1.; t += 0.01){
    //   calcBSplineInPoint(t, )
    // }
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
