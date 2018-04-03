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

  private int[] colors = {
    Color.RED.getRGB(),
    Color.ORANGE.getRGB(),
    Color.YELLOW.getRGB(),
    Color.GREEN.getRGB(),
    Color.CYAN.getRGB(),
    Color.BLUE.getRGB(),
    Color.MAGENTA.getRGB(),
  };

  private int N = 6;

  private Function f;

  public IsolinePane(int width, int height){
    setPreferredSize(new Dimension(width, height));

    f = new Function(){
      @Override
      public double function(int x, int y){
        return x * x + y * y;
      }
    };

    portrait = new BufferedImage(rightDownCorner[0] - leftUpCorner[0] + 1, rightDownCorner[1] - leftUpCorner[1] + 1, BufferedImage.TYPE_INT_ARGB);

    double min = f.getMinimum(leftUpCorner[0], leftUpCorner[1], rightDownCorner[0], rightDownCorner[1]);
    double max = f.getMaximum(leftUpCorner[0], leftUpCorner[1], rightDownCorner[0], rightDownCorner[1]);

    double[] levels = new double[N + 1];
    double div = (max - min) / (N + 1);

    for(int i = 0; i < N + 1; i++) {
      levels[i] = min + div * i;
      System.out.println(levels[i]);
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
      g.fillRect(i * 700 / (N + 1), 0, 700 / (N + 1), 40);
      g.setColor(Color.BLACK);
      g.drawRect(i * 700 / (N + 1), 0, 700 / (N + 1), 40);
    }
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

    g.drawImage(portrait, 10, 10, null);
    g.drawImage(legend, 10, 10 + 550 + 10, null);
  }
}
