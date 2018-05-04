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
  private BSplinePanel bsplinePanel;

  public GUI(){
    super("Wireframe - 3D Graphics.");

    Rectangle[] points = new Rectangle[8];
    int sign = -1;
    for(int i = 0; i < points.length; i++){
      points[i] = new Rectangle(14 + 80 * i, 120 + i * 30, 12, 12);
    }

    bsplinePanel = new BSplinePanel(points);
    RotatingFigure rotatePane = new RotatingFigure();

    JMenuBar menu = new JMenuBar();

    JMenu mFile = new JMenu("File");
    JMenu mWireframe = new JMenu("Wireframe");
    JMenu mAbout = new JMenu("About");

    JMenuItem mFileNew = new JMenuItem("New");
    JMenuItem mFileQuit = new JMenuItem("Quit");
    JMenuItem mWireframeBSettings = new JMenuItem("B-Spline settings");
    // JMenuItem mWireframeCSettings = new JMenuItem("Common settings");
    JMenuItem mAboutInfo = new JMenuItem("Info");

    mFile.add(mFileNew);
    mFile.addSeparator();
    mFile.add(mFileQuit);

    mWireframe.add(mWireframeBSettings);
    // mWireframe.add(mWireframeCSettings);

    mAbout.add(mAboutInfo);

    menu.add(mFile);
    menu.add(mWireframe);
    menu.add(mAbout);

    setJMenuBar(menu);

    JToolBar toolBar = new JToolBar();
    try{
      JButton buttonNew = new JButton(new ImageIcon(ImageIO.read(getClass().getResource("./resourses/new.png"))));
      ActionListener ln = (e) -> {
        System.out.println("New");
      };
      buttonNew.addActionListener(ln);

      JButton buttonExit = new JButton(new ImageIcon(ImageIO.read(getClass().getResource("./resourses/exit.png"))));
      ActionListener le = (e) -> {
        System.exit(0);
      };
      buttonExit.addActionListener(le);

      JButton buttonWireframeBSettings = new JButton(new ImageIcon(ImageIO.read(getClass().getResource("./resourses/spline.png"))));
      ActionListener wbs = (e) -> {
        JDialog dialog = new JDialog(this, "B-Spline settings", true);
        JButton submit = new JButton("Apply");
        JPanel settings = new JPanel();
        settings.setLayout(new GridLayout(1, 7));

        TextField N = new TextField("5", 5);
        TextField M = new TextField("8", 5);
        TextField K = new TextField("5", 5);
        settings.add(new JLabel("N", SwingConstants.RIGHT));
        settings.add(N);
        settings.add(new JLabel("M", SwingConstants.RIGHT));
        settings.add(M);
        settings.add(new JLabel("K", SwingConstants.RIGHT));
        settings.add(K);

        bsplinePanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("B - Spline"), BorderFactory.createEmptyBorder(1,1,1,1)));
        settings.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Settings"), BorderFactory.createEmptyBorder(1,1,1,1)));
        submit.addActionListener((ex) -> {
          int n = Integer.valueOf(N.getText());
          int m = Integer.valueOf(M.getText());;
          int k = Integer.valueOf(K.getText());;
          Point2D.Double[] ps = bsplinePanel.getFigurePoints(n, k);
          rotatePane.setFigureParams(ps, m, k);
          // for(int i = 0; i < ps.length; i++){
          //   System.out.println(ps[i]);
          // }

          dialog.dispose();
        });
        settings.add(submit);
        dialog.add(bsplinePanel);
        dialog.add(settings, BorderLayout.SOUTH);
        dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        dialog.pack();
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
      };
      buttonWireframeBSettings.addActionListener(wbs);

      // JButton buttonWireframeCSettings = new JButton(new ImageIcon(ImageIO.read(getClass().getResource("./resourses/settings.png"))));
      // ActionListener wcs = (e) -> {
      //   System.out.println("WCS");
      // };
      // buttonWireframeCSettings.addActionListener(wcs);

      JButton buttonAbout = new JButton(new ImageIcon(ImageIO.read(getClass().getResource("./resourses/about.png"))));
      ActionListener la = (e) -> {
        JOptionPane.showMessageDialog(this,  "Wireframe - 3D Graphics.\nVersion 0.1\nBy Egor Pyataev - Group 15206");
      };
      buttonAbout.addActionListener(la);

      toolBar.add(buttonNew);
      toolBar.add(buttonExit);
      toolBar.addSeparator();
      toolBar.add(buttonWireframeBSettings);
      // toolBar.add(buttonWireframeCSettings);
      toolBar.addSeparator();
      toolBar.add(buttonAbout);

      mFileNew.addActionListener(ln);
      mFileQuit.addActionListener(le);
      mWireframeBSettings.addActionListener(wbs);
      // mWireframeCSettings.addActionListener(wcs);
      mAboutInfo.addActionListener(la);
    }
    catch(Exception ex){
      System.err.println(ex.getMessage());
    }

    toolBar.setFloatable(false);
    toolBar.setRollover(true);

    add(toolBar, BorderLayout.PAGE_START);
    JScrollPane scrollpane = new JScrollPane(rotatePane);
    setPreferredSize(new Dimension(800, 600));
    add(scrollpane, BorderLayout.CENTER);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    pack();
    setLocationRelativeTo(null);
    setVisible(true);
  }
}
