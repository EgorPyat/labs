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

  private Function f;

  public IsolinePane(){
    setPreferredSize(new Dimension(780, 490));

    f = new Function(){
      @Override
      public double function(int x, int y){
        return x * x + y * y;
      }
    };

    portrait = new BufferedImage(rightDownCorner[0] - leftUpCorner[0], rightDownCorner[1] - leftUpCorner[1], BufferedImage.TYPE_INT_ARGB);

    for(int i = leftUpCorner[0]; i < rightDownCorner[0]; i++){
      for(int j = leftUpCorner[1]; j < rightDownCorner[1]; j++) {
        if(f.function(i, j) < 10) portrait.setRGB(i - leftUpCorner[0], j - leftUpCorner[1], Color.RED.getRGB());
        else portrait.setRGB(i - leftUpCorner[0], j - leftUpCorner[1], Color.BLUE.getRGB());
      }
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
  }
}
