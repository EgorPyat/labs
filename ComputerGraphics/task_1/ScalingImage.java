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
import java.awt.event.ItemEvent;
import java.awt.Point;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import javax.imageio.ImageIO;

public class ScalingImage extends JFrame{
  private MainPanel mainPanel = null;
  private Timer timer = null;

  public ScalingImage(){
    super("Life - The game.");
    JMenuBar menu = new JMenuBar();
    JMenu mFile = new JMenu("File");
    JMenu mGame = new JMenu("Game");
    JMenu mAbout = new JMenu("About");
    JMenuItem mFileNew = new JMenuItem("New");
    JMenuItem mFileSave = new JMenuItem("Save");
    JMenuItem mFileImport = new JMenuItem("Import");
    JMenuItem mFileQuit = new JMenuItem("Quit");
    JMenuItem mAboutInfo = new JMenuItem("Info");
    JMenuItem mGameStep = new JMenuItem("Step");
    JMenuItem mGameRun = new JMenuItem("Run");
    JMenu     mGameMode = new JMenu("Mode");
    JRadioButtonMenuItem mGameModeXOR = new JRadioButtonMenuItem("XOR");
    JRadioButtonMenuItem mGameModeReplace = new JRadioButtonMenuItem("Replace");
    JMenuItem mGameClearField = new JMenuItem("Clear field");
    JMenuItem mGameSettings = new JMenuItem("Settings");

    mFileQuit.addActionListener((e) -> System.exit(0));
    mAboutInfo.addActionListener((e) -> JOptionPane.showMessageDialog(this,  "Life - The Game.\nBy Egor Pyataev"));
    mFile.add(mFileNew);
    mFile.add(mFileSave);
    mFile.add(mFileImport);
    mFile.addSeparator();
    mFile.add(mFileQuit);
    mGame.add(mGameStep);
    mGame.add(mGameRun);
    mGame.addSeparator();
    mGameMode.add(mGameModeXOR);
    mGameMode.add(mGameModeReplace);
    mGame.add(mGameMode);
    mGame.addSeparator();
    mGame.add(mGameClearField);
    mGame.addSeparator();
    mGame.add(mGameSettings);
    mAbout.add(mAboutInfo);
    menu.add(mFile);
    menu.add(mGame);
    menu.add(mAbout);
    setJMenuBar(menu);
    JToolBar toolBar = new JToolBar();

    try{
      JButton buttonNew = new JButton(new ImageIcon(ImageIO.read(getClass().getResource("new.png"))));
      buttonNew.setSize(new Dimension(32, 32));
      buttonNew.addActionListener((e) -> {
        if(timer == null){
          JDialog dialog = new JDialog(this, "New Game", true);
          dialog.setLayout(new FlowLayout());
          TextField height = new TextField("6", 20);
          TextField width = new TextField("10" , 20);
          TextField radius = new TextField("48" , 20);
          TextField thickness = new TextField("1" , 20);
          JButton submit = new JButton("Sumbit");
          submit.addActionListener((d) -> {
            getContentPane().remove(mainPanel);
            mainPanel = new MainPanel(new HexahedronGrid(Integer.valueOf(height.getText()), Integer.valueOf(width.getText()), Integer.valueOf(radius.getText())));
            add(mainPanel);
            mainPanel.draw();
            pack();
            setLocationRelativeTo(null);
            dialog.dispose();
          });
          dialog.add(height);
          dialog.add(width);
          dialog.add(radius);
          dialog.add(thickness);
          dialog.add(submit);

          dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
          dialog.setSize(200, 220);
          dialog.setLocationRelativeTo(this);
          dialog.setVisible(true);
        }
      });

      JButton buttonSave = new JButton(new ImageIcon(ImageIO.read(getClass().getResource("save.png"))));
      buttonSave.setSize(new Dimension(32, 32));

      JButton buttonImport = new JButton(new ImageIcon(ImageIO.read(getClass().getResource("import.png"))));
      buttonImport.setSize(new Dimension(32, 32));

      JButton buttonStep = new JButton(new ImageIcon(ImageIO.read(getClass().getResource("step.png"))));
      buttonStep.setSize(new Dimension(32, 32));
      buttonStep.addActionListener((e) -> {
        if(timer == null){
          mainPanel.stepChange();
        }
      });

      JToggleButton buttonRun = new JToggleButton(new ImageIcon(ImageIO.read(getClass().getResource("run.png"))));
      buttonRun.setSize(new Dimension(32, 32));
      buttonRun.addItemListener((e) -> {
        if(e.getStateChange() == ItemEvent.SELECTED){
          System.out.println("select");
          timer = new Timer(true);
          timer.schedule(new TimerTask(){
            @Override
            public void run(){
              mainPanel.stepChange();
              if(mainPanel.stopChanging()){
                ((JToggleButton)e.getItem()).setSelected(false);
              }
            };
          }, 600, 600);
        }
        else if(e.getStateChange() == ItemEvent.DESELECTED){
          timer.cancel();
          timer = null;
          System.out.println("deselect");
        }
      });

      JToggleButton buttonMode = new JToggleButton(new ImageIcon(ImageIO.read(getClass().getResource("mode.png"))));
      buttonMode.setSize(new Dimension(32, 32));
      buttonMode.addItemListener((e) -> {
        if(timer == null){
          mainPanel.changeMode();
        }
      });

      JButton buttonClear = new JButton(new ImageIcon(ImageIO.read(getClass().getResource("clear.png"))));
      buttonClear.setSize(new Dimension(32, 32));
      buttonClear.addActionListener((e) -> {
        if(timer == null){
          mainPanel.clear();
        }
      });

      JButton buttonSettings = new JButton(new ImageIcon(ImageIO.read(getClass().getResource("settings.png"))));
      buttonSettings.setSize(new Dimension(32, 32));

      JButton buttonAbout = new JButton(new ImageIcon(ImageIO.read(getClass().getResource("about.png"))));
      buttonAbout.setSize(new Dimension(32, 32));
      buttonAbout.addActionListener((e) -> JOptionPane.showMessageDialog(this,  "Life - The Game.\nBy Egor Pyataev"));

      JButton buttonExit = new JButton(new ImageIcon(ImageIO.read(getClass().getResource("exit.png"))));
      buttonExit.setSize(new Dimension(32, 32));
      buttonExit.addActionListener((e) -> System.exit(0));

      toolBar.add(buttonNew);
      toolBar.add(buttonSave);
      toolBar.add(buttonImport);
      toolBar.add(buttonExit);
      toolBar.addSeparator();
      toolBar.add(buttonStep);
      toolBar.add(buttonRun);
      toolBar.add(buttonMode);
      toolBar.add(buttonClear);
      toolBar.add(buttonSettings);
      toolBar.addSeparator();
      toolBar.add(buttonAbout);
    }
    catch(IOException e){}

    toolBar.setFloatable(false);
    toolBar.setRollover(true);

    add(toolBar, BorderLayout.PAGE_START);
    mainPanel = new MainPanel(new HexahedronGrid(6, 10, 48));
    add(mainPanel);
    mainPanel.draw();
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    pack();
    setLocationRelativeTo(null);
    setVisible(true);
  }
}

class MainPanel extends JPanel {
  private JScrollPane scrollpane;
  private ImagePanel imagePanel;

  public void changeMode(){
    imagePanel.changeMode();
  }
  public boolean stopChanging(){
    return imagePanel.stopChanging();
  }
  public void stepChange(){
    imagePanel.stepChange();
  }
  public void clear(){
    imagePanel.clear();
  }
  public void draw(){
    imagePanel.draw();
  }
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
  private Color defColor;
  private Point lastCell;
  private boolean xor = false;

  public void changeMode(){
    this.xor = !this.xor;
  }

  public boolean stopChanging(){
    return this.field.isExtinction();
  }

  public void stepChange(){
    field.stepChange();
    this.draw();
  }

  public void clear(){
    this.field.clearField();
    this.draw();
  }

  public void draw(){
    drawHexahedronGrid();
    repaint();
  }

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
    Hexahedron[][] f = this.field.getField();
    for(int i = 0; i < this.field.getHexHeight(); i++){
      for(int j = 0; j < this.field.getHexWidth(); j++){
        drawHexahedron(g, f[i][j]);
        Point p = f[i][j].getCenter();
        spanFilling(this.image, (int)p.getX(), (int)p.getY(), f[i][j].isAlive() ? Color.RED : this.defColor);
      }
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
    this.defColor = this.image.getGraphics().getColor();
    setPreferredSize(new Dimension(initWidth, initHeight));
    int R = this.field.getHexRadius();
    int yl = (int)(R * Math.sqrt(3) / 2);
    int yl2 = yl + yl;

    addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        if(image.getRGB(x, y) == 0 || image.getRGB(x, y) == Color.BLACK.getRGB()){
          return;
        }
        Point p = getCenterCoords(x, y);
        int cellX = ((int)p.getX() - 20) / (R + R / 2);
        int cellY = ((int)p.getY() - 20) / (yl2);
        lastCell = new Point(cellY, cellX);
        System.out.println("(" + cellY + ", " + cellX + ")" + "(" + (y - 20) + ", " + (x - 20) + ")" + " (" + (p.getY() - 20) + ", " + (p.getX() - 20) + ")");
        try{
          Hexahedron h = field.getField()[cellY][cellX];
          if(h.isAlive() && xor){
            spanFilling(image, x, y, defColor);
            h.changeStatus();
          }
          else if(!h.isAlive()){
            spanFilling(image, x, y, Color.RED);
            h.changeStatus();
          }
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
      if(image.getRGB(x, y) == 0 || image.getRGB(x, y) == Color.BLACK.getRGB()){
        return;
      }
      Point p = getCenterCoords(x, y);
      int cellX = ((int)p.getX() - 20) / (R + R / 2);
      int cellY = ((int)p.getY() - 20) / (yl2);
      Point temp = new Point(cellY, cellX);
      if(temp.equals(lastCell)) return;
      else lastCell = temp;
      try{
        Hexahedron h = field.getField()[cellY][cellX];
        if(h.isAlive() && xor){
          spanFilling(image, x, y, defColor);
          h.changeStatus();
        }
        else if(!h.isAlive()){
          spanFilling(image, x, y, Color.RED);
          h.changeStatus();
        }
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
