import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.geom.Ellipse2D;

public class ScalingImage {

   private static void createAndShowUI() {
      MainPanel mainPanel = new MainPanel();

      JFrame frame = new JFrame("ScalingImage");
      frame.getContentPane().add(mainPanel);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.pack();
      frame.setLocationRelativeTo(null);
      frame.setVisible(true);
   }

   public static void main(String[] args) {
      java.awt.EventQueue.invokeLater(new Runnable() {
         public void run() {
            createAndShowUI();
         }
      });
   }
}

class MainPanel extends JPanel {
   private JScrollPane scrollpane;
   private ImagePanel imagePanel;

   public MainPanel() {
      BufferedImage img = null;
      img = new BufferedImage(600, 800, BufferedImage.TYPE_INT_ARGB);
      img.setRGB(10, 10, 10);
      Graphics2D g2d = img.createGraphics();

      g2d.setColor(Color.red);
      g2d.fill(new Ellipse2D.Float(0, 0, 200, 100));
      g2d.dispose();

      imagePanel = new ImagePanel(img);
      scrollpane = new JScrollPane(imagePanel);

      setLayout(new BorderLayout());
      add(scrollpane, BorderLayout .CENTER);
   }

}

class ImagePanel extends JPanel {
   private BufferedImage image;
   private int initWidth, initHeight;

   public ImagePanel(BufferedImage image) {
      this.image = image;
      initWidth = image.getWidth();
      initHeight = image.getHeight();
      setPreferredSize(new Dimension(initWidth, initHeight));
   }

   @Override
   public void paint(Graphics g) {
      super.paintComponent(g);
      if (image != null) {
         g.drawImage(image, 0, 0, null);
      }
   }
}
