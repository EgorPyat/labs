// Рассказать об обьемной визуализации
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import javax.swing.*;
import java.awt.geom.Ellipse2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.ItemEvent;
import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;
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
    JRadioButtonMenuItem mGameImpacts = new JRadioButtonMenuItem("Show impacts");
    JRadioButtonMenuItem mGameModeReplace = new JRadioButtonMenuItem("Replace", true);
    JRadioButtonMenuItem mGameModeXOR = new JRadioButtonMenuItem("XOR");
    JMenuItem mGameClearField = new JMenuItem("Clear field");
    JMenuItem mGameSettings = new JMenuItem("Settings");

    Object o = new Object();
    mFileQuit.addActionListener((e) -> System.exit(0));
    mAboutInfo.addActionListener((e) -> JOptionPane.showMessageDialog(this,  "Life - The Game.\nBy Egor Pyataev"));

    ButtonGroup bg = new ButtonGroup();
    bg.add(mGameModeReplace);
    bg.add(mGameModeXOR);

    mFile.add(mFileNew);
    mFile.add(mFileSave);
    mFile.add(mFileImport);
    mFile.addSeparator();
    mFile.add(mFileQuit);
    mGame.add(mGameStep);
    mGame.add(mGameRun);
    mGame.addSeparator();
    mGameMode.add(mGameModeReplace);
    mGameMode.add(mGameModeXOR);
    mGame.add(mGameMode);
    mGame.add(mGameImpacts);
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
          JPanel inFieldPane = new JPanel();
          inFieldPane.setLayout(new GridLayout(4, 2));
          TextField height = new TextField("6", 10);
          TextField width = new TextField("10" , 10);
          TextField radius = new TextField("48" , 10);
          TextField thickness = new TextField("2" , 10);
          JButton submit = new JButton("Sumbit");
          submit.addActionListener((d) -> {
            getContentPane().remove(mainPanel);
            mainPanel = new MainPanel(new HexahedronGrid(Integer.valueOf(height.getText()), Integer.valueOf(width.getText()), Integer.valueOf(radius.getText()), Integer.valueOf(thickness.getText())));
            add(mainPanel);
            mainPanel.draw();
            pack();
            dialog.dispose();
          });
          inFieldPane.add(new JLabel("Height"));
          inFieldPane.add(height);
          inFieldPane.add(new JLabel("Width"));
          inFieldPane.add(width);
          inFieldPane.add(new JLabel("Radius"));
          inFieldPane.add(radius);
          inFieldPane.add(new JLabel("Thickness"));
          inFieldPane.add(thickness);
          dialog.add(inFieldPane, BorderLayout.NORTH);
          dialog.add(submit, BorderLayout.SOUTH);

          dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
          dialog.pack();
          dialog.setLocationRelativeTo(this);
          dialog.setVisible(true);
        }
      });
      buttonNew.setToolTipText("Create new field");

      JButton buttonSave = new JButton(new ImageIcon(ImageIO.read(getClass().getResource("save.png"))));
      buttonSave.setSize(new Dimension(32, 32));
      buttonSave.addActionListener((e) -> {
        if(timer == null){
          JFileChooser c = new JFileChooser(".");
          int rVal = c.showSaveDialog(this);
          if (rVal == JFileChooser.APPROVE_OPTION) {
            try(FileWriter fw = new FileWriter(c.getSelectedFile())) {
              int[] settings = mainPanel.getSettings();
              LinkedList<Point> ac = mainPanel.getCells();
              fw.write(settings[1] + " " + settings[0] + "\n");
              fw.write(settings[3] + "\n");
              fw.write(settings[2] + "\n");
              fw.write(ac.size() + "\n");
              for (Point el : ac) {
                fw.write((int)el.getX() + " " + (int)el.getY() + "\n");
              }
            }
            catch (Exception ex) {
              System.out.println(ex.getMessage());
            }
          }
          if (rVal == JFileChooser.CANCEL_OPTION) {
            System.out.println("canceled");
          }
        }
      });
      buttonSave.setToolTipText("Save field state");

      JButton buttonImport = new JButton(new ImageIcon(ImageIO.read(getClass().getResource("import.png"))));
      buttonImport.setSize(new Dimension(32, 32));
      buttonImport.addActionListener((e) -> {
        if(timer == null){
          JFileChooser c = new JFileChooser(".");
          int rVal = c.showOpenDialog(this);
          if (rVal == JFileChooser.APPROVE_OPTION) {
            int i = 0;
            int N = 6;
            int M = 10;
            int thickness = 2;
            int radius = 48;
            LinkedList<Point> ac = new LinkedList<Point>();
            try(BufferedReader br = new BufferedReader (new FileReader(c.getSelectedFile()))){
              while(true){
                String line = br.readLine();
                if(line == null) break;
                String[] ars = line.split(" ");
                if(i == 0){
                  M = Integer.valueOf(ars[0]);
                  N = Integer.valueOf(ars[1]);
                }
                else if(i == 1){
                  thickness = Integer.valueOf(ars[0]);
                }
                else if(i == 2){
                  radius = Integer.valueOf(ars[0]);
                }
                else if(i == 3){
                  ++i;
                  continue;
                }
                else{
                  ac.add(new Point(Integer.valueOf(ars[0]), Integer.valueOf(ars[1])));
                }
                ++i;
              }
              getContentPane().remove(mainPanel);
              mainPanel = new MainPanel(new HexahedronGrid(N, M, radius, thickness));
              mainPanel.setCells(ac);
              add(mainPanel);
              mainPanel.draw();
              pack();
            }
            catch(Exception ex){
              System.out.println(ex.getMessage());
            }
          }
          if (rVal == JFileChooser.CANCEL_OPTION) {
            System.out.println("canceled");
          }
        }
      });
      buttonImport.setToolTipText("Import field state from file");

      JButton buttonStep = new JButton(new ImageIcon(ImageIO.read(getClass().getResource("step.png"))));
      buttonStep.setSize(new Dimension(32, 32));
      buttonStep.addActionListener((e) -> {
        if(timer == null){
          mainPanel.stepChange();
        }
      });
      buttonStep.setToolTipText("Change field state by one step");

      JToggleButton buttonRun = new JToggleButton(new ImageIcon(ImageIO.read(getClass().getResource("run.png"))));
      buttonRun.setSize(new Dimension(32, 32));
      buttonRun.addItemListener((e) -> {
        if(e.getStateChange() == ItemEvent.SELECTED){
          timer = new Timer(true);
            timer.schedule(new TimerTask(){
            @Override
            public void run(){
              synchronized(o){
                mainPanel.stepChange();
              }
              if(mainPanel.stopChanging()){
                ((JToggleButton)e.getItem()).setSelected(false);
              }
            };
          }, mainPanel.getSpeed(), mainPanel.getSpeed());
        }
        else if(e.getStateChange() == ItemEvent.DESELECTED){
          timer.cancel();
          timer = null;
          System.out.println("deselect");
        }
      });
      buttonRun.setToolTipText("Change field state continuously");

      JToggleButton buttonMode = new JToggleButton(new ImageIcon(ImageIO.read(getClass().getResource("mode.png"))));
      buttonMode.setSize(new Dimension(32, 32));
      buttonMode.addItemListener((e) -> {
        mainPanel.changeMode();
      });
      buttonMode.setToolTipText("Change cell filling mode");

      JToggleButton buttonImpact = new JToggleButton(new ImageIcon(ImageIO.read(getClass().getResource("impact.png"))));
      buttonImpact.setSize(new Dimension(32, 32));
      buttonImpact.addItemListener((e) -> {
        if(e.getStateChange() == ItemEvent.SELECTED){
          mainPanel.showImpact(true);
        }
        else if(e.getStateChange() == ItemEvent.DESELECTED){
          mainPanel.showImpact(false);
        }
      });
      buttonImpact.setToolTipText("Enable / disable cells' impact show");

      JButton buttonClear = new JButton(new ImageIcon(ImageIO.read(getClass().getResource("clear.png"))));
      buttonClear.setSize(new Dimension(32, 32));
      buttonClear.addActionListener((e) -> {
        if(timer == null){
          mainPanel.clear();
        }
      });
      buttonClear.setToolTipText("Clear field");

      JButton buttonSettings = new JButton(new ImageIcon(ImageIO.read(getClass().getResource("settings.png"))));
      buttonSettings.setSize(new Dimension(32, 32));
      buttonSettings.addActionListener((e) -> {
        JDialog dialog = new JDialog(this, "Settings", true);
        JPanel fieldPane = new JPanel();
        JPanel cellPane = new JPanel();
        JPanel modePane = new JPanel();
        JPanel buttonPane = new JPanel();
        JPanel mPane = new JPanel();
        JPanel bPane = new JPanel();
        JPanel gamePane = new JPanel();

        fieldPane.setLayout(new GridLayout(2, 3));
        cellPane.setLayout(new GridLayout(2, 3));
        mPane.setLayout(new GridLayout(2, 1));
        modePane.setLayout(new GridLayout(1, 2));
        gamePane.setLayout(new GridLayout(5, 3));
        bPane.setLayout(new BorderLayout());

        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.CENTER;
        gridbag.setConstraints(buttonPane, constraints);
        buttonPane.setLayout(gridbag);

        int[] settings = mainPanel.getSettings();
        int[] newSettings = new int[settings.length];

        TextField height = new TextField(String.valueOf(settings[0]), 5);
        TextField width = new TextField(String.valueOf(settings[1]) , 5);
        TextField radius = new TextField(String.valueOf(settings[2]) , 5);
        TextField thickness = new TextField(String.valueOf(settings[3]) , 5);
        TextField speed = new TextField(String.valueOf(settings[4]), 5);

        double[] p = mainPanel.getProps();
        TextField fi = new TextField(String.valueOf(p[0]), 5);
        TextField si = new TextField(String.valueOf(p[1]), 5);
        TextField lb = new TextField(String.valueOf(p[2]), 5);
        TextField le = new TextField(String.valueOf(p[3]), 5);

        JButton submit = new JButton("Submit");
        JButton cancel = new JButton("Cancel");
        JRadioButton replace = new JRadioButton("Replace", settings[5] == 0 ? true : false);
        JRadioButton xor = new JRadioButton("XOR", settings[5] == 0 ? false : true);
        ButtonGroup group = new ButtonGroup();
        replace.addActionListener((m) -> {
          buttonMode.setSelected(false);
        });
        xor.addActionListener((m) -> {
          buttonMode.setSelected(true);
        });
        group.add(replace);
        group.add(xor);
        mPane.add(replace);
        mPane.add(xor);

        submit.addActionListener((d) -> {
          dialog.dispose();
        });

        cancel.addActionListener((d) -> {
          dialog.dispose();
        });

        JSlider slider1 = new JSlider(JSlider.HORIZONTAL);
        slider1.setMinimum(1);
        slider1.setValue(settings[0]);
        slider1.addChangeListener((s) -> {
          height.setText(String.valueOf(slider1.getValue()));
          synchronized(o){
            mainPanel.changeHeight(slider1.getValue());
            mainPanel.draw();
            mainPanel.scrollpane.updateUI();
          }
        });

        JSlider slider2 = new JSlider(JSlider.HORIZONTAL);
        slider2.setMinimum(1);
        slider2.setValue(settings[1]);
        slider2.addChangeListener((s) -> {
          width.setText(String.valueOf(slider2.getValue()));
          synchronized(o){
            mainPanel.changeWidth(slider2.getValue());
            mainPanel.draw();
            mainPanel.scrollpane.updateUI();
          }
        });

        JSlider slider3 = new JSlider(JSlider.HORIZONTAL);
        slider3.setMinimum(10);
        slider3.setValue(settings[2]);
        slider3.setMaximum(50);
        JSlider slider4 = new JSlider(JSlider.HORIZONTAL, 1, settings[2] / 2, settings[3]);
        slider3.addChangeListener((s) -> {
          radius.setText(String.valueOf(slider3.getValue()));
          slider4.setMaximum(slider3.getValue() / 2);
          if(slider4.getMaximum() < slider4.getValue()) slider4.setValue(slider4.getMaximum());
          synchronized(o){
            mainPanel.setRadius(slider3.getValue());
            mainPanel.draw();
            mainPanel.scrollpane.updateUI();
          }
        });

        slider4.addChangeListener((s) -> {
          thickness.setText(String.valueOf(slider4.getValue()));
          synchronized(o){
            mainPanel.setSideThickness(slider4.getValue());
            mainPanel.draw();
          }
        });

        JSlider slider5 = new JSlider(JSlider.HORIZONTAL, 100, 1000, settings[4]);
        slider5.addChangeListener((s) -> {
          speed.setText(String.valueOf(slider5.getValue()));
          mainPanel.setSpeed(slider5.getValue());
          if(timer != null){
            timer.cancel();
            timer = new Timer(true);
            timer.schedule(new TimerTask(){
              @Override
              public void run(){
                synchronized(o){
                  mainPanel.stepChange();
                }
                if(mainPanel.stopChanging()){
                  buttonRun.setSelected(false);
                }
              };
            }, slider5.getValue(), slider5.getValue());
          }
        });

        height.addFocusListener(new FocusListener() {
          public void focusGained(FocusEvent e) {}

          public void focusLost(FocusEvent e) {
            slider1.setValue(Integer.valueOf(height.getText()));
          }
        });

        width.addFocusListener(new FocusListener() {
          public void focusGained(FocusEvent e) {}

          public void focusLost(FocusEvent e) {
            slider2.setValue(Integer.valueOf(width.getText()));
          }
        });

        radius.addFocusListener(new FocusListener() {
          public void focusGained(FocusEvent e) {}

          public void focusLost(FocusEvent e) {
            slider3.setValue(Integer.valueOf(radius.getText()));
          }
        });

        thickness.addFocusListener(new FocusListener() {
          public void focusGained(FocusEvent e) {}

          public void focusLost(FocusEvent e) {
            slider4.setValue(Integer.valueOf(thickness.getText()));
          }
        });

        speed.addFocusListener(new FocusListener() {
          public void focusGained(FocusEvent e) {}

          public void focusLost(FocusEvent e) {
            slider5.setValue(Integer.valueOf(speed.getText()));
          }
        });

        fi.addFocusListener(new FocusListener() {
          public void focusGained(FocusEvent e) {}

          public void focusLost(FocusEvent e) {
            synchronized(o){
              mainPanel.setFI(Double.valueOf(fi.getText()));
              mainPanel.changeImpacts();
            }
          }
        });

        si.addFocusListener(new FocusListener() {
          public void focusGained(FocusEvent e) {}

          public void focusLost(FocusEvent e) {
            synchronized(o){
              mainPanel.setSI(Double.valueOf(si.getText()));
              mainPanel.changeImpacts();
            }
          }
        });

        lb.addFocusListener(new FocusListener() {
          public void focusGained(FocusEvent e) {}

          public void focusLost(FocusEvent e) {
            double lbt = Double.valueOf(lb.getText());
            double let = Double.valueOf(le.getText());
            if(lbt > let) lbt = let;
            mainPanel.setLB(lbt);
          }
        });

        le.addFocusListener(new FocusListener() {
          public void focusGained(FocusEvent e) {}

          public void focusLost(FocusEvent e) {
            double let = Double.valueOf(le.getText());
            double lbt = Double.valueOf(lb.getText());
            if(let < lbt) let = lbt;
            mainPanel.setLE(let);
          }
        });

        bPane.add(submit, BorderLayout.PAGE_START);
        bPane.add(new JPanel(), BorderLayout.CENTER);
        bPane.add(cancel, BorderLayout.PAGE_END);

        buttonPane.add(bPane);
        fieldPane.add(new JLabel("Height"));
        fieldPane.add(slider1);
        fieldPane.add(height);

        fieldPane.add(new JLabel("Width"));
        fieldPane.add(slider2);
        fieldPane.add(width);

        cellPane.add(new JLabel("Radius"));
        cellPane.add(slider3);
        cellPane.add(radius);

        cellPane.add(new JLabel("Thickness"));
        cellPane.add(slider4);
        cellPane.add(thickness);

        gamePane.add(new JLabel("Run speed"));
        gamePane.add(slider5);
        gamePane.add(speed);

        gamePane.add(new JLabel("FST_IMPACT"));
        gamePane.add(new JLabel(""));
        gamePane.add(fi);

        gamePane.add(new JLabel("SND_IMPACT"));
        gamePane.add(new JLabel(""));
        gamePane.add(si);

        gamePane.add(new JLabel("LIVE_BEGIN"));
        gamePane.add(new JLabel(""));
        gamePane.add(lb);

        gamePane.add(new JLabel("LIVE_END"));
        gamePane.add(new JLabel(""));
        gamePane.add(le);

        fieldPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Field"), BorderFactory.createEmptyBorder(5,5,5,5)));
        cellPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Hexahedron"), BorderFactory.createEmptyBorder(5,5,5,5)));
        mPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Mode"), BorderFactory.createEmptyBorder(5,5,5,5)));
        gamePane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Game"), BorderFactory.createEmptyBorder(5,5,5,5)));
        buttonPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Apply changes"), BorderFactory.createEmptyBorder(5,5,5,5)));
        JPanel leftPane = new JPanel(new GridLayout(4, 1));
        modePane.add(mPane);
        modePane.add(buttonPane);
        leftPane.add(fieldPane);
        leftPane.add(cellPane);
        leftPane.add(gamePane);
        leftPane.add(modePane);
        leftPane.setPreferredSize(new Dimension(350, 450));
        dialog.add(leftPane, BorderLayout.LINE_START);
        dialog.setResizable(false);
        dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
      });
      buttonSettings.setToolTipText("Change game settings");

      JButton buttonAbout = new JButton(new ImageIcon(ImageIO.read(getClass().getResource("about.png"))));
      buttonAbout.setSize(new Dimension(32, 32));
      buttonAbout.addActionListener((e) -> JOptionPane.showMessageDialog(this,  "Life - The Game.\nVersion 0.8\nBy Egor Pyataev - Group 15206"));
      buttonAbout.setToolTipText("View information about game");

      JButton buttonExit = new JButton(new ImageIcon(ImageIO.read(getClass().getResource("exit.png"))));
      buttonExit.setSize(new Dimension(32, 32));
      buttonExit.addActionListener((e) -> System.exit(0));
      buttonExit.setToolTipText("Exit");

      toolBar.add(buttonNew);
      toolBar.add(buttonSave);
      toolBar.add(buttonImport);
      toolBar.add(buttonExit);
      toolBar.addSeparator();
      toolBar.add(buttonStep);
      toolBar.add(buttonRun);
      toolBar.add(buttonMode);
      toolBar.add(buttonImpact);
      toolBar.add(buttonClear);
      toolBar.add(buttonSettings);
      toolBar.addSeparator();
      toolBar.add(buttonAbout);
    }
    catch(IOException e){}

    toolBar.setFloatable(false);
    toolBar.setRollover(true);

    add(toolBar, BorderLayout.PAGE_START);
    mainPanel = new MainPanel(new HexahedronGrid(6, 10, 48, 2));
    add(mainPanel);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    mainPanel.draw();
    pack();
    setLocationRelativeTo(null);
    setVisible(true);
  }
}

class MainPanel extends JPanel {
  public JScrollPane scrollpane;
  private ImagePanel imagePanel;

  public void changeImpacts(){
    imagePanel.changeImpacts();
  }

  public double[] getProps(){
    return imagePanel.getProps();
  }

  public void setFI(double fi){
    imagePanel.setFI(fi);
  }

  public void setSI(double si){
    imagePanel.setSI(si);
  }

  public void setLB(double lb){
    imagePanel.setLB(lb);
  }

  public void setLE(double le){
    imagePanel.setLE(le);
  }

  public void setCells(LinkedList<Point> ac){
    imagePanel.setCells(ac);
  }

  public LinkedList<Point> getCells(){
    return imagePanel.getCells();
  }

  public void showImpact(boolean show){
    this.imagePanel.showImpact(show);
  }

  public void changeWidth(int w){
    imagePanel.changeWidth(w);
  }

  public void changeHeight(int h){
    imagePanel.changeHeight(h);
  }

  public int getSpeed(){
    return imagePanel.getSpeed();
  }

  public void setSpeed(int s){
    imagePanel.setSpeed(s);
  }

  public void setRadius(int r){
    imagePanel.setRadius(r);
  }

  public void setSideThickness(int s){
    imagePanel.setSideThickness(s);
  }
  public int[] getSettings(){
    return this.imagePanel.getSettings();
  }
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
  private BufferedImage impactIm;
  private HexahedronGrid field;
  private int initWidth;
  private int initHeight;
  private Color defColor;
  private Point lastCell;
  private boolean xor = false;
  private int speed = 600;
  private boolean showImpacts = false;

  public void changeImpacts(){
    field.recountImpact();
    draw();
  }

  public double[] getProps(){
    return field.getLiveProps();
  }

  public void setFI(double fi){
    field.setFI(fi);
  }

  public void setSI(double si){
    field.setSI(si);
  }

  public void setLB(double lb){
    field.setLB(lb);
  }

  public void setLE(double le){
    field.setLE(le);
  }

  public void setCells(LinkedList<Point> ac){
    field.setAliveCells(ac);
  }

  public LinkedList<Point> getCells(){
    return field.getAliveCells();
  }

  public void showImpact(boolean show){
    this.showImpacts = show;
    Graphics ig = this.impactIm.createGraphics();
    ((Graphics2D)ig).setBackground(new Color(0, 0, 0, 0));
    ig.clearRect(0, 0, impactIm.getWidth(), impactIm.getHeight());
    ig.setColor(Color.black);
    field.recountImpact();
    if(showImpacts){
      Hexahedron[][] f = field.getField();
      for(int i = 0; i < field.getHexHeight(); i++){
        for(int j = 0; j < field.getHexWidth(); j++){
          Point ce = f[i][j].getCenter();
          if(showImpacts) ig.drawString(String.format("%.1f", f[i][j].getImpact()), (int)ce.getX() - 8, (int)ce.getY() + 5);
        }
      }
    }
    repaint();
  }

  public void changeWidth(int w){
    this.field.changeWidth(w);
    // this.draw();
    this.initWidth = field.getWidth();
    this.initHeight = field.getHeight();
    this.image = new BufferedImage(field.getWidth(), field.getHeight(), BufferedImage.TYPE_INT_ARGB);
    this.impactIm = new BufferedImage(field.getWidth(), field.getHeight(), BufferedImage.TYPE_INT_ARGB);
    setPreferredSize(new Dimension(initWidth, initHeight));
  }

  public void changeHeight(int h){
    this.field.changeHeight(h);
    this.initWidth = field.getWidth();
    this.initHeight = field.getHeight();
    this.image = new BufferedImage(field.getWidth(), field.getHeight(), BufferedImage.TYPE_INT_ARGB);
    this.impactIm = new BufferedImage(field.getWidth(), field.getHeight(), BufferedImage.TYPE_INT_ARGB);

    setPreferredSize(new Dimension(initWidth, initHeight));
    // this.draw();
  }

  public int getSpeed(){
    return this.speed;
  }

  public void setSpeed(int s){
    this.speed = s;
  }

  public void setRadius(int r){
    field.setRadius(r);
    this.initWidth = field.getWidth();
    this.initHeight = field.getHeight();
    this.image = new BufferedImage(field.getWidth(), field.getHeight(), BufferedImage.TYPE_INT_ARGB);
    this.impactIm = new BufferedImage(field.getWidth(), field.getHeight(), BufferedImage.TYPE_INT_ARGB);
    this.defColor = this.image.getGraphics().getColor();
    setPreferredSize(new Dimension(initWidth, initHeight));
  }

  public void setSideThickness(int s){
    field.setSideThickness(s);
  }

  public int[] getSettings(){
    int[] s = new int[6];
    s[0] = this.field.getHexHeight();
    s[1] = this.field.getHexWidth();
    s[2] = this.field.getHexRadius();
    s[3] = this.field.getHexSideThick();
    s[4] = this.speed;
    s[5] = this.xor == false ? 0 : 1;

    return s;
  }

  public void changeMode(){
    this.xor = !this.xor;
  }

  public boolean stopChanging(){
    return this.field.isExtinction();
  }

  public void stepChange(){
    field.stepChange();
    field.recountImpact();
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

  private int sign (int x) {
  	return (x > 0) ? 1 : (x < 0) ? -1 : 0;
  }

  public void drawBrezenhemLine(BufferedImage img, int x1, int y1, int x2, int y2){
    int x, y, dx, dy, incx, incy, pdx, pdy, es, el, err;

  	dx = x2 - x1;
  	dy = y2 - y1;

  	incx = sign(dx);
  	incy = sign(dy);

  	if(dx < 0) dx = -dx;
  	if(dy < 0) dy = -dy;

  	if (dx > dy){
  		pdx = incx;	pdy = 0;
  		es = dy;	el = dx;
  	}
  	else{
  		pdx = 0;	pdy = incy;
  		es = dx;	el = dy;
  	}

  	x = x1;
  	y = y1;
  	err = el/2;
  	img.setRGB(x, y, Color.BLACK.getRGB());

  	for(int t = 0; t < el; t++){
  		err -= es;
  		if (err < 0){
  			err += el;
  			x += incx;
  			y += incy;
  		}
  		else{
  			x += pdx;
  			y += pdy;
  		}

      img.setRGB(x, y, Color.BLACK.getRGB());
  	}
  }

  private void drawHexahedron(BufferedImage img, Hexahedron h){
    int xc[] = h.getXCoords();
    int yc[] = h.getYCoords();
    int x1, x2, y1, y2;
    int j = 5;
    int thick = field.getHexSideThick();

    if(thick > 1){
      Graphics2D g2 = (Graphics2D)img.createGraphics();
      g2.setStroke(new BasicStroke(thick));
      g2.setColor(Color.BLACK);
      g2.drawPolygon(xc, yc, 6);
    }
    else{
      while(j >= 0){
        if(j > 0){
          x1 = xc[j]; x2 = xc[j-1];
          y1 = yc[j]; y2 = yc[j-1];
          drawBrezenhemLine(img, x1, y1, x2, y2);
          j--;
        }
        else{
          x1 = xc[j]; x2 = xc[5];
          y1 = yc[j]; y2 = yc[5];
          drawBrezenhemLine(img, x1, y1, x2, y2);
          j--;
        }
      }
    }
  }

  public void drawHexahedronGrid(){
    Graphics g = this.image.createGraphics();
    Graphics ig = this.impactIm.createGraphics();
    paintComponent(g);

    if(showImpacts){
      ((Graphics2D)ig).setBackground(new Color(0, 0, 0, 0));
      ig.clearRect(0, 0, impactIm.getWidth(), impactIm.getHeight());
      ig.setColor(Color.black);
    }

    g.setColor(Color.black);
    Hexahedron[][] f = this.field.getField();
    for(int i = 0; i < this.field.getHexHeight(); i++){
      for(int j = 0; j < this.field.getHexWidth(); j++){
        drawHexahedron(this.image, f[i][j]);
        Point p = f[i][j].getCenter();
        spanFilling(this.image, (int)p.getX(), (int)p.getY(), f[i][j].isAlive() ? Color.RED : this.defColor);
        if(showImpacts){
          ig.drawString(String.format("%.1f", f[i][j].getImpact()), (int)p.getX() - 8, (int)p.getY() + 5);
        }
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
    return new Point(xc / 2, yc / 2);
  }

  public ImagePanel(HexahedronGrid field){
    this.field = field;
    this.initWidth = field.getWidth();
    this.initHeight = field.getHeight();
    this.image = new BufferedImage(field.getWidth(), field.getHeight(), BufferedImage.TYPE_INT_ARGB);
    this.impactIm = new BufferedImage(field.getWidth(), field.getHeight(), BufferedImage.TYPE_INT_ARGB);
    this.defColor = this.image.getGraphics().getColor();
    setPreferredSize(new Dimension(initWidth, initHeight));

    addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        int R = field.getHexRadius();
        int yl = (int)(R * Math.sqrt(3) / 2);
        int yl2 = yl + yl;
        int x = e.getX();
        int y = e.getY();
        if(image.getRGB(x, y) == 0 || image.getRGB(x, y) == Color.BLACK.getRGB()){
          return;
        }
        Point p = getCenterCoords(x, y);
        int cellX = ((int)p.getX() - 20) / (R + R / 2);
        int cellY = ((int)p.getY() - 20) / (yl2);
        lastCell = new Point(cellY, cellX);
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

          field.recountImpact();
          Graphics ig = impactIm.createGraphics();

          if(showImpacts){
            ((Graphics2D)ig).setBackground(new Color(0, 0, 0, 0));
            ig.clearRect(0, 0, impactIm.getWidth(), impactIm.getHeight());
            ig.setColor(Color.black);
          }

          Hexahedron[][] f = field.getField();
          for(int i = 0; i < field.getHexHeight(); i++){
            for(int j = 0; j < field.getHexWidth(); j++){
              Point ce = f[i][j].getCenter();
              if(showImpacts) ig.drawString(String.format("%.1f", f[i][j].getImpact()), (int)ce.getX() - 8, (int)ce.getY() + 5);
            }
          }
          repaint();
        }
        catch(ArrayIndexOutOfBoundsException ex){}
      }
    });
    addMouseMotionListener(new MouseMotionAdapter() {
    @Override
    public void mouseDragged(MouseEvent e) {
      int R = field.getHexRadius();
      int yl = (int)(R * Math.sqrt(3) / 2);
      int yl2 = yl + yl;
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

        field.recountImpact();
        Graphics ig = impactIm.createGraphics();
        if(showImpacts){
          ((Graphics2D)ig).setBackground(new Color(0, 0, 0, 0));
          ig.clearRect(0, 0, impactIm.getWidth(), impactIm.getHeight());
          ig.setColor(Color.black);
        }
        Hexahedron[][] f = field.getField();
        for(int i = 0; i < field.getHexHeight(); i++){
          for(int j = 0; j < field.getHexWidth(); j++){
            Point ce = f[i][j].getCenter();
            if(showImpacts) ig.drawString(String.format("%.1f", f[i][j].getImpact()), (int)ce.getX() - 8, (int)ce.getY() + 5);
          }
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
      if(showImpacts) g.drawImage(impactIm, 0, 0, null);
    }
  }
}
