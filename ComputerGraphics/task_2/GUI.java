import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import java.net.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.*;

public class GUI extends JFrame{
  public GUI(){
    super("Filters.");

    JMenuBar menu = new JMenuBar();
    JMenu mFile = new JMenu("File");
    JMenu mFilter = new JMenu("Filter");
    JMenu mAbout = new JMenu("About");

    menu.add(mFile);
    menu.add(mFilter);
    menu.add(mAbout);
    setJMenuBar(menu);

    JScrollPane scrollpane = new JScrollPane(new AreasPanel());
    setLayout(new BorderLayout());
    setPreferredSize(new Dimension(800, 600));
    add(scrollpane, BorderLayout.CENTER);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    pack();
    setLocationRelativeTo(null);
    setVisible(true);
  }
}

class AreasPanel extends JPanel{
  private BufferedImage image;
  private BufferedImage subimage;
  private int X = 0;
  private int Y = 0;
  private double scale;
  private Rectangle imageBounds;
  private Rectangle selector;
  private boolean dragging;
  private boolean visible;

  public AreasPanel(){
    setPreferredSize(new Dimension(1110, 400));

    try{
      image = ImageIO.read(getClass().getResource("m.jpg"));
    }
    catch(IOException ex) {
      System.err.println(ex.getMessage());
    }

    int imgH = image.getHeight();
    int imgW = image.getWidth();

    double scaleH = 350 / (double)imgH;
    double scaleW = 350 / (double)imgW;
    scale = scaleH < scaleW ? scaleH : scaleW;
    if(scale >= 1.0){
      scale = 1.0;
    }
    if(scale == 1.0){
      // System.out.println("1.0");
      selector = new Rectangle(0, 0, imgW, imgH);
    }
    else{
      selector = new Rectangle(0, 0, (int)(350 * scale), (int)(350 * scale));
    }
    // System.out.println(scale);
    int w = (int)(imgW * scale);
    int h = (int)(imgH * scale);
    imageBounds = new Rectangle(15, 15, w, h);

    addMouseListener(new MouseAdapter(){
      @Override
      public void mousePressed(MouseEvent e){
        visible = true;
        dragging = true;
        int x = e.getX() - selector.width / 2;
        int y = e.getY() - selector.height / 2;
        setSelector(x, y);
        X = (int)((selector.x - 15) / scale);
        Y = (int)((selector.y - 15) / scale);
        System.out.println(X + " " + Y);
        int shiftX = 0;
        int shiftY = 0;
        int sideX = 350;
        int sideY = 350;
        if(scale == 1.0){
          sideX = image.getWidth();
          sideY = image.getHeight();
        }
        if(Y + sideY > image.getHeight()){
          shiftY = image.getHeight() - Y - sideY;
          System.out.println("shiftY " + shiftY);
        }
        if(X + sideX > image.getWidth()){
          System.out.println("shiftX " + shiftX);
          shiftX = image.getWidth() - X - sideX;
        }
        try{
          // System.out.println(X + " " + Y);
          subimage = image.getSubimage(X + shiftX, Y + shiftY, sideX, sideY);
        }
        catch(Exception ex){
          System.out.println(ex.getMessage());
        }
        repaint();
      }

      @Override
      public void mouseReleased(MouseEvent e){
        dragging = false;
        visible = false;
        repaint();
      }
    });
    addMouseMotionListener(new MouseMotionAdapter(){
      @Override
      public void mouseDragged(MouseEvent e){
        if(dragging){
          int x = e.getX();
          int y = e.getY();
          setSelector(x - selector.width / 2, y - selector.height / 2);
          X = (int)((selector.x - 15) / scale);
          Y = (int)((selector.y - 15) / scale);
          System.out.println(X + " " + Y);

          // System.out.println((int)((selector.y) / scale));
          int shiftX = 0;
          int shiftY = 0;
          int sideX = 350;
          int sideY = 350;
          if(scale == 1.0){
            sideX = image.getWidth();
            sideY = image.getHeight();
          }
          if(Y + sideY > image.getHeight()){
            shiftY = image.getHeight() - Y - sideY;
            System.out.println("shiftY " + shiftY);
          }
          if(X + sideX > image.getWidth()){
            System.out.println(image.getWidth());
            shiftX = image.getWidth() - X - sideX;
            System.out.println("shiftX " + shiftX);
          }
          try{
            // System.out.println(X + " " + Y);
            subimage = image.getSubimage(X + shiftX, Y + shiftY, sideX, sideY);
          }
          catch(Exception ex){
            System.out.println(ex.getMessage());
          }
          repaint();
        }
      }
    });

  }

  private Point getLegalLocation(int x, int y){
    if(x < imageBounds.x){
      x = imageBounds.x;
    }
    if(x + selector.width > imageBounds.x + imageBounds.width){
      x = imageBounds.x + imageBounds.width - selector.width;
    }
    if(y < imageBounds.y){
      y = imageBounds.y;
    }
    if(y + selector.height > imageBounds.y + imageBounds.height){
      y = imageBounds.y + imageBounds.height - selector.height;
    }

    return new Point(x, y);
  }

  public Point setSelector(int x, int y){
    Point p = getLegalLocation(x, y);
    selector.setLocation(p.x, p.y);
    return p;
  }

  public void drawDashedLine(Graphics g, int x1, int y1, int x2, int y2){
    Graphics2D g2d = (Graphics2D) g.create();
    Stroke dashed = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{5}, 0);
    g2d.setStroke(dashed);
    g2d.drawLine(x1, y1, x2, y2);
  }

  @Override
  public void paint(Graphics g){
    super.paint(g);
    g.setColor(Color.DARK_GRAY);
    drawDashedLine(g, 15, 15, 365, 15);
    drawDashedLine(g, 380, 15, 730, 15);
    drawDashedLine(g, 745, 15, 1095, 15);
    drawDashedLine(g, 15, 365, 365, 365);
    drawDashedLine(g, 380, 365, 730, 365);
    drawDashedLine(g, 745, 365, 1095, 365);
    drawDashedLine(g, 15, 15, 15, 365);
    drawDashedLine(g, 365, 15, 365, 365);
    drawDashedLine(g, 380, 15, 380, 365);
    drawDashedLine(g, 730, 15, 730, 365);
    drawDashedLine(g, 745, 15, 745, 365);
    drawDashedLine(g, 1095, 15, 1095, 365);
    if(image.getHeight() > 350 || image.getWidth() > 350){
      g.drawImage(image, 15, 15, (int)(image.getWidth() * scale), (int)(image.getHeight() * scale), null);
    }
    else{
      g.drawImage(image, 15, 15, null);
    }
    g.drawImage(subimage, 380, 15, null);
    g.setXORMode(Color.WHITE);
    Graphics2D g2d = (Graphics2D) g.create();
    Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{3}, 0);
    g2d.setStroke(dashed);
    if(visible)g2d.draw(selector);
  }
}
