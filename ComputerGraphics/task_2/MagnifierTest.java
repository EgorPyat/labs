import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import java.net.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.*;

public class MagnifierTest{
  MagnifierPanel magnifierPanel;
  MagnifierControl control;

  public MagnifierTest(){
    magnifierPanel = new MagnifierPanel();
    control = new MagnifierControl(magnifierPanel);
    magnifierPanel.addMouseListener(control);
    magnifierPanel.addMouseMotionListener(control);
    JFrame f = new JFrame();
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.getContentPane().add(new JScrollPane(magnifierPanel));
    f.getContentPane().add(getUIPanel(), "South");
    f.setSize(400,400);
    f.setLocation(200,200);
    f.setVisible(true);
  }

  private JPanel getUIPanel(){
    final JLabel label = new JLabel("scale = 2.0");
    int value = (int)(magnifierPanel.scale * 2);
    final JSlider slider = new JSlider(JSlider.HORIZONTAL, 1, 8, value);
    slider.addChangeListener(new ChangeListener(){
        public void stateChanged(ChangeEvent e){
            float value = slider.getValue() / 2f;
            label.setText("scale = " + value);
            magnifierPanel.setScale(value);
        }
    });
    JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(2,2,2,2);
    gbc.weightx = 1.0;
    panel.add(label, gbc);
    panel.add(slider, gbc);
    return panel;
  }

  public static void main(String[] args){
    new MagnifierTest();
  }
}

class MagnifierPanel extends JPanel{
  BufferedImage image;
  Rectangle viewer;
  Rectangle imageBounds;
  float scale;
  Dimension targetSize;

  public MagnifierPanel(){
    loadImage();
    viewer = new Rectangle(0, 0, 160, 160);
    imageBounds = new Rectangle(0, 0, image.getWidth(), image.getHeight());
    setScale(2.0f);
    setLayout(null);
    addComponentListener(new ComponentAdapter(){
      public void componentResized(ComponentEvent e){
        positionViewerAndImage();
      }
    });
  }

  protected void paintComponent(Graphics g){
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D)g;
    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
    int w = getWidth();
    int h = getHeight();
    int imageWidth = image.getWidth();
    int imageHeight = image.getHeight();
    int x = (w - imageWidth) / 2;
    int y = (h - imageHeight) / 2;
    g2.drawImage(image, x, y, this);
    BufferedImage viewImage = getMagnifiedImage();
    if(viewImage != null) g2.drawImage(viewImage, 500, 500, this);
    g2.setPaint(Color.red);
    //g2.drawLine(w/2, 0, w/2, h);  // show center
    //g2.drawLine(0, h/2, w, h/2);  //   lines
    g2.setStroke(new BasicStroke(2f));
    g2.draw(viewer);
  }

  private BufferedImage getMagnifiedImage(){
    Point p = getSubimageLocation();
    int w = targetSize.width;
    int h = targetSize.height;
    BufferedImage target = null;
    try{
      target = image.getSubimage(p.x, p.y, w, h);
    }
    catch(RasterFormatException rfe){
      System.out.println("viewer out of bounds: " + rfe.getMessage() + "\n" + "\tx = " + p.x + "\t(x + w) = " + (p.x + w) + "\n" + "\ty = " + p.y + "\t(y + h) = " + (p.y + h));
      return target;
    }

    w = viewer.width;
    h = viewer.height;
    BufferedImage view = new BufferedImage(w, h, image.getType());
    Graphics2D g2 = view.createGraphics();
    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
    AffineTransform at = AffineTransform.getScaleInstance(scale, scale);
    g2.drawRenderedImage(target, at);
    g2.dispose();

    return view;
  }

  private Point getSubimageLocation(){
    int w = targetSize.width;
    int h = targetSize.height;
    int x = viewer.x - imageBounds.x + (viewer.width - w) / 2;
    int y = viewer.y - imageBounds.y + (viewer.height - h) / 2;
    if(x + w > image.getWidth()) x = image.getWidth() - w;
    if(y + h > image.getHeight()) y = image.getHeight() - h;

    return new Point(x, y);
  }

  public Dimension getPreferredSize(){
    int w = image.getWidth() + (scale > 1.0f ? viewer.width  - targetSize.width  : 0);
    int h = image.getHeight() + (scale > 1.0f ? viewer.height - targetSize.height : 0);

    return new Dimension(w, h);
  }

  public void setScale(float f){
    scale = f;
    int width  = (int)(viewer.width / scale);
    int height = (int)(viewer.height / scale);
    targetSize = new Dimension(width, height);
    positionViewerAndImage();
    revalidate();
    repaint();
  }

  private void positionViewerAndImage(){
    int w = getWidth();
    int h = getHeight();
    int x = (w - viewer.width) / 2;
    int y = (h - viewer.height) / 2;
    viewer.setLocation(x, y);
    x = (w - image.getWidth()) / 2;
    y = (h - image.getHeight()) / 2;
    imageBounds.setLocation(x, y);
    repaint();
  }

  public void setViewer(int x, int y){
    Point p = getLegalLocation(x, y);
    viewer.setLocation(p.x, p.y);
    repaint();
  }

  private Point getLegalLocation(int x, int y){
    int deltaW = (viewer.width - targetSize.width) / 2;
    int deltaH = (viewer.height - targetSize.height) / 2;
    if(x + deltaW < imageBounds.x){
      x = imageBounds.x - deltaW;
    }
    if(x + viewer.width - deltaW > imageBounds.x + imageBounds.width){
      x = imageBounds.x + imageBounds.width - viewer.width + deltaW;
    }
    if(y + deltaH < imageBounds.y){
      y = imageBounds.y - deltaH;
    }
    if(y + viewer.height - deltaH > imageBounds.y + imageBounds.height){
      y = imageBounds.y + imageBounds.height - viewer.height + deltaH;
    }

    return new Point(x, y);
  }

  private void loadImage(){
    String fileName = "p.jpeg";
    try{
        URL url = getClass().getResource(fileName);
        image = ImageIO.read(url);
    }
    catch(MalformedURLException mue){
        System.err.println("url: " + mue.getMessage());
    }
    catch(IOException ioe){
        System.err.println("read: " + ioe.getMessage());
    }
  }
}

class MagnifierControl extends MouseInputAdapter{
    MagnifierPanel magnifierPanel;
    Point offset;
    boolean dragging;

    public MagnifierControl(MagnifierPanel mp){
        magnifierPanel = mp;
        offset = new Point();
        dragging = false;
    }

    public void mousePressed(MouseEvent e){
        Point p = e.getPoint();
        if(magnifierPanel.viewer.contains(p))
        {
            offset.x = p.x - magnifierPanel.viewer.x;
            offset.y = p.y - magnifierPanel.viewer.y;
            dragging = true;
        }
    }

    public void mouseReleased(MouseEvent e){
        dragging = false;
    }

    public void mouseDragged(MouseEvent e){
        if(dragging){
            int x = e.getX() - offset.x;
            int y = e.getY() - offset.y;
            magnifierPanel.setViewer(x, y);
        }
    }
}
