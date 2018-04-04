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

    JMenuItem mGraphConfig = new JMenuItem("Config");

    mFile.add(mFileNew);
    mFile.addSeparator();
    mFile.add(mFileQuit);

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
            int km[] = null;
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
                  km = new int[2];
                  km[0] = Integer.valueOf(ars[0]);
                  km[1] = Integer.valueOf(ars[1]);
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

      JButton buttonGraphConfig = new JButton(new ImageIcon(ImageIO.read(getClass().getResource("./resourses/config.png"))));
      ActionListener lc = (e) -> {
        System.out.println("config");
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
      toolBar.add(buttonGraphConfig);
      toolBar.addSeparator();
      toolBar.add(buttonAbout);

      mFileNew.addActionListener(ln);
      mFileQuit.addActionListener(le);
      mAboutInfo.addActionListener(la);
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
    System.out.println(menu.getHeight());
  }
}
