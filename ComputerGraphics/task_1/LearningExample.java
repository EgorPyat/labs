import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.*;

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

@SuppressWarnings("serial")
class MainPanel extends JPanel {
   private static final String URL_PATH = "http://duke.kenai.com/"
            + "guitar/DukeAsKeith-daylightSmall.png";
   protected static final double SCALE_FACTOR = 1.01;
   private static final int TIMER_DELAY = 200;
   private JScrollPane scrollpane;
   private ImagePanel imagePanel;

   public MainPanel() {
      BufferedImage img = null;
      try {
         URL imageUrl = new URL(URL_PATH);
         img = ImageIO.read(imageUrl);
      } catch (MalformedURLException e) {
         e.printStackTrace();
         System.exit(-1);
      } catch (IOException e) {
         e.printStackTrace();
         System.exit(-1);
      }

      imagePanel = new ImagePanel(img);
      scrollpane = new JScrollPane(imagePanel);

      setLayout(new BorderLayout());
      add(scrollpane, BorderLayout.CENTER);

      new Timer(TIMER_DELAY, new ActionListener() {
         double scale = 1.0;

         public void actionPerformed(ActionEvent e) {
            scale *= SCALE_FACTOR;
            imagePanel.reScale(scale);
            imagePanel.repaint();
            //!! scrollpane.getViewport().revalidate();  // **** un-comment this line ****
         }
      }).start();
   }

}

@SuppressWarnings("serial")
class ImagePanel extends JPanel {
   private BufferedImage image;
   private AffineTransform at = new AffineTransform(); // identity transform
   private int initWidth, initHeight;

   public ImagePanel(BufferedImage image) {
      this.image = image;
      initWidth = image.getWidth();
      initHeight = image.getHeight();
      setPreferredSize(new Dimension(initWidth, initHeight));
   }

   public void reScale(double scale) {
      at = AffineTransform.getScaleInstance(scale, scale);
      int width = (int) (scale * initWidth);
      int height = (int) (scale * initHeight);
      setPreferredSize(new Dimension(width, height));
      repaint();
   }

   @Override
   protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      Graphics2D g2 = (Graphics2D) g;
      g2.transform(at);
      if (image != null) {
         g.drawImage(image, 0, 0, null);
      }
   }
}
