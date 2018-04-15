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
import javax.swing.border.*;

class IsolinePane extends JPanel{
  private BufferedImage portrait;
  private Rectangle portraitBounds;
  private Dimension curSize;
  private Dimension curPanelSize;
  private BufferedImage legend;
  private BufferedImage grid;
  private BufferedImage isolines;
  private BufferedImage entryPoints;
  private BufferedImage dinamicLines;
  private boolean dinamic = false;
  private double curLevel = 0;
  private short[][] cells;
  private double[] leftUpCorner = {-5.75, -3};
  private double[] rightDownCorner = {5.75, 3};
  private double stepX, stepY;
  private short[] km = {5, 5};
  private int[] colors = {
    Color.RED.getRGB(),
    Color.ORANGE.getRGB(),
    Color.YELLOW.getRGB(),
    Color.GREEN.getRGB(),
    Color.CYAN.getRGB(),
    Color.BLUE.getRGB(),
    Color.MAGENTA.getRGB(),
    Color.BLACK.getRGB()
  };
  private int N = 6;
  private Function f;
  private Function leg;
  private boolean showGrid = false;
  private boolean showIsolines = false;
  private boolean showEntries = false;
  private boolean isInterpolation = false;

  public void setGraphSettings(short[] km, int[][] colors){
    this.km = km;
    this.colors = new int[colors.length];
    for(int i = 0; i < colors.length; i++){
      this.colors[i] = new Color(colors[i][0], colors[i][1], colors[i][2]).getRGB();
    }
    this.N = colors.length - 2;

    drawGraph(this.portrait, f);
    drawGraph(this.legend, leg);
    repaint();
  }

  public void drawGraph(BufferedImage portrait, Function f){
    double min = f.getMinimum(leftUpCorner[0], leftUpCorner[1], rightDownCorner[0], rightDownCorner[1], stepX, stepY);
    double max = f.getMaximum(leftUpCorner[0], leftUpCorner[1], rightDownCorner[0], rightDownCorner[1], stepX, stepY);

    double[] levels = new double[N + 1];
    double div = (max - min) / (N + 1);
    // System.out.println("min " + min);
    for(int i = 0; i < N + 1; i++) {
      levels[i] = min + div * i;
      // System.out.println(levels[i]);
      if(isInterpolation){
        if(i != 0){
          levels[i] += div / 2;
        }
      }
    }

    double llevel, level, rlevel;
    int lcolor, color, rcolor;
    double val, interval;
    int p, la, ra, lr, rr, lg, rg, lb, rb, a, r, g, b;

    for(int i = 0; i < portrait.getWidth(); i++){
      for(int j = 0; j < portrait.getHeight(); j++){
        val = f.function(i * stepX + leftUpCorner[0], j * stepY + leftUpCorner[1]);
        for(int c = 0; c < N + 1; c++){
          lcolor = N - c;
          llevel = levels[lcolor];

          if(val > llevel){
            if(!isInterpolation){
              portrait.setRGB(i, j, colors[N - c]);
            }
            else{
              la = (colors[lcolor] >> 24) & 0xff;
              lr = (colors[lcolor] >> 16) & 0xff;
              lg = (colors[lcolor] >>  8) & 0xff;
              lb = (colors[lcolor]      ) & 0xff;

              rcolor = c == 0 ? N - c : N - c + 1;
              rlevel = levels[rcolor];

              ra = (colors[rcolor] >> 24) & 0xff;
              rr = (colors[rcolor] >> 16) & 0xff;
              rg = (colors[rcolor] >>  8) & 0xff;
              rb = (colors[rcolor]      ) & 0xff;

              interval = rlevel - llevel;

              a = (int)((double)la * (rlevel - val) / interval + (double)ra * (val - llevel) / interval);
              r = (int)((double)lr * (rlevel - val) / interval + (double)rr * (val - llevel) / interval);
              g = (int)((double)lg * (rlevel - val) / interval + (double)rg * (val - llevel) / interval);
              b = (int)((double)lb * (rlevel - val) / interval + (double)rb * (val - llevel) / interval);
              p = (a << 24) | (r << 16) | (g << 8) | b;
              if(rlevel == llevel){
                p = colors[lcolor];
              }
              portrait.setRGB(i, j, p);
            }
            break;
          }
        }
      }
    }
  }

  public void drawGrid(){
    grid = new BufferedImage(portrait.getWidth(), portrait.getHeight(), BufferedImage.TYPE_INT_ARGB);

    cells = new short[(km[0]) * (km[1])][4];

    Graphics g = grid.createGraphics();
    g.setColor(Color.BLACK);
    short numX = (short)(grid.getWidth() / km[0]);
    short remX = (short)(grid.getWidth() % km[0]);
    short numY = (short)(grid.getHeight() / km[1]);
    short remY = (short)(grid.getHeight() % km[1]);
    // System.out.println(numX + " " + numY);
    // System.out.println(remX + " " + remY);
    int n = 0;
    for(int i = 1; i < km[0]; i++){
      drawDashedLine(g, remX / 2 + numX * i, 0, remX / 2 + numX * i, grid.getHeight());
    }
    n = 0;
    for(int i = 1; i < km[1]; i++){
      drawDashedLine(g, 0, remY / 2 + numY * i, grid.getWidth(), remY / 2 + numY * i);
    }
    short k = 0;
    short m = 0;
    for(int i = 0; i < cells.length; i++){
      cells[i][0] = (short)(numX * k       + (k == 0 ? 0 : remX / 2) + ((k) == km[0]? remX / 2 : 0));
      cells[i][1] = (short)(numY * m       + (m == 0 ? 0 : remY / 2) + ((m) == km[1]? remY / 2 : 0));
      cells[i][2] = (short)(numX * (k + 1) + remX / 2 + ((k + 1) == km[0] ? remX / 2 : 0));
      cells[i][3] = (short)(numY * (m + 1) + remY / 2 + ((m + 1) == km[1] ? remY / 2 : 0));
      k++;
      if(k == km[0]){
        k = 0;
        ++m;
      }
      // drawCenteredCircle((Graphics2D)g, cells[i][0], cells[i][1], 10);
      // drawCenteredCircle((Graphics2D)g, cells[i][2], cells[i][3], 10);
    }
  }

  public void setShowGrid(boolean grid){
    showGrid = grid;
    repaint();
  }

  public void drawDinamicLines(double level){
    // dinamicLines = new BufferedImage(portrait.getWidth(), portrait.getHeight(), BufferedImage.TYPE_INT_ARGB);
    Graphics g = (dinamicLines.createGraphics());
    g.setColor(new Color(colors[colors.length - 1]));
    int entries = 0;
    Point[] p = new Point[4];
    p[0] = null;
    p[1] = null;
    p[2] = null;
    p[3] = null;
    double f1, f2, t;
    int x, dx, y, dy;
    double min = f.getMinimum(leftUpCorner[0], leftUpCorner[1], rightDownCorner[0], rightDownCorner[1], stepX, stepY);
    double max = f.getMaximum(leftUpCorner[0], leftUpCorner[1], rightDownCorner[0], rightDownCorner[1], stepX, stepY);

    double[] levels = new double[N + 1];
    double div = (max - min) / (N + 1);

    for(int i = 0; i < N + 1; i++) {
      levels[i] = min + div * i;
    }

    for(int i = 0; i < cells.length; i++){
      dx = cells[i][2] - cells[i][0];
      dy = cells[i][3] - cells[i][1];
      double z = level;
      p[0] = null;
      p[1] = null;
      p[2] = null;
      p[3] = null;
      f1 = f.function(cells[i][0] * stepX + leftUpCorner[0], cells[i][1] * stepY + leftUpCorner[1]);
      f2 = f.function(cells[i][2] * stepX + leftUpCorner[0], cells[i][1] * stepY + leftUpCorner[1]);

      if((z < f1 && z < f2) || (z > f1 && z > f2)){
        entries = entries;
      }
      else{
        ++entries;
        if(f2 > f1){
          x = cells[i][0] + (int)((double)dx * (z - f1) / (f2 - f1));
        }
        else{
          x = cells[i][2] - (int)((double)dx * (z - f2) / (f1 - f2));
        }
        y = cells[i][1];
        p[0] = new Point(x, y);
      }

      f1 = f.function(cells[i][2] * stepX + leftUpCorner[0], cells[i][1] * stepY + leftUpCorner[1]);
      f2 = f.function(cells[i][2] * stepX + leftUpCorner[0], cells[i][3] * stepY + leftUpCorner[1]);

      if((z < f1 && z < f2) || (z > f1 && z > f2)){
        entries = entries;
      }
      else{
        ++entries;
        x = cells[i][2];
        if(f2 > f1){
          y = cells[i][1] + (int)((double)dy * (z - f1) / (f2 - f1));
        }
        else{
          y = cells[i][3] - (int)((double)dy * (z - f2) / (f1 - f2));
        }
        p[1] = new Point(x, y);
      }

      f1 = f.function(cells[i][2] * stepX + leftUpCorner[0], cells[i][3] * stepY + leftUpCorner[1]);
      f2 = f.function(cells[i][0] * stepX + leftUpCorner[0], cells[i][3] * stepY + leftUpCorner[1]);

      if((z < f1 && z < f2) || (z > f1 && z > f2)){
        entries = entries;
      }
      else{
        ++entries;
        if(f2 > f1){
          x = cells[i][2] - (int)((double)dx * (z - f1) / (f2 - f1));
        }
        else{
          x = cells[i][0] + (int)((double)dx * (z - f2) / (f1 - f2));
        }
        y = cells[i][3];
        p[2] = new Point(x, y);

      }

      f1 = f.function(cells[i][0] * stepX + leftUpCorner[0], cells[i][3] * stepY + leftUpCorner[1]);
      f2 = f.function(cells[i][0] * stepX + leftUpCorner[0], cells[i][1] * stepY + leftUpCorner[1]);

      if((z < f1 && z < f2) || (z > f1 && z > f2)){
        entries = entries;
      }
      else{
        ++entries;
        x = cells[i][0];
        if(f2 > f1){
          y = cells[i][3] - (int)((double)dy * (z - f1) / (f2 - f1));
        }
        else{
          y = cells[i][1] + (int)((double)dy * (z - f2) / (f1 - f2));
        }
        p[3] = new Point(x, y);
      }
      int count = 0;
      Point p1[] = new Point[2];

      for(int q = 0; q < p.length; q++){
        // System.out.println(p[q]);
        if(p[q] != null){
          p1[count] = new Point((int)p[q].getX(), (int)p[q].getY());
          ++count;
        }
        if(count == 2){
          break;
        }
      }
      Graphics2D g2d = (Graphics2D)g;
      g2d.setColor(new Color(colors[colors.length - 1]));
      g2d.setStroke(new BasicStroke(2));
      g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

      if(!(p1[0] == null || p1[1] == null)) g2d.drawLine(p1[0].x, p1[0].y, p1[1].x, p1[1].y);
    }
  }

  public void setDinamic(boolean d){
    dinamic = d;
  }

  public void drawIsolines(){
    isolines = new BufferedImage(portrait.getWidth(), portrait.getHeight(), BufferedImage.TYPE_INT_ARGB);
    entryPoints = new BufferedImage(portrait.getWidth(), portrait.getHeight(), BufferedImage.TYPE_INT_ARGB);
    Graphics g = (isolines.createGraphics());
    g.setColor(new Color(colors[colors.length - 1]));
    Graphics2D g1 = (Graphics2D)(entryPoints.createGraphics());
    g1.setColor(new Color(colors[colors.length - 1]));
    int entries = 0;
    Point[] p = new Point[4];
    p[0] = null;
    p[1] = null;
    p[2] = null;
    p[3] = null;
    double f1, f2, t;
    int x, dx, y, dy;
    double min = f.getMinimum(leftUpCorner[0], leftUpCorner[1], rightDownCorner[0], rightDownCorner[1], stepX, stepY);
    double max = f.getMaximum(leftUpCorner[0], leftUpCorner[1], rightDownCorner[0], rightDownCorner[1], stepX, stepY);

    double[] levels = new double[N + 1];
    double div = (max - min) / (N + 1);

    for(int i = 0; i < N + 1; i++) {
      levels[i] = min + div * i;
      // System.out.println(levels[i]);
      // if(isInterpolation){
      //   if(i != 0){
      //     levels[i] += div / 2;
      //   }
      // }
    }

    for(int i = 0; i < cells.length; i++){
      dx = cells[i][2] - cells[i][0];
      dy = cells[i][3] - cells[i][1];
      for(int l = 1; l < levels.length; l++){
        double z = levels[l];
        p[0] = null;
        p[1] = null;
        p[2] = null;
        p[3] = null;
        f1 = f.function(cells[i][0] * stepX + leftUpCorner[0], cells[i][1] * stepY + leftUpCorner[1]);
        f2 = f.function(cells[i][2] * stepX + leftUpCorner[0], cells[i][1] * stepY + leftUpCorner[1]);

        if((z < f1 && z < f2) || (z > f1 && z > f2)){
          entries = entries;
        }
        else{
          ++entries;
          if(f2 > f1){
            x = cells[i][0] + (int)((double)dx * (z - f1) / (f2 - f1));
          }
          else{
            x = cells[i][2] - (int)((double)dx * (z - f2) / (f1 - f2));
          }
          y = cells[i][1];
          p[0] = new Point(x, y);
          drawCenteredCircle(g1, x, y, 5);
        }

        f1 = f.function(cells[i][2] * stepX + leftUpCorner[0], cells[i][1] * stepY + leftUpCorner[1]);
        f2 = f.function(cells[i][2] * stepX + leftUpCorner[0], cells[i][3] * stepY + leftUpCorner[1]);

        if((z < f1 && z < f2) || (z > f1 && z > f2)){
          entries = entries;
        }
        else{
          ++entries;
          x = cells[i][2];
          if(f2 > f1){
            y = cells[i][1] + (int)((double)dy * (z - f1) / (f2 - f1));
          }
          else{
            y = cells[i][3] - (int)((double)dy * (z - f2) / (f1 - f2));
          }
          p[1] = new Point(x, y);
          drawCenteredCircle(g1, x, y, 5);
        }

        f1 = f.function(cells[i][2] * stepX + leftUpCorner[0], cells[i][3] * stepY + leftUpCorner[1]);
        f2 = f.function(cells[i][0] * stepX + leftUpCorner[0], cells[i][3] * stepY + leftUpCorner[1]);

        if((z < f1 && z < f2) || (z > f1 && z > f2)){
          entries = entries;
        }
        else{
          ++entries;
          if(f2 > f1){
            x = cells[i][2] - (int)((double)dx * (z - f1) / (f2 - f1));
          }
          else{
            x = cells[i][0] + (int)((double)dx * (z - f2) / (f1 - f2));
          }
          y = cells[i][3];
          p[2] = new Point(x, y);
          drawCenteredCircle(g1, x, y, 5);

        }

        f1 = f.function(cells[i][0] * stepX + leftUpCorner[0], cells[i][3] * stepY + leftUpCorner[1]);
        f2 = f.function(cells[i][0] * stepX + leftUpCorner[0], cells[i][1] * stepY + leftUpCorner[1]);

        if((z < f1 && z < f2) || (z > f1 && z > f2)){
          entries = entries;
        }
        else{
          ++entries;
          x = cells[i][0];
          if(f2 > f1){
            y = cells[i][3] - (int)((double)dy * (z - f1) / (f2 - f1));
          }
          else{
            y = cells[i][1] + (int)((double)dy * (z - f2) / (f1 - f2));
          }
          p[3] = new Point(x, y);
          drawCenteredCircle(g1, x, y, 5);
        }
        int count = 0;
        Point p1[] = new Point[2];

        for(int q = 0; q < p.length; q++){
          // System.out.println(p[q]);
          if(p[q] != null){
            p1[count] = new Point((int)p[q].getX(), (int)p[q].getY());
            ++count;
          }
          if(count == 2){
            break;
          }
        }
        Graphics2D g2d = (Graphics2D)g;
        g2d.setColor(new Color(colors[colors.length - 1]));
        g2d.setStroke(new BasicStroke(2));
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        ((Graphics2D)g1).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // System.out.println(p1[0] + " " + p1[1]);
        if(!(p1[0] == null || p1[1] == null)) g2d.drawLine(p1[0].x, p1[0].y, p1[1].x, p1[1].y);
      }
    }
  }

  public void setShowIsolines(boolean iso){
    showIsolines = iso;
    repaint();
  }

  public void setShowEntries(boolean en){
    showEntries = en;
    repaint();
  }

  public void setInterpolation(boolean interpolation){
    isInterpolation = interpolation;
    drawGraph(this.portrait, f);
    drawGraph(this.legend, leg);
    repaint();
  }

  public IsolinePane(int width, int height){
    setPreferredSize(new Dimension(width, height));
    setLayout(new BorderLayout());
    f = new Function(){
      @Override
      public double function(double x, double y){
        // Ackley function
        // return -20 * Math.exp(-0.2 * Math.sqrt(0.5 * (x * x + y * y))) - Math.exp(0.5 * (Math.cos(2 * Math.PI * x) + Math.cos(2 * Math.PI * y))) + Math.E + 20;
        // return x * x + y * y;
        return Math.sin(x) + Math.cos(y);
        // return x;
      }
    };

    leg = new Function(){
      @Override
      public double function(double x, double y){
        return x;
      }
    };

    JPanel statusBar = new JPanel();

    TextField x = new TextField(" x: none", 7);
    TextField y = new TextField(" y: none", 7);
    TextField func = new TextField(" f: none", 7);

    x.setEnabled(false);
    y.setEnabled(false);
    func.setEnabled(false);

    statusBar.add(x);
    statusBar.add(y);
    statusBar.add(func);

    add(statusBar, BorderLayout.SOUTH);

    curSize = new Dimension(767, 395);
    curPanelSize = new Dimension(width, height);

    addMouseListener(new MouseAdapter(){
      @Override
      public void mousePressed(MouseEvent e){
        if(portraitBounds.contains(e.getPoint())){
          drawDinamicLines(f.function((e.getX() - 10) * stepX + leftUpCorner[0], (e.getY() - 10) * stepY + leftUpCorner[1]));
          repaint();
        }
      }

      @Override
      public void mouseReleased(MouseEvent e){
        dinamicLines = new BufferedImage(portrait.getWidth(), portrait.getHeight(), BufferedImage.TYPE_INT_ARGB);
        repaint();
      }
    });

    addMouseMotionListener(new MouseMotionAdapter(){
      @Override
      public void mouseMoved(MouseEvent e){
        Point p = e.getPoint();
        if(portraitBounds.contains(p)){
          double xc = leftUpCorner[0] + (p.getX() - 10) * stepX;
          double yc = leftUpCorner[1] + (p.getY() - 10) * stepY;

          x.setText(String.format(" x: %.2f", xc));
          y.setText(String.format(" y: %.2f", yc));
          func.setText(String.format(" f: %.2f", f.function(xc, yc)));
        }
      }

      @Override
      public void mouseDragged(MouseEvent e){
        Point p = e.getPoint();
        if(portraitBounds.contains(p)){
          double xc = leftUpCorner[0] + (p.getX() - 10) * stepX;
          double yc = leftUpCorner[1] + (p.getY() - 10) * stepY;

          double level = f.function((e.getX() - 10) * stepX + leftUpCorner[0], (e.getY() - 10) * stepY + leftUpCorner[1]);

          x.setText(String.format(" x: %.2f", xc));
          y.setText(String.format(" y: %.2f", yc));
          func.setText(String.format(" f: %.2f", f.function(xc, yc)));

          if(dinamic){
            if(curLevel != level){
              dinamicLines = new BufferedImage(portrait.getWidth(), portrait.getHeight(), BufferedImage.TYPE_INT_ARGB);
            }
            drawDinamicLines(level);
            repaint();
          }
        }
      }
    });

    addComponentListener(new ComponentAdapter() {
        @Override
        public void componentResized(ComponentEvent e){
          Dimension newPanelSize = getSize();
          curSize = new Dimension(curSize.width + newPanelSize.width - curPanelSize.width, curSize.height + newPanelSize.height - curPanelSize.height);
          curPanelSize = newPanelSize;
          drawImages(curSize);
          repaint();
        }
    });
  }

  public double[] getSettings(){
    double[] s = new double[6];

    for(int i = 0; i < 2; i++){
      s[i] = km[i];
      s[i + 2] = leftUpCorner[i];
      s[i + 4] = rightDownCorner[i];
    }

    return s;
  }

  public void setSettings(double[] s){
    km[0] = (short)s[0];
    km[1] = (short)s[1];
    leftUpCorner[0] = s[2];
    leftUpCorner[1] = s[3];
    rightDownCorner[0] = s[4];
    rightDownCorner[1] = s[5];
    drawImages(curSize);
    repaint();
  }

  public void drawImages(Dimension size){
    portrait = new BufferedImage(curSize.width, curSize.height, BufferedImage.TYPE_INT_ARGB);
    portraitBounds = new Rectangle(10, 10, portrait.getWidth(), portrait.getHeight());

    stepX = (rightDownCorner[0] - leftUpCorner[0]) / portrait.getWidth();
    stepY = (rightDownCorner[1] - leftUpCorner[1]) / portrait.getHeight();

    legend = new BufferedImage(portrait.getWidth(), 40, BufferedImage.TYPE_INT_ARGB);
    dinamicLines = new BufferedImage(portrait.getWidth(), portrait.getHeight(), BufferedImage.TYPE_INT_ARGB);

    drawGraph(this.portrait, f);
    drawGraph(this.legend, leg);
    drawGrid();
    drawIsolines();
  }

  public void drawDashedLine(Graphics g, int x1, int y1, int x2, int y2){
    Graphics2D g2d = (Graphics2D)g;
    Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{1}, 0);
    g2d.setStroke(dashed);
    g2d.drawLine(x1, y1, x2, y2);
  }

  public void drawCenteredCircle(Graphics2D g, int x, int y, int r) {
    x = x - (r / 2);
    y = y - (r / 2);
    g.setColor(new Color(colors[colors.length - 1]));
    g.fillOval(x,y,r,r);
  }

  @Override
  public void paint(Graphics g){
    super.paint(g);
    int wG = portrait.getWidth();
    int hG = portrait.getHeight();
    int wL = legend.getWidth();
    int hL = legend.getHeight();
    int width = 10 + wG + 10;
    int height = 10 + hG + 10 + hL + 10;
    double scale1;
    double scale2;
    double scale = 1;

    g.drawImage(portrait, 10, 10, (int)(wG * scale), (int)(hG * scale), null);
    g.drawImage(legend, 10, 10 + (int)(hG * scale) + 10, (int)(wL * scale), (int)(hL), null);
    if(showGrid) g.drawImage(grid, 10, 10, (int)(wG * scale), (int)(hG * scale), null);
    if(showIsolines) g.drawImage(isolines, 10, 10, (int)(wG * scale), (int)(hG * scale), null);
    if(showEntries) g.drawImage(entryPoints, 10, 10, (int)(wG * scale), (int)(hG * scale), null);
    if(dinamic) g.drawImage(dinamicLines, 10, 10, (int)(wG * scale), (int)(hG * scale), null);
  }
}
