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

class IsolinePane extends JPanel{
  private BufferedImage portrait;
  private BufferedImage legend;

  private int[] leftUpCorner = {-100, -150};
  private int[] rightDownCorner = {600, 400};

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
    portrait = new BufferedImage(rightDownCorner[0] - leftUpCorner[0] + 1, rightDownCorner[1] - leftUpCorner[1] + 1, BufferedImage.TYPE_INT_ARGB);

    double min = f.getMinimum(leftUpCorner[0], leftUpCorner[1], rightDownCorner[0], rightDownCorner[1]);
    double max = f.getMaximum(leftUpCorner[0], leftUpCorner[1], rightDownCorner[0], rightDownCorner[1]);

    double[] levels = new double[N + 1];
    double div = (max - min) / (N + 1);

    for(int i = 0; i < N + 1; i++) {
      levels[i] = min + div * i;
    }

    double level;
    double val;
    for(int i = leftUpCorner[0]; i < rightDownCorner[0] + 1; i++){
      for(int j = leftUpCorner[1]; j < rightDownCorner[1] + 1; j++){
        val = f.function(i, j);
        for(int c = 0; c < N + 1; c++){
          level = levels[N - c];
          if(val > level){
            portrait.setRGB(i - leftUpCorner[0], j - leftUpCorner[1], colors[N - c]);
            break;
          }
        }
      }
    }

    legend = new BufferedImage(rightDownCorner[0] - leftUpCorner[0] + 1, 40, BufferedImage.TYPE_INT_ARGB);
    Graphics g = legend.createGraphics();
    for(int i = 0; i < N + 1; i++){
      g.setColor(new Color(colors[i]));
      g.fillRect(i * (portrait.getWidth()) / (N + 1), 0, portrait.getWidth() / (N + 1), 40);
      g.setColor(Color.BLACK);
      g.drawRect(i * portrait.getWidth() / (N + 1), 0, portrait.getWidth() / (N + 1), 40);
    }
  }
  public IsolinePane(int width, int height){
    setPreferredSize(new Dimension(width, height));

    f = new Function(){
      @Override
      public double function(int x, int y){
        return x * x + y * y;
      }
    };

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
      public void mouseDragged(MouseEvent e){

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
    setPreferredSize(new Dimension(0,0));
    // if(width > getWidth() || height > getHeight()){
    System.out.println(getWidth() + " " + getHeight());
    System.out.println(width + " " + height);
      scale1 = /*width > getWidth() ? */(double)getWidth() / width /*: 1*/;
      scale2 = /*height > getHeight() ? */(double)getHeight() / height /*: 1*/;
      scale = scale1 < scale2 ? scale1 : scale2;
      System.out.println(scale);
    // }
    g.drawImage(portrait, 10, 10, (int)(wG * scale) - 10, (int)(hG * scale), null);
    g.drawImage(legend, 10, 10 + (int)(hG * scale) + 10, (int)(wL * scale) - 10, (int)(hL) - 10, null);
  }
}
