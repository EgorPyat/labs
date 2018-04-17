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

  private Point[] P;
  private int X0, Y0;

  public BSplinePanel(){
    X0 = WIDTH  / 2;
    Y0 = HEIGHT / 2;

    P = new Point[5];

    setPreferredSize(new Dimension(600, 450));
    setBackground(new Color(221, 224, 220));
  }

  @Override
  public void paint(Graphics g){
    super.paint(g);

    g.setColor(new Color(98, 68, 109));
    g.drawLine(X0, 0, X0, getHeight());
    g.drawLine(0, Y0, getWidth(), Y0);
  }
}
