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
    super("Filter - The Graphs.");

    IsolinePane panel = new IsolinePane();
    JMenuBar menu = new JMenuBar();

    JMenu mFile = new JMenu("File");
    JMenu mAbout = new JMenu("About");

    JMenuItem mFileNew = new JMenuItem("New");
    JMenuItem mFileQuit = new JMenuItem("Quit");
    JMenuItem mAboutInfo = new JMenuItem("Info");

    mFile.add(mFileNew);
    mFile.addSeparator();
    mFile.add(mFileQuit);

    mAbout.add(mAboutInfo);

    menu.add(mFile);
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

        }
      };
      buttonNew.addActionListener(ln);

      JButton buttonExit = new JButton(new ImageIcon(ImageIO.read(getClass().getResource("./resourses/exit.png"))));
      ActionListener le = (e) -> {
        System.exit(0);
      };
      buttonExit.addActionListener(le);

      toolBar.add(buttonNew);
      toolBar.add(buttonExit);

      mFileNew.addActionListener(ln);
      mFileQuit.addActionListener(le);
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
  }
}
