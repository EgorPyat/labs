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
    super("Photojop.");

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
    add(scrollpane, BorderLayout .CENTER);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    pack();
    setLocationRelativeTo(null);
    setVisible(true);
  }
}

class AreasPanel extends JPanel{
  private BufferedImage image;
  private double scale;
  private Rectangle imageBounds;
  private Rectangle selector;
  private boolean dragging;

  public AreasPanel(){
    setPreferredSize(new Dimension(1110, 400));

    try{
      image = ImageIO.read(getClass().getResource("p.jpeg"));
    }
    catch(IOException ex) {
      System.err.println(ex.getMessage());
    }


    int imgH = image.getHeight();
    int imgW = image.getWidth();

    double scaleH = 350 / (double)imgH;
    double scaleW = 350 / (double)imgW;
    scale = scaleH < scaleW ? scaleH : scaleW;

    selector = new Rectangle(0, 0, (int)(350 * scale), (int)(350 * scale));
    int w = (int)(imgW * scale);
    int h = (int)(imgH * scale);
    imageBounds = new Rectangle(15, 15, w, h);
    int x = (w) / 2 + 15;
    int y = (h) / 2 + 15;
    System.out.println((w) + " " + (h ));
    selector.setLocation(x - selector.width / 2, y - selector.height / 2);


    Point offset = new Point();
    addMouseListener(new MouseAdapter(){
      @Override
      public void mousePressed(MouseEvent e){
        Point p = e.getPoint();
        if(selector.contains(p)){
          offset.x = p.x - selector.x;
          offset.y = p.y - selector.y;
          dragging = true;
        }
      }

      @Override
      public void mouseReleased(MouseEvent e){
        dragging = false;
      }
    });
    addMouseMotionListener(new MouseMotionAdapter(){
      @Override
      public void mouseDragged(MouseEvent e){
        if(dragging){
          int x = e.getX() - offset.x;
          int y = e.getY() - offset.y;
          setSelector(x, y);
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

  public void setSelector(int x, int y){
    Point p = getLegalLocation(x, y);
    selector.setLocation(p.x, p.y);
    repaint();
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
    g.setXORMode(Color.GRAY);
    Graphics2D g2d = (Graphics2D) g.create();
    Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{3}, 0);
    g2d.setStroke(dashed);
    g2d.draw(selector);

  }
}
