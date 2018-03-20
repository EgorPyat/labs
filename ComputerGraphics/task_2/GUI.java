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

public class GUI extends JFrame{
  public GUI(){
    super("Filter - The App.");

    AreasPanel areasPanel = new AreasPanel();

    JMenuBar menu = new JMenuBar();
    JMenu mFile = new JMenu("File");
    JMenu mFilter = new JMenu("Filter");
    JMenu mAbout = new JMenu("About");
    JMenuItem mFileNew = new JMenuItem("New");
    JMenuItem mFileSave = new JMenuItem("Save");
    JMenuItem mFileQuit = new JMenuItem("Quit");

    JRadioButtonMenuItem mFilterSelect = new JRadioButtonMenuItem("Select");
    JMenuItem mFilterReset = new JMenuItem("Reset");
    JMenuItem mFilterTransfer = new JMenuItem("Transfer");
    JMenuItem mFilterBNWF = new JMenuItem("BlackNWhite filter");

    JMenuItem mAboutInfo = new JMenuItem("Info");

    mFile.add(mFileNew);
    mFile.add(mFileSave);
    mFile.addSeparator();
    mFile.add(mFileQuit);

    mFilter.add(mFilterSelect);
    mFilter.addSeparator();
    mFilter.add(mFilterReset);
    mFilter.add(mFilterTransfer);
    mFilter.addSeparator();
    mFilter.add(mFilterBNWF);

    mAbout.add(mAboutInfo);

    menu.add(mFile);
    menu.add(mFilter);
    menu.add(mAbout);
    setJMenuBar(menu);

    JToolBar toolBar = new JToolBar();
    try{
      JButton buttonNew = new JButton(new ImageIcon(ImageIO.read(getClass().getResource("new.png"))));
      ActionListener ln = (e) -> {
        JFileChooser c = new JFileChooser(".");
        c.setFileFilter(new FileNameExtensionFilter("Image formats", "jpg", "jpeg", "png"));
        int rVal = c.showOpenDialog(this);
        if(rVal == JFileChooser.APPROVE_OPTION){
          areasPanel.setImage(c.getSelectedFile().getName());
        }
      };
      buttonNew.addActionListener(ln);

      JButton buttonSave = new JButton(new ImageIcon(ImageIO.read(getClass().getResource("save.png"))));
      ActionListener ls = (e) -> {
        JFileChooser c = new JFileChooser(".");
        BufferedImage image = areasPanel.getFilteredImage();
        if(image == null){
          JOptionPane.showMessageDialog(this, "Nothing to save.", "Save warning", JOptionPane.WARNING_MESSAGE);
          return;
        }
        while(true){
          int rVal = c.showSaveDialog(this);
          if(rVal == JFileChooser.APPROVE_OPTION){
            try{
              File selectedFile = c.getSelectedFile();
              if(selectedFile.exists()){
                JOptionPane.showMessageDialog(this, "Selected file exists, choose other name for file.", "Save warning", JOptionPane.WARNING_MESSAGE);
                continue;
              }
              File outputfile = new File(c.getSelectedFile().getName() + ".png");
              ImageIO.write(image, "png", outputfile);
              break;
            }
            catch(IOException ex) {
              System.err.println(ex.getMessage());
            }
          }
          else{
            break;
          }
        }
      };
      buttonSave.addActionListener(ls);

      JButton buttonExit = new JButton(new ImageIcon(ImageIO.read(getClass().getResource("exit.png"))));
      ActionListener le = (e) -> {
        System.exit(0);
      };
      buttonExit.addActionListener(le);

      JToggleButton buttonSelect = new JToggleButton(new ImageIcon(ImageIO.read(getClass().getResource("select.png"))));
      buttonSelect.addItemListener((e) -> {
        if(e.getStateChange() == ItemEvent.SELECTED){
              mFilterSelect.setSelected(true);
              areasPanel.setSelect(true);
          }
          else if(e.getStateChange() == ItemEvent.DESELECTED){
              mFilterSelect.setSelected(false);
              areasPanel.setSelect(false);
          }
      });

      JButton buttonReset = new JButton(new ImageIcon(ImageIO.read(getClass().getResource("reset.png"))));
      ActionListener lr = (e) -> {
        areasPanel.reset();
      };
      buttonReset.addActionListener(lr);

      JButton buttonTransfer = new JButton(new ImageIcon(ImageIO.read(getClass().getResource("transfer.png"))));
      ActionListener lt = (e) -> {
        areasPanel.transfer();
      };
      buttonTransfer.addActionListener(lt);

      JButton buttonAbout = new JButton(new ImageIcon(ImageIO.read(getClass().getResource("about.png"))));
      ActionListener la = (e) -> {
        JOptionPane.showMessageDialog(this,  "Filter - The App.\nVersion 0.1\nBy Egor Pyataev - Group 15206");
      };
      buttonAbout.addActionListener(la);

      JButton blackNwhiteFilter = new JButton(new ImageIcon(ImageIO.read(getClass().getResource("bnw.png"))));
      ActionListener bnwf = (e) -> {
        areasPanel.blackNwhiteFilter();
      };
      blackNwhiteFilter.addActionListener(bnwf);

      toolBar.add(buttonNew);
      toolBar.add(buttonSave);
      toolBar.add(buttonExit);
      toolBar.addSeparator();
      toolBar.add(buttonSelect);
      toolBar.add(buttonReset);
      toolBar.add(buttonTransfer);
      toolBar.addSeparator();
      toolBar.add(buttonAbout);
      toolBar.addSeparator();
      toolBar.add(blackNwhiteFilter);

      mFileNew.addActionListener(ln);
      mFileSave.addActionListener(ls);
      mFileQuit.addActionListener(le);

      mFilterSelect.addItemListener((e) -> {
        if(e.getStateChange() == ItemEvent.SELECTED){
          buttonSelect.setSelected(true);
        }
        else if(e.getStateChange() == ItemEvent.DESELECTED){
          buttonSelect.setSelected(false);
        }
      });
      mFilterReset.addActionListener(lr);
      mFilterTransfer.addActionListener(lt);
      mFilterBNWF.addActionListener(bnwf);

      mAboutInfo.addActionListener(la);
    }
    catch(Exception ex){
      System.err.println(ex.getMessage());
    }
    toolBar.setFloatable(false);
    toolBar.setRollover(true);

    add(toolBar, BorderLayout.PAGE_START);

    JScrollPane scrollpane = new JScrollPane(areasPanel);
    setPreferredSize(new Dimension(1123, 600));
    add(scrollpane, BorderLayout.CENTER);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    pack();
    setLocationRelativeTo(null);
    setVisible(true);
  }
}

class AreasPanel extends JPanel{
  private BufferedImage image = null;
  private BufferedImage subimage = null;
  private BufferedImage filteredImage = null;
  private int X = 0;
  private int Y = 0;
  private double scale;
  private Rectangle imageBounds;
  private Rectangle selector;
  private boolean dragging;
  private boolean visible;
  private boolean selected = false;

  public void blackNwhiteFilter(){
    if(subimage == null){
      JOptionPane.showMessageDialog(this, "Nothing to filter.", "Filter warning", JOptionPane.WARNING_MESSAGE);
      return;
    }

    int alpha, red, green, blue;
    int newPixel;

    filteredImage = new BufferedImage(subimage.getWidth(), subimage.getHeight(), subimage.getType());

    for(int i = 0; i < subimage.getWidth(); i++) {
      for(int j = 0; j < subimage.getHeight(); j++) {
        alpha = new Color(subimage.getRGB(i, j)).getAlpha();
        red = new Color(subimage.getRGB(i, j)).getRed();
        green = new Color(subimage.getRGB(i, j)).getGreen();
        blue = new Color(subimage.getRGB(i, j)).getBlue();
        newPixel = (red + green + blue) / 3;
        newPixel = (new Color(newPixel, newPixel, newPixel, alpha)).getRGB();
        filteredImage.setRGB(i, j, newPixel);
      }
    }
    repaint();
  }

  public BufferedImage getFilteredImage(){
    return filteredImage;
  }

  public void setSelect(boolean select){
    selected = select;
  }

  public void reset(){
    subimage = null;
    filteredImage = null;
    repaint();
  }

  public void transfer(){
    subimage = filteredImage;
    repaint();
  }

  public void setImage(String name){
    try{
      image = ImageIO.read(getClass().getResource(name));
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
      selector = new Rectangle(0, 0, imgW, imgH);
    }
    else{
      selector = new Rectangle(0, 0, (int)(350 * scale), (int)(350 * scale));
    }
    int w = (int)(imgW * scale);
    int h = (int)(imgH * scale);
    imageBounds = new Rectangle(15, 15, w, h);
    subimage = null;
    filteredImage = null;
    repaint();
  }

  public AreasPanel(){
    setPreferredSize(new Dimension(1110, 450));

    addMouseListener(new MouseAdapter(){
      @Override
      public void mousePressed(MouseEvent e){
        Point p = e.getPoint();
        if(imageBounds != null && selected && imageBounds.contains(p)){
          visible = true;
          dragging = true;
          int x = e.getX() - selector.width / 2;
          int y = e.getY() - selector.height / 2;
          setSelector(x, y);
          X = (int)((selector.x - 15) / scale);
          Y = (int)((selector.y - 15) / scale);
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
          }
          if(X + sideX > image.getWidth()){
            shiftX = image.getWidth() - X - sideX;
          }
          try{
            subimage = image.getSubimage(X + shiftX, Y + shiftY, sideX, sideY);
          }
          catch(Exception ex){
            System.out.println(ex.getMessage());
          }
          repaint();
        }
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
        Point p = e.getPoint();
        if(imageBounds != null && selected && imageBounds.contains(p)){
          if(dragging){
            int x = e.getX();
            int y = e.getY();
            setSelector(x - selector.width / 2, y - selector.height / 2);
            X = (int)((selector.x - 15) / scale);
            Y = (int)((selector.y - 15) / scale);
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
            }
            if(X + sideX > image.getWidth()){
              System.out.println(image.getWidth());
              shiftX = image.getWidth() - X - sideX;
            }
            try{
              subimage = image.getSubimage(X + shiftX, Y + shiftY, sideX, sideY);
            }
            catch(Exception ex){
              System.out.println(ex.getMessage());
            }
            repaint();
          }
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

    if(image != null){
      if(image.getHeight() > 350 || image.getWidth() > 350){
        g.drawImage(image, 15, 15, (int)(image.getWidth() * scale), (int)(image.getHeight() * scale), null);
      }
      else{
        g.drawImage(image, 15, 15, null);
      }
      g.drawImage(subimage, 380, 15, null);
      g.drawImage(filteredImage, 745, 15, null);

      g.setXORMode(Color.WHITE);
      Graphics2D g2d = (Graphics2D) g.create();
      Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{3}, 0);
      g2d.setStroke(dashed);
      if(visible)g2d.draw(selector);
    }
  }
}
