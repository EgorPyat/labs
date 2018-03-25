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
import java.util.Arrays;

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
    JMenuItem mFilterNF = new JMenuItem("Negative filter");
    JMenuItem mFilterDF = new JMenuItem("Dithering filter");
    JMenuItem mFilterDouble = new JMenuItem("Double scale filter");
    JMenuItem mFilterBF = new JMenuItem("Blur filter");
    JMenuItem mFilterSF = new JMenuItem("Sharpness filter");
    JMenuItem mFilterEF = new JMenuItem("Embossing filter");
    JMenuItem mFilterWC = new JMenuItem("Water color filter");
    JMenuItem mFilterRF = new JMenuItem("Rotate filter");
    JMenuItem mFilterGCF = new JMenuItem("Gamma correction filter");

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
    mFilter.add(mFilterNF);
    mFilter.add(mFilterDF);
    mFilter.add(mFilterDouble);
    mFilter.add(mFilterBF);
    mFilter.add(mFilterSF);
    mFilter.add(mFilterEF);
    mFilter.add(mFilterWC);
    mFilter.add(mFilterRF);
    mFilter.add(mFilterGCF);

    mAbout.add(mAboutInfo);

    menu.add(mFile);
    menu.add(mFilter);
    menu.add(mAbout);
    setJMenuBar(menu);

    JToolBar toolBar = new JToolBar();
    try{
      JButton buttonNew = new JButton(new ImageIcon(ImageIO.read(getClass().getResource("./resourses/new.png"))));
      ActionListener ln = (e) -> {
        JFileChooser c = new JFileChooser("./FIT_15206_Pyataev_Egor_Data");
        c.setFileFilter(new FileNameExtensionFilter("Image formats", "jpg", "jpeg", "png", "bmp"));
        int rVal = c.showOpenDialog(this);
        if(rVal == JFileChooser.APPROVE_OPTION){
          areasPanel.setImage(c.getSelectedFile().getPath());
        }
      };
      buttonNew.addActionListener(ln);

      JButton buttonSave = new JButton(new ImageIcon(ImageIO.read(getClass().getResource("./resourses/save.png"))));
      ActionListener ls = (e) -> {
        JFileChooser c = new JFileChooser("./FIT_15206_Pyataev_Egor_Data");
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
              String ending = ".png";
              if(c.getSelectedFile().getName().endsWith(".png")) ending = "";
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

      JButton buttonExit = new JButton(new ImageIcon(ImageIO.read(getClass().getResource("./resourses/exit.png"))));
      ActionListener le = (e) -> {
        System.exit(0);
      };
      buttonExit.addActionListener(le);

      JToggleButton buttonSelect = new JToggleButton(new ImageIcon(ImageIO.read(getClass().getResource("./resourses/select.png"))));
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

      JButton buttonReset = new JButton(new ImageIcon(ImageIO.read(getClass().getResource("./resourses/reset.png"))));
      ActionListener lr = (e) -> {
        areasPanel.reset();
      };
      buttonReset.addActionListener(lr);

      JButton buttonTransfer = new JButton(new ImageIcon(ImageIO.read(getClass().getResource("./resourses/transfer.png"))));
      ActionListener lt = (e) -> {
        areasPanel.transfer();
      };
      buttonTransfer.addActionListener(lt);

      JButton buttonAbout = new JButton(new ImageIcon(ImageIO.read(getClass().getResource("./resourses/about.png"))));
      ActionListener la = (e) -> {
        JOptionPane.showMessageDialog(this,  "Filter - The App.\nVersion 0.2.2.8\nBy Egor Pyataev - Group 15206");
      };
      buttonAbout.addActionListener(la);

      JButton blackNwhiteFilter = new JButton(new ImageIcon(ImageIO.read(getClass().getResource("./resourses/bnw.png"))));
      ActionListener bnwf = (e) -> {
        areasPanel.blackNwhiteFilter();
      };
      blackNwhiteFilter.addActionListener(bnwf);

      JButton negativeFilter = new JButton(new ImageIcon(ImageIO.read(getClass().getResource("./resourses/negative.png"))));
      ActionListener nf = (e) -> {
        areasPanel.negativeFilter();
      };
      negativeFilter.addActionListener(nf);

      JButton ditheringFilter = new JButton(new ImageIcon(ImageIO.read(getClass().getResource("./resourses/dither.png"))));
      ActionListener dtf = (e) -> {
        areasPanel.orderDitheringFilter();

        if(!areasPanel.canFilter()){
          return;
        }

        JDialog dialog = new JDialog(this, "Set dithering settings", true);
        JPanel ditherSetter = new JPanel();
        JSlider ditherSlider = new JSlider(JSlider.HORIZONTAL);
        TextField ditherField = new TextField("", 10);
        JButton submit = new JButton("Submit");
        JButton cancel = new JButton("Cancel");

        ButtonGroup group = new ButtonGroup();
        JRadioButton ordButton = new JRadioButton("Ordered", true);
        group.add(ordButton);
        JRadioButton fsButton = new JRadioButton("Floyd-Stanberg", false);
        group.add(fsButton);
        ditherSetter.setLayout(new GridLayout(4, 2));

        ditherSlider.setMinimum(2);
        ditherSlider.setMaximum(16);

        submit.addActionListener((d) -> {
          dialog.dispose();
        });

        cancel.addActionListener((d) -> {
          dialog.dispose();
        });

        int d = areasPanel.getDither();

        ditherField.setText(String.valueOf(d));
        ditherSlider.setValue(d);

        ditherField.addFocusListener(new FocusListener(){
          public void focusGained(FocusEvent ev){}

          public void focusLost(FocusEvent ev){
            ditherSlider.setValue(Integer.valueOf(ditherField.getText()));
          }
        });
        fsButton.addItemListener((ec) -> {
          if(fsButton.isSelected()) areasPanel.FloidStendbergDitheringFilter();
          else areasPanel.orderDitheringFilter();
        });

        ditherSlider.addChangeListener((ev) -> {
          int t = ditherSlider.getValue();
          areasPanel.setDither(t);
          ditherField.setText(String.valueOf(t));
          if(ordButton.isSelected()) areasPanel.orderDitheringFilter();
          else areasPanel.FloidStendbergDitheringFilter();
        });

        ditherSetter.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Dithering"), BorderFactory.createEmptyBorder(5,5,5,5)));

        ditherSetter.add(ditherSlider);
        ditherSetter.add(ditherField);
        ditherSetter.add(ordButton);
        ditherSetter.add(fsButton);
        ditherSetter.add(new JPanel());
        ditherSetter.add(new JPanel());
        ditherSetter.add(submit);
        ditherSetter.add(cancel);

        dialog.add(ditherSetter);

        dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        dialog.setPreferredSize(new Dimension(340, 140));
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
      };
      ditheringFilter.addActionListener(dtf);

      JButton doubleScaleFilter = new JButton(new ImageIcon(ImageIO.read(getClass().getResource("./resourses/double.png"))));
      ActionListener df = (e) -> {
        areasPanel.doubleScaleFilter();
      };
      doubleScaleFilter.addActionListener(df);

      JButton blurFilter = new JButton(new ImageIcon(ImageIO.read(getClass().getResource("./resourses/blur.png"))));
      ActionListener bf = (e) -> {
        areasPanel.blurFilter();
      };
      blurFilter.addActionListener(bf);

      JButton sharpnessFilter = new JButton(new ImageIcon(ImageIO.read(getClass().getResource("./resourses/sharp.png"))));
      ActionListener sf = (e) -> {
        areasPanel.sharpnessFilter();
      };
      sharpnessFilter.addActionListener(sf);

      JButton embossingFilter = new JButton(new ImageIcon(ImageIO.read(getClass().getResource("./resourses/stamp.png"))));
      ActionListener ef = (e) -> {
        areasPanel.embossingFilter();
      };
      embossingFilter.addActionListener(ef);

      JButton watercolorFilter = new JButton(new ImageIcon(ImageIO.read(getClass().getResource("./resourses/watercolor.png"))));
      ActionListener wcf = (e) -> {
        areasPanel.watercolorFilter();
      };
      watercolorFilter.addActionListener(wcf);

      JButton rotateFilter = new JButton(new ImageIcon(ImageIO.read(getClass().getResource("./resourses/rotate.png"))));
      ActionListener rf = (e) -> {
        areasPanel.rotateFilter();

        if(!areasPanel.canFilter()){
          return;
        }

        JDialog dialog = new JDialog(this, "Set angle", true);
        JPanel angleSetter = new JPanel();
        JSlider angleSlider = new JSlider(JSlider.HORIZONTAL);
        TextField angleField = new TextField("", 10);
        JButton submit = new JButton("Submit");
        JButton cancel = new JButton("Cancel");
        int a = areasPanel.getAngle();

        dialog.setLayout(new BorderLayout());
        angleSetter.setLayout(new GridLayout(3, 2));

        angleSlider.setMinimum(-180);
        angleSlider.setMaximum(180);

        submit.addActionListener((d) -> {
          dialog.dispose();
        });

        cancel.addActionListener((d) -> {
          areasPanel.setAngle(a);
          areasPanel.rotateFilter();
          dialog.dispose();
        });

        angleField.setText(String.valueOf(a));
        angleSlider.setValue(a);

        angleField.addFocusListener(new FocusListener(){
          public void focusGained(FocusEvent ev){}

          public void focusLost(FocusEvent ev){
            angleSlider.setValue(Integer.valueOf(angleField.getText()));
          }
        });

        angleSlider.addChangeListener((ev) -> {
          int t = angleSlider.getValue();
          angleField.setText(String.valueOf(t));
          areasPanel.setAngle(t);
          areasPanel.rotateFilter();
        });

        angleSetter.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Angle"), BorderFactory.createEmptyBorder(5,5,5,5)));

        angleSetter.add(angleSlider);
        angleSetter.add(angleField);
        angleSetter.add(new JPanel());
        angleSetter.add(new JPanel());
        angleSetter.add(submit);
        angleSetter.add(cancel);

        dialog.add(angleSetter);

        dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        dialog.setPreferredSize(new Dimension(300, 130));
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
      };
      rotateFilter.addActionListener(rf);

      JButton gammaCorrectionFilter = new JButton(new ImageIcon(ImageIO.read(getClass().getResource("./resourses/gamma.png"))));
      ActionListener gcf = (e) -> {
        areasPanel.gammaCorrectionFilter();

        if(!areasPanel.canFilter()){
          return;
        }

        JDialog dialog = new JDialog(this, "Set gamma", true);
        JPanel gammaSetter = new JPanel();
        JSlider gammaSlider = new JSlider(JSlider.HORIZONTAL);
        TextField gammaField = new TextField("", 10);
        JButton submit = new JButton("Submit");
        JButton cancel = new JButton("Cancel");
        double g = areasPanel.getGamma();

        dialog.setLayout(new BorderLayout());
        gammaSetter.setLayout(new GridLayout(3, 2));

        gammaSlider.setMinimum(0);
        gammaSlider.setMaximum(30);

        submit.addActionListener((d) -> {
          dialog.dispose();
        });

        cancel.addActionListener((d) -> {
          areasPanel.setGamma(g);
          areasPanel.gammaCorrectionFilter();
          dialog.dispose();
        });

        gammaField.setText(String.valueOf(g));
        gammaSlider.setValue((int)(g * 10));

        gammaField.addFocusListener(new FocusListener(){
          public void focusGained(FocusEvent ev){}

          public void focusLost(FocusEvent ev){
            gammaSlider.setValue((int)(Double.valueOf(gammaField.getText()) * 10));
          }
        });

        gammaSlider.addChangeListener((ev) -> {
          double t = gammaSlider.getValue() / 10.0;
          gammaField.setText(String.valueOf(t));
          areasPanel.setGamma(t);
          areasPanel.gammaCorrectionFilter();
        });

        gammaSetter.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Gamma"), BorderFactory.createEmptyBorder(5,5,5,5)));

        gammaSetter.add(gammaSlider);
        gammaSetter.add(gammaField);
        gammaSetter.add(new JPanel());
        gammaSetter.add(new JPanel());
        gammaSetter.add(submit);
        gammaSetter.add(cancel);

        dialog.add(gammaSetter);

        dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        dialog.setPreferredSize(new Dimension(300, 130));
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
      };
      gammaCorrectionFilter.addActionListener(gcf);

      toolBar.add(buttonNew);
      toolBar.add(buttonSave);
      toolBar.add(buttonExit);
      toolBar.addSeparator();
      toolBar.add(buttonSelect);
      toolBar.add(buttonReset);
      toolBar.add(buttonTransfer);
      toolBar.addSeparator();
      toolBar.add(blackNwhiteFilter);
      toolBar.add(negativeFilter);
      toolBar.add(ditheringFilter);
      toolBar.add(doubleScaleFilter);
      toolBar.add(blurFilter);
      toolBar.add(sharpnessFilter);
      toolBar.add(embossingFilter);
      toolBar.add(watercolorFilter);
      toolBar.add(rotateFilter);
      toolBar.add(gammaCorrectionFilter);
      toolBar.addSeparator();
      toolBar.add(buttonAbout);

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
      mFilterNF.addActionListener(nf);
      mFilterDF.addActionListener(dtf);
      mFilterDouble.addActionListener(df);
      mFilterBF.addActionListener(bf);
      mFilterSF.addActionListener(sf);
      mFilterEF.addActionListener(ef);
      mFilterWC.addActionListener(wcf);
      mFilterRF.addActionListener(rf);
      mFilterGCF.addActionListener(gcf);

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
  private int angle = 0;
  private double gamma = 1.0;
  private int dither = 4;

  public void blackNwhiteFilter(){
    if(subimage == null){
      JOptionPane.showMessageDialog(this, "Nothing to filter.", "Filter warning", JOptionPane.WARNING_MESSAGE);
      return;
    }

    double alpha, red, green, blue;
    double newPixel;

    filteredImage = new BufferedImage(subimage.getWidth(), subimage.getHeight(), subimage.getType());

    for(int i = 0; i < subimage.getWidth(); i++) {
      for(int j = 0; j < subimage.getHeight(); j++) {
        alpha = new Color(subimage.getRGB(i, j)).getAlpha();
        red = new Color(subimage.getRGB(i, j)).getRed() * 0.299;
        green = new Color(subimage.getRGB(i, j)).getGreen() * 0.587;
        blue = new Color(subimage.getRGB(i, j)).getBlue() * 0.114;
        newPixel = red + green + blue;
        newPixel = newPixel > 255 ? 255 : newPixel;
        newPixel = (new Color((int)newPixel, (int)newPixel, (int)newPixel, (int)alpha)).getRGB();
        filteredImage.setRGB(i, j, (int)newPixel);
      }
    }
    repaint();
  }

  public void negativeFilter(){
    if(subimage == null){
      JOptionPane.showMessageDialog(this, "Nothing to filter.", "Filter warning", JOptionPane.WARNING_MESSAGE);
      return;
    }

    int pixel, alpha, red, green, blue;

    filteredImage = new BufferedImage(subimage.getWidth(), subimage.getHeight(), subimage.getType());

    for(int i = 0; i < subimage.getWidth(); i++) {
      for(int j = 0; j < subimage.getHeight(); j++) {
        pixel = subimage.getRGB(i, j);
        alpha = (pixel >> 24) & 0xff;
        red = (pixel >> 16) & 0xff;
        green = (pixel >> 8) & 0xff;
        blue = pixel & 0xff;
        red = 255 - red;
        green = 255 - green;
        blue = 255 - blue;
        pixel = (alpha << 24) | (red << 16) | (green << 8) | blue;
        filteredImage.setRGB(i, j, pixel);
      }
    }
    repaint();
  }

  public void FloidStendbergDitheringFilter(){
    if(subimage == null){
      JOptionPane.showMessageDialog(this, "Nothing to filter.", "Filter warning", JOptionPane.WARNING_MESSAGE);
      return;
    }

    BufferedImage tempImage = new BufferedImage(subimage.getWidth(), subimage.getHeight(), subimage.getType());
    filteredImage = new BufferedImage(subimage.getWidth(), subimage.getHeight(), subimage.getType());

    int pixel, alpha, red, green, blue;
    int nred, ngreen, nblue;
    int ered, egreen, eblue;
    int d = dither;

    for(int i = 0; i < subimage.getWidth(); i++) {
      for(int j = 0; j < subimage.getHeight(); j++) {
        tempImage.setRGB(i, j, subimage.getRGB(i, j));
      }
    }

    for(int i = 1; i < subimage.getWidth() - 1; i++) {
      for(int j = 1; j < subimage.getHeight() - 1; j++) {
        alpha = new Color(tempImage.getRGB(i, j)).getAlpha();
        red = new Color(tempImage.getRGB(i, j)).getRed();
        green = new Color(tempImage.getRGB(i, j)).getGreen();
        blue = new Color(tempImage.getRGB(i, j)).getBlue();

        nred = 255 * (d * red / 256) / d;
        ngreen = 255 * (d * green / 256) / d;
        nblue = 255 * (d * blue / 256) / d;

        ered = red - nred;
        egreen = green - ngreen;
        eblue = blue - nblue;

        filteredImage.setRGB(i, j, new Color(nred, ngreen, nblue, alpha).getRGB());

        alpha = new Color(tempImage.getRGB(i + 1, j)).getAlpha();
        red = new Color(tempImage.getRGB(i + 1, j)).getRed() + ered * 7 / 16;
        red = red > 255 ? 255 : red;
        red = red < 0 ? 0 : red;
        green = new Color(tempImage.getRGB(i + 1, j)).getGreen() + egreen * 7 / 16;
        green = green > 255 ? 255 : green;
        green = green < 0 ? 0 : green;
        blue = new Color(tempImage.getRGB(i + 1, j)).getBlue() + eblue * 7 / 16;
        blue = blue > 255 ? 255 : blue;
        blue = blue < 0 ? 0 : blue;

        tempImage.setRGB(i + 1, j, new Color(red, green, blue, alpha).getRGB());

        alpha = new Color(tempImage.getRGB(i - 1, j + 1)).getAlpha();
        red = new Color(tempImage.getRGB(i - 1, j + 1)).getRed() + ered * 3 / 16;
        red = red > 255 ? 255 : red;
        red = red < 0 ? 0 : red;
        green = new Color(tempImage.getRGB(i - 1, j + 1)).getGreen() + egreen * 3 / 16;
        green = green > 255 ? 255 : green;
        green = green < 0 ? 0 : green;
        blue = new Color(tempImage.getRGB(i - 1, j + 1)).getBlue() + eblue * 3 / 16;
        blue = blue > 255 ? 255 : blue;
        blue = blue < 0 ? 0 : blue;
        tempImage.setRGB(i - 1, j + 1, new Color(red, green, blue, alpha).getRGB());

        alpha = new Color(tempImage.getRGB(i, j + 1)).getAlpha();
        red = new Color(tempImage.getRGB(i, j + 1)).getRed() + ered * 5 / 16;
        red = red > 255 ? 255 : red;
        red = red < 0 ? 0 : red;
        green = new Color(tempImage.getRGB(i, j + 1)).getGreen() + egreen * 5 / 16;
        green = green > 255 ? 255 : green;
        green = green < 0 ? 0 : green;
        blue = new Color(tempImage.getRGB(i, j + 1)).getBlue() + eblue * 5 / 16;
        blue = blue > 255 ? 255 : blue;
        blue = blue < 0 ? 0 : blue;
        tempImage.setRGB(i, j + 1, new Color(red, green, blue, alpha).getRGB());

        alpha = new Color(tempImage.getRGB(i + 1, j + 1)).getAlpha();
        red = new Color(tempImage.getRGB(i + 1, j + 1)).getRed() + ered * 1 / 16;
        red = red > 255 ? 255 : red;
        red = red < 0 ? 0 : red;
        green = new Color(tempImage.getRGB(i + 1, j + 1)).getGreen() + egreen * 1 / 16;
        green = green > 255 ? 255 : green;
        green = green < 0 ? 0 : green;
        blue = new Color(tempImage.getRGB(i + 1, j + 1)).getBlue() + eblue * 1 / 16;
        blue = blue > 255 ? 255 : blue;
        blue = blue < 0 ? 0 : blue;
        tempImage.setRGB(i + 1, j + 1, new Color(red, green, blue, alpha).getRGB());
      }
    }
    repaint();
  }

  public void orderDitheringFilter(){
    if(subimage == null){
      JOptionPane.showMessageDialog(this, "Nothing to filter.", "Filter warning", JOptionPane.WARNING_MESSAGE);
      return;
    }

    int N = 4;
    int[][] matrix = {
  	 	 {0,  8,  2, 10},
  		{12,  4, 14,  6},
  		 {3, 11,  1,  9},
  		{15,  7, 13,  5}
	  };

    int pixel, alpha, red, green, blue, x, y;
    int l = 17;

    filteredImage = new BufferedImage(subimage.getWidth(), subimage.getHeight(), subimage.getType());
    int d = dither;

    for(int i = 0; i < subimage.getWidth(); i++) {
      for(int j = 0; j < subimage.getHeight(); j++) {
        alpha = new Color(subimage.getRGB(i, j)).getAlpha();
        red = new Color(subimage.getRGB(i, j)).getRed();
        green = new Color(subimage.getRGB(i, j)).getGreen();
        blue = new Color(subimage.getRGB(i, j)).getBlue();
        x = i % N;
        y = j % N;
        red = 255 * (red * 17 / 256 > matrix[x][y] ? d * red / 256 + 1 : d * red / 256) / d;
        green = 255 * (green * 17 / 256 > matrix[x][y] ? d * green / 256 + 1 : d * green / 256) / d;
        blue = 255 * (blue * 17 / 256 > matrix[x][y] ? d * blue / 256 + 1 : d * blue / 256) / d;

        filteredImage.setRGB(i, j, new Color(red, green, blue, alpha).getRGB());
      }
    }
    repaint();
  }

  public void doubleScaleFilter(){
    if(subimage == null){
      JOptionPane.showMessageDialog(this, "Nothing to filter.", "Filter warning", JOptionPane.WARNING_MESSAGE);
      return;
    }

    int pixel;

    filteredImage = new BufferedImage(subimage.getWidth(), subimage.getHeight(), subimage.getType());

    for(int i = 0; i < subimage.getWidth(); i += 2){
      for(int j = 0; j < subimage.getHeight(); j += 2){
        pixel = subimage.getRGB(subimage.getWidth() / 4 + i / 2, subimage.getHeight() / 4 + j / 2);
        filteredImage.setRGB(i, j, pixel);
        if(j + 1 < subimage.getHeight()) filteredImage.setRGB(i, j + 1, pixel);
        if(i + 1 < subimage.getWidth()) filteredImage.setRGB(i + 1, j, pixel);
        if(i + 1 < subimage.getWidth() && j + 1 < subimage.getHeight()) filteredImage.setRGB(i + 1, j + 1, pixel);
      }
    }
    repaint();
  }

  public void blurFilter(){
    if(subimage == null){
      JOptionPane.showMessageDialog(this, "Nothing to filter.", "Filter warning", JOptionPane.WARNING_MESSAGE);
      return;
    }

    filteredImage = new BufferedImage(subimage.getWidth(), subimage.getHeight(), subimage.getType());

    int alpha, red, green, blue;

    for(int i = 1; i < filteredImage.getWidth() - 1; i++){
      for(int j = 1; j < filteredImage.getHeight() - 1; j++){
        alpha = (new Color(subimage.getRGB(i - 1, j)).getAlpha() + new Color(subimage.getRGB(i, j - 1)).getAlpha() + new Color(subimage.getRGB(i, j + 1)).getAlpha() + new Color(subimage.getRGB(i + 1, j)).getAlpha() + new Color(subimage.getRGB(i, j)).getAlpha() * 2) / 6;
        alpha = alpha < 0 ? 0 : alpha;
        alpha = alpha > 255 ? 255 : alpha;

        red = (new Color(subimage.getRGB(i - 1, j)).getRed() + new Color(subimage.getRGB(i, j - 1)).getRed() + new Color(subimage.getRGB(i, j + 1)).getRed() + new Color(subimage.getRGB(i + 1, j)).getRed() + new Color(subimage.getRGB(i, j)).getRed() * 2) / 6;
        red = red < 0 ? 0 : red;
        red = red > 255 ? 255 : red;

        green = (new Color(subimage.getRGB(i - 1, j)).getGreen() + new Color(subimage.getRGB(i, j - 1)).getGreen() + new Color(subimage.getRGB(i, j + 1)).getGreen() + new Color(subimage.getRGB(i + 1, j)).getGreen() + new Color(subimage.getRGB(i, j - 1)).getGreen() * 2) / 6;
        green = green < 0 ? 0 : green;
        green = green > 255 ? 255 : green;

        blue = (new Color(subimage.getRGB(i - 1, j)).getBlue() + new Color(subimage.getRGB(i, j - 1)).getBlue() + new Color(subimage.getRGB(i, j + 1)).getBlue() + new Color(subimage.getRGB(i + 1, j)).getBlue() + new Color(subimage.getRGB(i, j - 1)).getBlue() * 2) / 6;
        blue = blue < 0 ? 0 : blue;
        blue = blue > 255 ? 255 : blue;

        filteredImage.setRGB(i, j, new Color(red, green, blue, alpha).getRGB());
      }
    }
    repaint();
  }

  public void sharpnessFilter(){
    if(subimage == null){
      JOptionPane.showMessageDialog(this, "Nothing to filter.", "Filter warning", JOptionPane.WARNING_MESSAGE);
      return;
    }

    filteredImage = new BufferedImage(subimage.getWidth(), subimage.getHeight(), subimage.getType());

    int alpha, red, green, blue;

    for(int i = 1; i < filteredImage.getWidth() - 1; i++){
      for(int j = 1; j < filteredImage.getHeight() - 1; j++){
        alpha = (-new Color(subimage.getRGB(i - 1, j)).getAlpha() - new Color(subimage.getRGB(i, j - 1)).getAlpha() - new Color(subimage.getRGB(i, j + 1)).getAlpha() - new Color(subimage.getRGB(i + 1, j)).getAlpha() + new Color(subimage.getRGB(i, j)).getAlpha() * 5);
        alpha = alpha < 0 ? 0 : alpha;
        alpha = alpha > 255 ? 255 : alpha;

        red = (-new Color(subimage.getRGB(i - 1, j)).getRed() - new Color(subimage.getRGB(i, j - 1)).getRed() - new Color(subimage.getRGB(i, j + 1)).getRed() - new Color(subimage.getRGB(i + 1, j)).getRed() + new Color(subimage.getRGB(i, j)).getRed() * 5);
        red = red < 0 ? 0 : red;
        red = red > 255 ? 255 : red;

        green = (-new Color(subimage.getRGB(i - 1, j)).getGreen() - new Color(subimage.getRGB(i, j - 1)).getGreen() - new Color(subimage.getRGB(i, j + 1)).getGreen() - new Color(subimage.getRGB(i + 1, j)).getGreen() + new Color(subimage.getRGB(i, j - 1)).getGreen() * 5);
        green = green < 0 ? 0 : green;
        green = green > 255 ? 255 : green;

        blue = (-new Color(subimage.getRGB(i - 1, j)).getBlue() - new Color(subimage.getRGB(i, j - 1)).getBlue() - new Color(subimage.getRGB(i, j + 1)).getBlue() - new Color(subimage.getRGB(i + 1, j)).getBlue() + new Color(subimage.getRGB(i, j - 1)).getBlue() * 5);
        blue = blue < 0 ? 0 : blue;
        blue = blue > 255 ? 255 : blue;

        filteredImage.setRGB(i, j, new Color(red, green, blue, alpha).getRGB());
      }
    }
    repaint();
  }

  public void embossingFilter(){
    if(subimage == null){
      JOptionPane.showMessageDialog(this, "Nothing to filter.", "Filter warning", JOptionPane.WARNING_MESSAGE);
      return;
    }

    filteredImage = new BufferedImage(subimage.getWidth(), subimage.getHeight(), subimage.getType());

    int alpha, red, green, blue;

    for(int i = 1; i < filteredImage.getWidth() - 1; i++){
      for(int j = 1; j < filteredImage.getHeight() - 1; j++){
        alpha = new Color(subimage.getRGB(i - 1, j)).getAlpha() - new Color(subimage.getRGB(i, j - 1)).getAlpha() + new Color(subimage.getRGB(i, j + 1)).getAlpha() - new Color(subimage.getRGB(i + 1, j)).getAlpha() + 128;
        alpha = alpha < 0 ? 0 : alpha;
        alpha = alpha > 255 ? 255 : alpha;

        red = new Color(subimage.getRGB(i - 1, j)).getRed() - new Color(subimage.getRGB(i, j - 1)).getRed() + new Color(subimage.getRGB(i, j + 1)).getRed() - new Color(subimage.getRGB(i + 1, j)).getRed() + 128;
        red = red < 0 ? 0 : red;
        red = red > 255 ? 255 : red;

        green = new Color(subimage.getRGB(i - 1, j)).getGreen() - new Color(subimage.getRGB(i, j - 1)).getGreen() + new Color(subimage.getRGB(i, j + 1)).getGreen() - new Color(subimage.getRGB(i + 1, j)).getGreen() + 128;
        green = green < 0 ? 0 : green;
        green = green > 255 ? 255 : green;

        blue = new Color(subimage.getRGB(i - 1, j)).getBlue() - new Color(subimage.getRGB(i, j - 1)).getBlue() + new Color(subimage.getRGB(i, j + 1)).getBlue() - new Color(subimage.getRGB(i + 1, j)).getBlue() + 128;
        blue = blue < 0 ? 0 : blue;
        blue = blue > 255 ? 255 : blue;

        filteredImage.setRGB(i, j, new Color(red, green, blue, alpha).getRGB());
      }
    }
    repaint();
  }

  public void watercolorFilter(){
    if(subimage == null){
      JOptionPane.showMessageDialog(this, "Nothing to filter.", "Filter warning", JOptionPane.WARNING_MESSAGE);
      return;
    }

    int[] window = null;

    filteredImage = new BufferedImage(subimage.getWidth(), subimage.getHeight(), subimage.getType());
    for(int i = 2; i < filteredImage.getWidth() - 2; i++){
      for(int j = 2; j < filteredImage.getHeight() - 2; j++){
        window = subimage.getRGB(i - 2, j - 2, 5, 5, window, 0, 5);
        Arrays.sort(window, 0, window.length);
        filteredImage.setRGB(i, j, window[13]);
      }
    }
    repaint();
  }

  public void rotateFilter(){
    if(subimage == null){
      JOptionPane.showMessageDialog(this, "Nothing to filter.", "Filter warning", JOptionPane.WARNING_MESSAGE);
      return;
    }

    int width = subimage.getWidth();
    int height = subimage.getHeight();
    double oldIradius = (double)(height - 1) / 2;
    double oldJradius = (double)(width - 1) / 2;

    double angleRad = -angle * Math.PI / 180;
    double angleCos = Math.cos( angleRad );
    double angleSin = Math.sin( angleRad );

    double halfWidth  = (double)subimage.getWidth() / 2;
    double halfHeight = (double)subimage.getHeight() / 2;

    double cx1 = halfWidth * angleCos;
    double cy1 = halfWidth * angleSin;

    double cx2 = halfWidth * angleCos - halfHeight * angleSin;
    double cy2 = halfWidth * angleSin + halfHeight * angleCos;

    double cx3 = -halfHeight * angleSin;
    double cy3 =  halfHeight * angleCos;

    double cx4 = 0;
    double cy4 = 0;

    halfWidth  = Math.max(Math.max(cx1, cx2), Math.max(cx3, cx4)) - Math.min(Math.min(cx1, cx2), Math.min(cx3, cx4));
    halfHeight = Math.max(Math.max(cy1, cy2), Math.max(cy3, cy4)) - Math.min(Math.min(cy1, cy2), Math.min(cy3, cy4));

    int newWidth = (int)(halfWidth * 2 );
    int newHeight = (int)(halfHeight * 2);

    filteredImage = new BufferedImage(newWidth, newHeight, subimage.getType());
    double newIradius = (double)(newHeight - 1) / 2;
    double newJradius = (double)(newWidth - 1) / 2;

    double ci, cj;

    int oi, oj;

    ci = -newIradius;
    for(int i = 0; i < newHeight; i++){
      cj = -newJradius;
      for(int j = 0; j < newWidth; j++){
        oi = (int)(angleCos * ci + angleSin * cj + oldIradius);
        oj = (int)(-angleSin * ci + angleCos * cj + oldJradius);
        if(( oi < 0 ) || ( oj < 0 ) || ( oi >= height ) || ( oj >= width )){
          filteredImage.setRGB(i, j, new Color(255, 255, 255).getRGB());
        }
        else{
          int r = new Color(subimage.getRGB(oi, oj)).getRed();
          int g = new Color(subimage.getRGB(oi, oj)).getGreen();
          int b = new Color(subimage.getRGB(oi, oj)).getBlue();
          filteredImage.setRGB(i, j, new Color(r, g, b).getRGB());
        }
        cj++;
      }
      ci++;
    }
    repaint();
  }

  public void gammaCorrectionFilter(){
    if(subimage == null){
      JOptionPane.showMessageDialog(this, "Nothing to filter.", "Filter warning", JOptionPane.WARNING_MESSAGE);
      return;
    }

    double alpha, red, green, blue;
    double newPixel;

    filteredImage = new BufferedImage(subimage.getWidth(), subimage.getHeight(), subimage.getType());

    for(int i = 0; i < subimage.getWidth(); i++){
      for(int j = 0; j < subimage.getHeight(); j++){
        alpha = new Color(subimage.getRGB(i, j)).getAlpha();
        red = 255 * Math.pow(new Color(subimage.getRGB(i, j)).getRed() / 255.0, gamma);
        green = 255 * Math.pow(new Color(subimage.getRGB(i, j)).getGreen() / 255.0, gamma);
        blue = 255 * Math.pow(new Color(subimage.getRGB(i, j)).getBlue() / 255.0, gamma);
        newPixel = (new Color((int)red, (int)green, (int)blue, (int)alpha)).getRGB();
        filteredImage.setRGB(i, j, (int)newPixel);
      }
    }
    repaint();
  }

  public boolean canFilter(){
    return filteredImage != null;
  }

  public BufferedImage getFilteredImage(){
    return filteredImage;
  }

  public int getAngle(){
    return angle;
  }

  public void setAngle(int a){
    angle = a;
  }

  public double getGamma(){
    return gamma;
  }

  public void setGamma(double g){
    gamma = g;
  }

  public int getDither(){
    return dither;
  }

  public void setDither(int d){
    dither = d;
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
    angle = 0;
    gamma = 1.0;
    repaint();
  }

  public void setImage(String name){
    try{
      image = ImageIO.read(new File(name));
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
            System.err.println(ex.getMessage());
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
              shiftX = image.getWidth() - X - sideX;
            }
            try{
              subimage = image.getSubimage(X + shiftX, Y + shiftY, sideX, sideY);
            }
            catch(Exception ex){
              System.err.println(ex.getMessage());
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
      if(subimage != null && subimage.getWidth() > 350) g.drawImage(subimage, 380, 15, 350, 350, null);
      else g.drawImage(subimage, 380, 15, null);
      if(filteredImage != null && filteredImage.getWidth() > 350) g.drawImage(filteredImage, 745, 15, 350, 350, null);
      else g.drawImage(filteredImage, 745, 15, null);

      g.setXORMode(Color.WHITE);
      Graphics2D g2d = (Graphics2D) g.create();
      Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{3}, 0);
      g2d.setStroke(dashed);
      if(visible)g2d.draw(selector);
    }
  }
}
