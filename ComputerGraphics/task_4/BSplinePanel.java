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
  public BSplinePanel(){
    setPreferredSize(new Dimension(400, 300));
  }

  @Override
  public void paint(Graphics g){
    g.setColor(Color.BLACK);
    g.drawLine(0, 0, 399, 299);
    g.drawLine(399, 0, 0, 299);
  }
}
