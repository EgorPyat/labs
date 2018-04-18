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
  }

  @Override
  public void paint(Graphics g){
    super.paint(g);

    g.setColor(new Color(98, 68, 109));
    g.drawLine(X0, 0, X0, getHeight());
    g.drawLine(0, Y0, getWidth(), Y0);

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
