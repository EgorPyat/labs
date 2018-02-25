// JTextField setText()
// искать центр, его координаты = > координаты шестиугольника, по Y делим на 2yl, по Х делим на 1.5R
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.*;
import java.awt.geom.Ellipse2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.Point;
import java.util.LinkedList;

public class ScalingImage extends JFrame{
    public ScalingImage(HexahedronGrid field){
      super("Life - The game.");
      JMenuBar menu = new JMenuBar();
      JMenu mFile = new JMenu("File");
      JMenu mAbout = new JMenu("About");
      JMenuItem mFileNew = new JMenuItem("New");
      JMenuItem mFileSave = new JMenuItem("Save");
      JMenuItem mFileImport = new JMenuItem("Import");
      JMenuItem mFileQuit = new JMenuItem("Quit");
      JMenuItem mAboutInfo = new JMenuItem("Info");
      mFileQuit.addActionListener((e) -> System.exit(0));
      mAboutInfo.addActionListener((e) -> JOptionPane.showMessageDialog(this,  "Life - The Game.\nBy Egor Pyataev"));
      mFile.add(mFileNew);
      mFile.add(mFileSave);
      mFile.add(mFileImport);
      mFile.add(mFileQuit);
      mAbout.add(mAboutInfo);
      menu.add(mFile);
      menu.add(mAbout);
      setJMenuBar(menu);
      MainPanel mainPanel = new MainPanel(field);
      add(mainPanel);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      pack();
      setLocationRelativeTo(null);
      setVisible(true);
   }

}

class MainPanel extends JPanel {
   private JScrollPane scrollpane;
   private ImagePanel imagePanel;

   public MainPanel(HexahedronGrid field){
      imagePanel = new ImagePanel(field);
      scrollpane = new JScrollPane(imagePanel);
      setLayout(new BorderLayout());
      setPreferredSize(new Dimension(800, 600));
      add(scrollpane, BorderLayout .CENTER);
   }
}

class ImagePanel extends JPanel {
   private BufferedImage image;
   private HexahedronGrid field;
   private int initWidth;
   private int initHeight;
   static boolean pressed = false;

   public void spanFilling(BufferedImage img, int x, int y, Color newColor){
     int oldRGB = img.getRGB(x, y);
     if(oldRGB == newColor.getRGB() || oldRGB == Color.BLACK.getRGB()){
       return;
     }
     LinkedList<Point> spans = new LinkedList<>();
     int tempRGB;
     spans.add(new Point(x, y));
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
           img.setRGB(x, y, newColor.getRGB());
           x += step;
         }
         else if(step == 1){
           x = tx;
           step = -1;
         }
         else break;
       }
     }
   }

   public void drawBrezenhemLine(){}

   private void drawHexahedron(Graphics g, Hexahedron h){
    //  int R = r;
    //  double a = 0;
    //  int i = 0;
    //  double angle = 60.0;
    //  int X = x;
    //  int Y = y;
    //  int[] xc = new int[6];
    //  int[] yc = new int[6];
    //  a = Math.sqrt(3)/2;
    //  int yl = (int)(a * R);
    //  int xl = R / 2;
    //  xc[0] = X + R;  yc[0] = Y;
    //  xc[1] = X + xl; yc[1] = Y - yl;
    //  xc[2] = X - xl; yc[2] = Y - yl;
    //  xc[3] = X - R;  yc[3] = Y;
    //  xc[4] = X - xl; yc[4] = Y + yl;
    //  xc[5] = X + xl; yc[5] = Y + yl;
    int xc[] = h.getXCoords();
    int yc[] = h.getYCoords();
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

   public void drawHexahedronGrid(){
     Graphics g = this.image.createGraphics();
     g.setColor(Color.black);
    // //  int R = radius % 2 == 0 ? radius : radius + 1;
    //  int R = radius;
    //  int X = R + 20;
    //  int tx = X;
    //  int Y = (int)(R * Math.sqrt(3)/2) - 1 + 20;
    //  int ty = Y;
    //  int xl = R + R / 2;
    //  int yl = (int)(R * Math.sqrt(3)/2);
    Hexahedron[][] f = this.field.getField();
     for(int i = 0; i < this.field.getHexHeight(); i++){
       for(int j = 0; j < this.field.getHexWidth(); j++){
         drawHexahedron(g, f[i][j]);
        //  spanFilling(this.image, X, Y, Color.LIGHT_GRAY);
        //  X += xl;
        //  Y = j % 2 == 0 ? Y + yl : Y - yl;
       }
      //  X = tx;
      //  Y = ty + yl * 2;
      //  ty = Y;
     }
   }

   public Point getCenterCoords(int x, int y){
     int xc;
     int yc;
     int tx = x;
     int ty = y;
     int color = this.image.getRGB(x, y);
     int tc = color;
     while(color != Color.BLACK.getRGB()){
       ++x;
       color = this.image.getRGB(x, y);
     }
     xc = x;
     x = tx;
     color = tc;
     while(color != Color.BLACK.getRGB()){
       --x;
       color = this.image.getRGB(x, y);
     }
     xc += x;
     x = tx;
     color = tc;
     while(color != Color.BLACK.getRGB()){
       ++y;
       color = this.image.getRGB(x, y);
     }
     yc = y;
     y = ty;
     color = tc;
     while(color != Color.BLACK.getRGB()){
       --y;
       color = this.image.getRGB(x, y);
     }
     yc += y;
     System.out.println(xc / 2 + " " + yc / 2);
     return new Point(xc / 2, yc / 2);
   }

   public ImagePanel(HexahedronGrid field){
      this.field = field;
      this.initWidth = field.getWidth();
      this.initHeight = field.getHeight();
      this.image = new BufferedImage(field.getWidth(), field.getHeight(), BufferedImage.TYPE_INT_ARGB);
      setPreferredSize(new Dimension(initWidth, initHeight));
      int N = this.field.getHexHeight();
      int M = this.field.getHexWidth();
      int R = this.field.getHexRadius();
      drawHexahedronGrid();

      addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
          int yl = (int)(R * Math.sqrt(3)/2);
          int yl2 = yl + yl;
          int x = e.getX();
          int y = e.getY();
          Point p = getCenterCoords(x, y);
          int cellX = ((int)p.getX() - 20) / (R + R / 2);
          int cellY = ((int)p.getY() - 20) / (yl2);
          System.out.println("(" + cellY + ", " + cellX + ")" + "(" + (y - 20) + ", " + (x - 20) + ")" + " (" + (p.getY() - 20) + ", " + (p.getX() - 20) + ")");
          try{
            spanFilling(image, x, y, Color.RED);
            repaint();
          }
          catch(ArrayIndexOutOfBoundsException ex){}
        }
      });
      addMouseMotionListener(new MouseMotionAdapter() {
        @Override
        public void mouseDragged(MouseEvent e) {
          int x = e.getX();
          int y = e.getY();
          int cellX;
          int cellY;
          try{
            spanFilling(image, x, y, Color.RED);
            repaint();
          }
          catch(ArrayIndexOutOfBoundsException ex){}
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
