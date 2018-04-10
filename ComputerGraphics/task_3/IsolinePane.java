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

class IsolinePane extends JPanel{
  private BufferedImage portrait;
  private Rectangle portraitBounds;
  private BufferedImage legend;

  private double[] leftUpCorner = {-5.75, -3};
  private double[] rightDownCorner = {5.75, 3};

  private double stepX, stepY;
  private int[] km;
  private int[] colors = {
    Color.RED.getRGB(),
    Color.ORANGE.getRGB(),
    Color.YELLOW.getRGB(),
    Color.GREEN.getRGB(),
    Color.CYAN.getRGB(),
    Color.BLUE.getRGB(),
    Color.MAGENTA.getRGB(),
    Color.BLACK.getRGB()
  };

  private int N = 6;

  private Function f;

  public void setGraphSettings(int[] km, int[][] colors){
    this.km = km;
    this.colors = new int[colors.length];
    for(int i = 0; i < colors.length; i++){
      this.colors[i] = new Color(colors[i][0], colors[i][1], colors[i][2]).getRGB();
    }
    this.N = colors.length - 2;

    drawGraph();
    repaint();
  }

  public void drawGraph(){
    portrait = new BufferedImage(777, 405, BufferedImage.TYPE_INT_ARGB);

    portraitBounds = new Rectangle(10, 10, portrait.getWidth(), portrait.getHeight());

    stepX = (rightDownCorner[0] - leftUpCorner[0]) / portrait.getWidth();
    stepY = (rightDownCorner[1] - leftUpCorner[1]) / portrait.getHeight();

    double min = f.getMinimum(leftUpCorner[0], leftUpCorner[1], rightDownCorner[0], rightDownCorner[1], stepX, stepY);
    double max = f.getMaximum(leftUpCorner[0], leftUpCorner[1], rightDownCorner[0], rightDownCorner[1], stepX, stepY);

    double[] levels = new double[N + 1];
    double div = (max - min) / (N + 1);

    for(int i = 0; i < N + 1; i++) {
      levels[i] = min + div * i;
    }

    double level;
    double val;
    for(int i = 0; i < portrait.getWidth(); i++){
      for(int j = 0; j < portrait.getHeight(); j++){
        val = f.function(i * stepX + leftUpCorner[0], j * stepY + leftUpCorner[1]);
        for(int c = 0; c < N + 1; c++){
          level = levels[N - c];
          if(val > level){
            portrait.setRGB(i, j, colors[N - c]);
            break;
          }
        }
      }
    }

    legend = new BufferedImage((int)((rightDownCorner[0] - leftUpCorner[0]) / stepX), 40, BufferedImage.TYPE_INT_ARGB);
    Graphics g = legend.createGraphics();
    for(int i = 0; i < N + 1; i++){
      g.setColor(new Color(colors[i]));
      g.fillRect(i * portrait.getWidth() / (N + 1), 0, portrait.getWidth() / (N + 1), 40);
      g.setColor(Color.BLACK);
      g.drawRect(i * portrait.getWidth() / (N + 1), 0, portrait.getWidth() / (N + 1), 40);
    }
  }

  public IsolinePane(int width, int height){
    setPreferredSize(new Dimension(width, height));
    setLayout(new BorderLayout());
    f = new Function(){
      @Override
      public double function(double x, double y){
        // Ackley function
        return -20 * Math.exp(-0.2 * Math.sqrt(0.5 * (x * x + y * y))) - Math.exp(0.5 * (Math.cos(2 * Math.PI * x) + Math.cos(2 * Math.PI * y))) + Math.E + 20;
      }
    };

    JPanel statusBar = new JPanel();

    TextField x = new TextField(" x: none", 7);
    TextField y = new TextField(" y: none", 7);
    TextField func = new TextField(" f: none", 7);

    x.setEnabled(false);
    y.setEnabled(false);
    func.setEnabled(false);

    statusBar.add(x);
    statusBar.add(y);
    statusBar.add(func);

    add(statusBar, BorderLayout.SOUTH);

    drawGraph();

    addMouseListener(new MouseAdapter(){
      @Override
      public void mousePressed(MouseEvent e){

      }

      @Override
      public void mouseReleased(MouseEvent e){

      }
    });

    addMouseMotionListener(new MouseMotionAdapter(){
      @Override
      public void mouseMoved(MouseEvent e){
        Point p = e.getPoint();
        if(portraitBounds.contains(p)){
          double xc = leftUpCorner[0] + (p.getX() - 10) * stepX;
          double yc = leftUpCorner[1] + (p.getY() - 10) * stepY;

          x.setText(String.format(" x: %.2f", xc));
          y.setText(String.format(" y: %.2f", yc));
          func.setText(String.format(" f: %.2f", f.function(xc, yc)));
        }
      }

      @Override
      public void mouseDragged(MouseEvent e){
        Point p = e.getPoint();
        if(portraitBounds.contains(p)){
          double xc = leftUpCorner[0] + (p.getX() - 10) * stepX;
          double yc = leftUpCorner[1] + (p.getY() - 10) * stepY;

          x.setText(String.format(" x: %.2f", xc));
          y.setText(String.format(" y: %.2f", yc));
          func.setText(String.format(" f: %.2f", f.function(xc, yc)));
        }
      }
    });
  }

  @Override
  public void paint(Graphics g){
    super.paint(g);
    int wG = portrait.getWidth();
    int hG = portrait.getHeight();
    int wL = legend.getWidth();
    int hL = legend.getHeight();
    int width = 10 + wG + 10;
    int height = 10 + hG + 10 + hL + 10;
    double scale1;
    double scale2;
    double scale = 1;
    // setPreferredSize(new Dimension(0,0));
    // // if(width > getWidth() || height > getHeight()){
    // System.out.println(getWidth() + " " + getHeight());
    // System.out.println(width + " " + height);
    //   scale1 = /*width > getWidth() ? */(double)getWidth() / width /*: 1*/;
    //   scale2 = /*height > getHeight() ? */(double)getHeight() / height /*: 1*/;
    //   scale = scale1 < scale2 ? scale1 : scale2;
    //   System.out.println(scale);
    // // }
    g.drawImage(portrait, 10, 10, (int)(wG * scale) - 10, (int)(hG * scale), null);
    g.drawImage(legend, 10, 10 + (int)(hG * scale) + 10, (int)(wL * scale) - 10, (int)(hL) - 10, null);
  }
}
