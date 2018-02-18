import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.*;
import java.awt.geom.Ellipse2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class ScalingImage extends JFrame{

    public ScalingImage(){
      super("Life - The game.");

      JMenuBar menu = new JMenuBar();
      JMenu mFile = new JMenu("File");
      JMenu mAbout = new JMenu("About");
      JMenuItem mFileItem = new JMenuItem("Exit");
      JMenuItem mAboutItem = new JMenuItem("Info");
      mFileItem.addActionListener((e) -> System.exit(0));
      mAboutItem.addActionListener((e) -> JOptionPane.showMessageDialog(this,  "Life - The Game."));
      mFile.add(mFileItem);
      mAbout.add(mAboutItem);
      menu.add(mFile);
      menu.add(mAbout);
      setJMenuBar(menu);

      MainPanel mainPanel = new MainPanel();
      add(mainPanel);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      pack();
      setLocationRelativeTo(null);
      setVisible(true);
   }

   public static void main(String[] args) {
      new ScalingImage();
   }
}

class MainPanel extends JPanel {
   private JScrollPane scrollpane;
   private ImagePanel imagePanel;

   public MainPanel() {
      BufferedImage img = null;
      img = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);

      Graphics g = img.createGraphics();
      //
      g.setColor(Color.black);
      // g2d.fill(new Ellipse2D.Float(0, 0, 200, 100));
      // g2d.dispose();
      int n = 6;
      int R = 50;
      double a, b,  z = 0 ;  int i = 0; double angle = 360.0 / n ;
  		//цикл создающий массив из точек
      int X = R + 20; int Y = (int)(R * Math.sqrt(3)/2) - 1 + 20;
      int[] x = new int[n];
      int[] y = new int[n];
  		while(i < n){
  			a = Math.cos( z/180*Math.PI);
  			b = Math.sin( z/180*Math.PI);
  			x[i] = X + (int)(a * R);
  			y[i] = Y - (int)(b * R);
  			z = z + angle;
  			i++;
  		}
  		System.out.println();

  		int x1, x2, y1, y2;

  		int j = n - 1;
  		while(j >= 0){
  			if(j > 0){
  				x1 = x[j]; x2 = x[j-1];
  				y1 = y[j]; y2 = y[j-1];
  				g.drawLine(x1, y1, x2, y2);
  				j--;
  			}
  			else{
  				x1 = x[j]; x2 = x[n-1];
  				y1 = y[j]; y2 = y[n-1];
  				g.drawLine(x1, y1, x2, y2);
  				j--;
  			}
  		}
      imagePanel = new ImagePanel(img);
      scrollpane = new JScrollPane(imagePanel);
      setLayout(new BorderLayout());
      add(scrollpane, BorderLayout .CENTER);
   }
}

class ImagePanel extends JPanel {
   private BufferedImage image;
   private int initWidth, initHeight;
   static boolean pressed = false;

   public ImagePanel(BufferedImage image) {
      this.image = image;
      initWidth = image.getWidth();
      initHeight = image.getHeight();
      setPreferredSize(new Dimension(initWidth, initHeight));
      addMouseListener(new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent mouseEvent) {
          System.out.println("Mouse x: " + mouseEvent.getX());
          System.out.println("Mouse y: " + mouseEvent.getY());
        }

        @Override
        public void mousePressed(MouseEvent mouseEvent) {
          System.out.println("Mouse x: " + mouseEvent.getX());
          System.out.println("Mouse y: " + mouseEvent.getY());
        }

        @Override
        public void mouseReleased(MouseEvent mouseEvent) {
          pressed = false;
        }

        @Override
        public void mouseEntered(MouseEvent mouseEvent) {

        }

        @Override
        public void mouseExited(MouseEvent mouseEvent) {

        }
      });
      addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
              System.out.println("Mouse x: " + e.getX());
              System.out.println("Mouse y: " + e.getY());
            }

            @Override
            public void mouseMoved(MouseEvent e) {
              System.out.println("Mouse x: " + e.getX());
              System.out.println("Mouse y: " + e.getY());
            }
        });
   }

   @Override
   public void paint(Graphics g) {
      // super.paintComponent(g);
      // Graphics2D bi = image.createGraphics();
      //
      // bi.setColor(Color.red);
      // bi.fill(new Ellipse2D.Float(0, 0, 200, 100));
      // // g2d.dispose();
      // Graphics2D g2d = (Graphics2D)g;
      // g2d.setColor(Color.black);
      // g2d.fillRect(0,0, getWidth()/2, getHeight()/2);
      // // g2d.setFont(new Font("Dialog", Font.BOLD, 24));
      // g2d.setColor(Color.white);
      // g2d.drawString("Java 2D is great!", 10, 80);
      // g2d.drawImage(image, 0, 0, null);
      if(image != null) {
        g.drawImage(image, 0, 0, null);
      }
   }
}
