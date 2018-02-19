import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.*;
import java.awt.geom.Ellipse2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.Point;
import java.util.LinkedList;

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
      int N = 11;
      int M = 10;
      int R = 50;
      int width = 20 + R * M + 25;
      int height = 20 + (int)(R * Math.sqrt(3)/2) * N + 20;
      System.out.println(width + " " + height);

      img = new BufferedImage(width * 3 / 2, height * 2, BufferedImage.TYPE_INT_ARGB);

      // Graphics g = img.createGraphics();

      // drawHexahedronGrid(g, N, M, R);

      imagePanel = new ImagePanel(img);
      scrollpane = new JScrollPane(imagePanel);
      setLayout(new BorderLayout());
      setPreferredSize(new Dimension(800, 600));
      add(scrollpane, BorderLayout .CENTER);
   }
}

class ImagePanel extends JPanel {
   private BufferedImage image;
   private int initWidth, initHeight;
   static boolean pressed = false;

   public void spanFilling(BufferedImage img, int x, int y, Color newColor){
     int oldRGB = img.getRGB(x, y);
     LinkedList<Point> spans = new LinkedList<>();
     int tempRGB;
     if(oldRGB == Color.GREEN.getRGB() || oldRGB == Color.BLACK.getRGB()){
       return;
     }
     spans.add(new Point(x, y));
     int i = 4;
     while(!spans.isEmpty()){
       boolean upAdded = false;
       boolean downAdded = false;
       Point span = spans.poll();

       x = (int)span.getX();
       y = (int)span.getY();

       int tx = x;
       int step = 1;

       while(true){
         tempRGB = img.getRGB(x, y);
         if(tempRGB != Color.BLACK.getRGB()){
           if(!upAdded){
             int t = img.getRGB(x, y + 1);
             if(t == oldRGB){
               spans.add(new Point(x, y + 1));
               upAdded = true;
             }
           }
           if(!downAdded){
             int t = img.getRGB(x, y - 1);
             if(t == oldRGB){
               spans.add(new Point(x, y - 1));
               downAdded = true;
             }
           }
           img.setRGB(x, y, Color.GREEN.getRGB());
           x += step;
         }
         else if(step == 1){
           x = tx;
           step = -1;
         }
         else break;

       }

     }
    //  repaint();

   }

   private void drawHexahedron(Graphics g, int x, int y, int r){
     int R = r;
     double a, b, z = 0;
     int i = 0;
     double angle = 60.0;
     int X = x;
     int Y = y;
     int[] xc = new int[6];
     int[] yc = new int[6];
     a = Math.sqrt(3)/2;
     int yl = (int)(a * R);
     int xl = R / 2;
     xc[0] = X + R;  yc[0] = Y;
     xc[1] = X + xl; yc[1] = Y - yl;
     xc[2] = X - xl; yc[2] = Y - yl;
     xc[3] = X - R;  yc[3] = Y;
     xc[4] = X - xl; yc[4] = Y + yl;
     xc[5] = X + xl; yc[5] = Y + yl;

     int x1, x2, y1, y2;
     int j = 5;
     while(j >= 0){
       if(j > 0){
         x1 = xc[j]; x2 = xc[j-1];
         y1 = yc[j]; y2 = yc[j-1];
         g.drawLine(x1, y1, x2, y2);
         j--;
       }
       else{
         x1 = xc[j]; x2 = xc[5];
         y1 = yc[j]; y2 = yc[5];
         g.drawLine(x1, y1, x2, y2);
         j--;
       }
     }
   }

   public void drawHexahedronGrid(Graphics g, int height, int width, int radius){
     g.setColor(Color.black);
     int R = radius % 2 == 0 ? radius : radius + 1;
     int X = R + 20;
     int tx = X;
     int Y = (int)(R * Math.sqrt(3)/2) - 1 + 20;
     int ty = Y;
     int xl = R + R / 2;
     int yl = (int)(R * Math.sqrt(3)/2);
     for(int i = 0; i < height; i++){
       for(int j = 0; j < width; j++){
         drawHexahedron(g, X, Y, R);
         X += xl;
         Y = j % 2 == 0 ? Y + yl : Y - yl;
       }
       X = tx;
       Y = ty + yl * 2;
       ty = Y;
     }
   }

   public ImagePanel(BufferedImage image) {
      this.image = image;
      initWidth = image.getWidth();
      initHeight = image.getHeight();
      setPreferredSize(new Dimension(initWidth, initHeight));
      int N = 11;
      int M = 10;
      int R = 50;
      Graphics g = this.image.createGraphics();
      drawHexahedronGrid(g, N, M, R);

      addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
          int x = e.getX();
          int y = e.getY();
          // System.out.println("Mouse x: " + e.getX());
          // System.out.println("Mouse y: " + e.getY());
          spanFilling(image, x, y, null);
          repaint();
        }
      });
      addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
              int x = e.getX();
              int y = e.getY();
              // System.out.println("Mouse x: " + e.getX());
              // System.out.println("Mouse y: " + e.getY());
              spanFilling(image, x, y, null);

              repaint();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
              // System.out.println("Mouse x: " + e.getX());
              // System.out.println("Mouse y: " + e.getY());
            }
        });
   }

   @Override
   public void paint(Graphics g) {
      super.paint(g);
      if(image != null) {
        g.drawImage(image, 0, 0, null);
      }
   }
}
