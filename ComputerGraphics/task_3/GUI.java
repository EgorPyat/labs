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
    super("Isoline - The Graph.");

    IsolinePane panel = new IsolinePane(787, 498);
    JMenuBar menu = new JMenuBar();

    JMenu mFile = new JMenu("File");
    JMenu mAbout = new JMenu("About");
    JMenu mGraph = new JMenu("Graph");

    JMenuItem mFileNew = new JMenuItem("New");
    JMenuItem mFileQuit = new JMenuItem("Quit");

    JMenuItem mAboutInfo = new JMenuItem("Info");

    JRadioButtonMenuItem mGraphInterpolation = new JRadioButtonMenuItem("Interpolate");
    JRadioButtonMenuItem mGraphIsolines = new JRadioButtonMenuItem("Show isolines");
    JRadioButtonMenuItem mGraphDIsolines = new JRadioButtonMenuItem("Enable dinamic isolines");
    JRadioButtonMenuItem mGraphGrid = new JRadioButtonMenuItem("Show grid");
    JRadioButtonMenuItem mGraphEntryPoints = new JRadioButtonMenuItem("Show isolines entry points");

    JMenuItem mGraphConfig = new JMenuItem("Config");

    mFile.add(mFileNew);
    mFile.addSeparator();
    mFile.add(mFileQuit);

    mGraph.add(mGraphInterpolation);
    mGraph.add(mGraphIsolines);
    mGraph.add(mGraphDIsolines);
    mGraph.add(mGraphGrid);
    mGraph.add(mGraphEntryPoints);
    mGraph.addSeparator();
    mGraph.add(mGraphConfig);

    mAbout.add(mAboutInfo);

    menu.add(mFile);
    menu.add(mGraph);
    menu.add(mAbout);

    setJMenuBar(menu);

    JToolBar toolBar = new JToolBar();
    try{
      JButton buttonNew = new JButton(new ImageIcon(ImageIO.read(getClass().getResource("./resourses/new.png"))));
      ActionListener ln = (e) -> {
        JFileChooser c = new JFileChooser("./FIT_15206_Pyataev_Egor_Data");
        c.setFileFilter(new FileNameExtensionFilter("Config formats", "txt"));
        int rVal = c.showOpenDialog(this);
        if(rVal == JFileChooser.APPROVE_OPTION){
          try(BufferedReader br = new BufferedReader(new FileReader(c.getSelectedFile()))){
            boolean colorsFlag = false;
            int count = -1;
            int colors[][] = null;
            short km[] = null;
            while(true){
              String line = br.readLine();
              if(line == null){
                if(count >= 0){
                  throw new Exception("Incorrect file format");
                }
                break;
              }
              if(line.contains("//")) line = line.substring(0, line.indexOf("//")).trim();
              if(line.equals("")) continue;
              String[] ars = line.split(" ");
              if(!colorsFlag){
                if(km == null){
                  if(ars.length != 2){
                    throw new Exception("Incorrect file format");
                  }
                  km = new short[2];
                  km[0] = Short.valueOf(ars[0]);
                  km[1] = Short.valueOf(ars[1]);
                  continue;
                }
                if(ars.length != 1){
                  throw new Exception("Incorrect file format");
                }
                count = Integer.valueOf(ars[0]) + 1;
                colors = new int[count + 1][3];
                colorsFlag = true;
              }
              else if(count >= 0){
                if(ars.length != 3){
                  throw new Exception("Incorrect file format");
                }
                colors[colors.length - (count + 1)][0] = Integer.valueOf(ars[0]);
                colors[colors.length - (count + 1)][1] = Integer.valueOf(ars[1]);
                colors[colors.length - (count + 1)][2] = Integer.valueOf(ars[2]);
                --count;
              }
            }
            panel.setGraphSettings(km, colors);
          }
          catch(Exception ex){
            System.out.println(ex.getMessage());
            JOptionPane.showMessageDialog(null, "Incorrect file format.", "New congig error", JOptionPane.ERROR_MESSAGE);
          }
        }
      };
      buttonNew.addActionListener(ln);

      JButton buttonExit = new JButton(new ImageIcon(ImageIO.read(getClass().getResource("./resourses/exit.png"))));
      ActionListener le = (e) -> {
        System.exit(0);
      };
      buttonExit.addActionListener(le);

      JToggleButton buttonInterpolation = new JToggleButton(new ImageIcon(ImageIO.read(getClass().getResource("./resourses/interpolation.png"))));
      buttonInterpolation.addItemListener((e) -> {
        if(e.getStateChange() == ItemEvent.SELECTED){
            mGraphInterpolation.setSelected(true);
            panel.setInterpolation(true);
        }
        else if(e.getStateChange() == ItemEvent.DESELECTED){
          mGraphInterpolation.setSelected(false);
          panel.setInterpolation(false);
        }
      });

      JToggleButton buttonIsolines = new JToggleButton(new ImageIcon(ImageIO.read(getClass().getResource("./resourses/isoline.png"))));
      buttonIsolines.addItemListener((e) -> {
        if(e.getStateChange() == ItemEvent.SELECTED){
            mGraphIsolines.setSelected(true);
            panel.setShowIsolines(true);
        }
        else if(e.getStateChange() == ItemEvent.DESELECTED){
            mGraphIsolines.setSelected(false);
            panel.setShowIsolines(false);
        }
      });

      JToggleButton buttonMouseIsoline = new JToggleButton(new ImageIcon(ImageIO.read(getClass().getResource("./resourses/select.png"))));
      buttonMouseIsoline.addItemListener((e) -> {
        if(e.getStateChange() == ItemEvent.SELECTED){
            mGraphDIsolines.setSelected(true);
            panel.setDinamic(true);
        }
        else if(e.getStateChange() == ItemEvent.DESELECTED){
            mGraphDIsolines.setSelected(false);
            panel.setDinamic(false);
        }
      });

      JToggleButton buttonGrid = new JToggleButton(new ImageIcon(ImageIO.read(getClass().getResource("./resourses/grid.png"))));
      buttonGrid.addItemListener((e) -> {
        if(e.getStateChange() == ItemEvent.SELECTED){
            mGraphGrid.setSelected(true);
            panel.setShowGrid(true);
        }
        else if(e.getStateChange() == ItemEvent.DESELECTED){
            mGraphGrid.setSelected(false);
            panel.setShowGrid(false);
        }
      });

      JToggleButton buttonEntryPoints = new JToggleButton(new ImageIcon(ImageIO.read(getClass().getResource("./resourses/entry-point.png"))));
      buttonEntryPoints.addItemListener((e) -> {
        if(e.getStateChange() == ItemEvent.SELECTED){
            mGraphEntryPoints.setSelected(true);
            panel.setShowEntries(true);
        }
        else if(e.getStateChange() == ItemEvent.DESELECTED){
            mGraphEntryPoints.setSelected(false);
            panel.setShowEntries(false);
        }
      });

      JButton buttonGraphConfig = new JButton(new ImageIcon(ImageIO.read(getClass().getResource("./resourses/config.png"))));
      ActionListener lc = (ex) -> {
        JDialog dialog = new JDialog(this, "Settings", true);
        JPanel pane = new JPanel();
        pane.setLayout(new GridLayout(6, 2));
        double[] s = panel.getSettings();

        TextField k = new TextField("", 5);
        k.setText(String.valueOf(s[0]));
        TextField m = new TextField("", 5);
        m.setText(String.valueOf(s[1]));
        TextField a = new TextField("", 5);
        a.setText(String.valueOf(s[2]));
        TextField b = new TextField("", 5);
        b.setText(String.valueOf(s[3]));
        TextField c = new TextField("", 5);
        c.setText(String.valueOf(s[4]));
        TextField d = new TextField("", 5);
        d.setText(String.valueOf(s[5]));
        JButton submit = new JButton("Sumbit");
        submit.addActionListener((e) -> {
          dialog.dispose();
          s[0] = Double.valueOf(k.getText());
          s[1] = Double.valueOf(m.getText());
          s[2] = Double.valueOf(a.getText());
          s[3] = Double.valueOf(b.getText());
          s[4] = Double.valueOf(c.getText());
          s[5] = Double.valueOf(d.getText());
          panel.setSettings(s);
        });

        pane.add(new JLabel("k"));
        pane.add(k);
        pane.add(new JLabel("m"));
        pane.add(m);
        pane.add(new JLabel("a"));
        pane.add(a);
        pane.add(new JLabel("b"));
        pane.add(b);
        pane.add(new JLabel("c"));
        pane.add(c);
        pane.add(new JLabel("d"));
        pane.add(d);

        dialog.add(pane, BorderLayout.NORTH);
        dialog.add(submit, BorderLayout.SOUTH);
        dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);

      };
      buttonGraphConfig.addActionListener(lc);

      JButton buttonAbout = new JButton(new ImageIcon(ImageIO.read(getClass().getResource("./resourses/about.png"))));
      ActionListener la = (e) -> {
        JOptionPane.showMessageDialog(this,  "Isoline - The Graph.\nVersion 0.1\nBy Egor Pyataev - Group 15206");
      };

      buttonAbout.addActionListener(la);
      toolBar.add(buttonNew);
      toolBar.add(buttonExit);
      toolBar.addSeparator();
      toolBar.add(buttonInterpolation);
      toolBar.add(buttonIsolines);
      toolBar.add(buttonMouseIsoline);
      toolBar.add(buttonGrid);
      toolBar.add(buttonEntryPoints);
      toolBar.add(buttonGraphConfig);
      toolBar.addSeparator();
      toolBar.add(buttonAbout);

      mFileNew.addActionListener(ln);
      mFileQuit.addActionListener(le);
      mAboutInfo.addActionListener(la);
      mGraphInterpolation.addItemListener((e) -> {
        if(e.getStateChange() == ItemEvent.SELECTED){
          buttonInterpolation.setSelected(true);
        }
        else if(e.getStateChange() == ItemEvent.DESELECTED){
          buttonInterpolation.setSelected(false);
        }
      });
      mGraphIsolines.addItemListener((e) -> {
        if(e.getStateChange() == ItemEvent.SELECTED){
          buttonIsolines.setSelected(true);
        }
        else if(e.getStateChange() == ItemEvent.DESELECTED){
          buttonIsolines.setSelected(false);
        }
      });
      mGraphDIsolines.addItemListener((e) -> {
        if(e.getStateChange() == ItemEvent.SELECTED){
          buttonMouseIsoline.setSelected(true);
        }
        else if(e.getStateChange() == ItemEvent.DESELECTED){
          buttonMouseIsoline.setSelected(false);
        }
      });
      mGraphGrid.addItemListener((e) -> {
        if(e.getStateChange() == ItemEvent.SELECTED){
          buttonGrid.setSelected(true);
        }
        else if(e.getStateChange() == ItemEvent.DESELECTED){
          buttonGrid.setSelected(false);
        }
      });
      mGraphEntryPoints.addItemListener((e) -> {
        if(e.getStateChange() == ItemEvent.SELECTED){
          buttonEntryPoints.setSelected(true);
        }
        else if(e.getStateChange() == ItemEvent.DESELECTED){
          buttonEntryPoints.setSelected(false);
        }
      });
      mGraphConfig.addActionListener(lc);
    }
    catch(Exception ex){
      System.err.println(ex.getMessage());
    }
    toolBar.setFloatable(false);
    toolBar.setRollover(true);

    add(toolBar, BorderLayout.PAGE_START);
    JScrollPane scrollpane = new JScrollPane(panel);
    setPreferredSize(new Dimension(800, 600));
    add(scrollpane, BorderLayout.CENTER);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    pack();
    setLocationRelativeTo(null);
    setVisible(true);
    // setResizable(false);
    // System.out.println(menu.getHeight());
  }
}
