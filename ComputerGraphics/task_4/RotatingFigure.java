import java.awt.*;
import java.awt.geom.*;
import static java.lang.Math.*;
import javax.swing.*;
import java.awt.event.*;

public class RotatingFigure extends JPanel {
  // int N = 8;
  private int N;
  private int K;
  // int nodesN = 4;
  private int nodesN;
  // int edgesN = nodesN - 1;
  private int edgesN;
  private double ox = 0, dx = 0;
  private double oy = 0, dy = 0;
  private double rx = 0, ry = 0;
  // double[][] startNodes = {{2, 0, 2}, {2 / Math.sqrt(2), 0, 0}, {2 / Math.sqrt(2), 0, -1}, {2, 0, -2}};
  private double[][] vectorX = {{0, 0, 0}, {1, 0, 0}};
  private double[][] vectorY = {{0, 0, 0}, {0, 1, 0}};
  private double[][] vectorZ = {{0, 0, 0}, {0, 0, 1}};

  private double[][] startNodes = null;
  private double[][][] nodes;
  private double[][][] nodesWorld;
  private double[][][] nodesCamera;
  private int[][] edges;
  private double[][] perspecMatrix;
  private int camPos = 10;

  public RotatingFigure(){
    setPreferredSize(new Dimension(787, 498));
    setBackground(Color.white);

    perspecMatrix = new double[4][4];

    double sw = 0.8;
    double sh = 0.8;
    double zf = 5.;
    double zb = 10.;

    perspecMatrix[0][0] = 2. * zf / sw;
    perspecMatrix[1][1] = 2. * zf / sh;
    perspecMatrix[2][2] = zb / (zb - zf);
    perspecMatrix[2][3] = -(zf * zb) / (zb - zf);
    perspecMatrix[3][2] = 1;

    // nodes = new double[N][nodesN][3];
    // nodes[0] = startNodes;
    // edges = new int[edgesN][2];
    //
    // for(int i = 0; i < edgesN; i++){
    //   edges[i][0] = i;
    //   edges[i][1] = i + 1;
    // }

    // rotateAroundZ();

    // nodesWorld = new double[N][nodesN][3];
    // nodesCamera = new double[N][nodesN][3];

    // rotateFigure(0, 0);

    addMouseListener(new MouseAdapter(){
      @Override
      public void mousePressed(MouseEvent e){
        if(startNodes != null){
          dx = e.getX();
          dy = e.getY();
          // rx = 0;
          // ry = 0;
          ox = dx;
          oy = dy;
        }
      }
    });

    addMouseMotionListener(new MouseMotionAdapter(){
      @Override
      public void mouseDragged(MouseEvent e){
        if(startNodes != null){
          int x = e.getX();
          int y = e.getY();
          dx = x - ox;
          dy = y - oy;
          // if(dx != 0 && dy != 0) return;
          rotateFigure(PI * dy / 180, 0);
          rotateFigure(0, PI * (-dx) / 180);
          perspec();

          ry += (dy == 0 ? 0 : (dy/abs(dy)));
          rx += (dx == 0 ? 0 : (-dx/abs(dx)));
          if(rx > 359) rx -= 360;
          if(rx < 0) rx += 360;
          if(ry > 359) ry -= 360;
          if(ry < 0) ry += 360;
          ox = x;
          oy = y;

          // System.out.println(y + " " + x);
          // System.out.println(dy + " " + dx);
          // System.out.println(ry + " " + rx);
          repaint();
        }
      }
    });

    addMouseWheelListener(new MouseWheelListener(){
      public void mouseWheelMoved(MouseWheelEvent e) {
        if(startNodes != null){
          camPos += e.getWheelRotation();
          if(camPos < -5) camPos = -5;
          rotateFigure(0, 0);
          perspec();
          repaint();
        }
      }
    });
  }

  private final void scale(double s) {
    for(double[][] edge : nodesCamera) {
      for(double[] node : edge) {
        node[0] *= s;
        node[1] *= s;
        node[2] *= s;
      }
    }
  }

  public void setFigureParams(Point2D.Double[] ps, int n, int k){
    N = n;
    K = k;
    nodesN = ps.length;
    edgesN = nodesN - 1;
    startNodes = new double[nodesN][3];
    nodesWorld = new double[N * K][nodesN][3];
    nodesCamera = new double[N * K][nodesN][3];

    for(int i = 0; i < nodesN; i++){
      startNodes[i][0] = 0;
      startNodes[i][1] = ps[i].y;
      startNodes[i][2] = ps[i].x;
    }

    rotateAroundZ();

    // rotateFigure((ry) * PI / 180, (rx) * PI / 180);
    // rotateFigure((45) * PI / 180, 0);
    // rotateFigure(0, (-10) * PI / 180);
    // rotateFigure(0, (10) * PI / 180);
    // rotateFigure((-45) * PI / 180, 0);

    // rotateFigure((-45 + 360) * PI / 180, (-10 + 360) * PI / 180);

    perspec();
    repaint();
    System.out.println("ds " + ry + " " + rx);
  }

  private void rotateAroundZ(){
    nodes = new double[N * K][nodesN][3];
    nodes[0] = startNodes;
    edges = new int[edgesN][2];

    for(int i = 0; i < edgesN; i++){
      edges[i][0] = i;
      edges[i][1] = i + 1;
    }

    for(int i = 1; i < N * K; i++){
      double sina = sin(PI * i * (360. / (N * K)) / 180);
      double cosa = cos(PI * i * (360. / (N * K)) / 180);
      for(int j = 0; j < nodesN; j++){
        nodes[i][j][0] = startNodes[j][0] * cosa - startNodes[j][1] * sina;
        nodes[i][j][1] = startNodes[j][0] * sina + startNodes[j][1] * cosa;
        nodes[i][j][2] = startNodes[j][2];
      }
    }
  }

  private final void rotateFigure(double angleX, double angleY) {
    double sinX = sin(angleX);
    double cosX = cos(angleX);

    double sinY = sin(angleY);
    double cosY = cos(angleY);

    for(double[][] edge : nodes) {
      for(double[] node : edge) {
        double x = node[0];
        double y = node[1];
        double z = node[2];

        // node[0] = x * cosX - z * sinX;
        // node[2] = z * cosX + x * sinX;
        //
        // z = node[2];
        //
        // node[1] = y * cosY - z * sinY;
        // node[2] = z * cosY + y * sinY;

        node[0] = x;
        node[1] = y * cosX - z * sinX;
        node[2] = y * sinX + z * cosX;

        x = node[0];
        y = node[1];
        z = node[2];

        node[0] = x * cosY + z * sinY;
        node[1] = y;
        node[2] = -x * sinY + z * cosY;
      }
    }
  }

  private void perspec(){
    for(int i = 0; i < N * K; i++){
      for(int j = 0; j < nodesN; j++){
        nodesWorld[i][j][0] = nodes[i][j][0];
        nodesWorld[i][j][1] = nodes[i][j][1];
        nodesWorld[i][j][2] = nodes[i][j][2] + 10;
      }
    }

    for(int i = 0; i < N * K; i++){
      for(int j = 0; j < nodesN; j++){
        double x = nodesWorld[i][j][0];
        double y = nodesWorld[i][j][1];
        double z = nodesWorld[i][j][2] + camPos;
        nodesCamera[i][j][0] = x * perspecMatrix[0][0];
        nodesCamera[i][j][1] = y * perspecMatrix[1][1];
        nodesCamera[i][j][2] = z * perspecMatrix[2][2] + perspecMatrix[2][3];
        double w = z * perspecMatrix[3][2];
        nodesCamera[i][j][0] /= w;
        nodesCamera[i][j][1] /= w;
        nodesCamera[i][j][2] /= w;
      }
    }

    scale(100);
  }

  private void drawFigure(Graphics2D g) {
    g.translate(getWidth() / 2, getHeight() / 2);
    // g.setStroke(new BasicStroke(1));

    for(int[] edge : edges) {
      double[] xy1 = nodesCamera[0][edge[0]];
      double[] xy2 = nodesCamera[0][edge[1]];
      g.drawLine((int) round(xy1[0]), (int) round(xy1[1]), (int) round(xy2[0]), (int) round(xy2[1]));
      for(int j = 0; j < nodesN; j += K){
        xy1 = nodesCamera[N * K - 1][j];
        xy2 = nodesCamera[0][j];
        g.drawLine((int) round(xy1[0]), (int) round(xy1[1]), (int) round(xy2[0]), (int) round(xy2[1]));
      }
    }

    for(int[] edge : edges) {
      for(int i = 1; i < N * K; i++){
        double[] xy1 = nodesCamera[i][edge[0]];
        double[] xy2 = nodesCamera[i][edge[1]];
        if(i % K == 0) g.drawLine((int) round(xy1[0]), (int) round(xy1[1]), (int) round(xy2[0]), (int) round(xy2[1]));
        for(int j = 0; j < nodesN; j += K){
          xy1 = nodesCamera[i - 1][j];
          xy2 = nodesCamera[i]    [j];
          g.drawLine((int) round(xy1[0]), (int) round(xy1[1]), (int) round(xy2[0]), (int) round(xy2[1]));
        }
      }
    }

    // for (double[][] edge : nodesCamera){
    //   for(double[] node : edge){
    //     g.fillOval((int) round(node[0]) - 4, (int) round(node[1]) - 4, 8, 8);
    //   }
    // }
  }

  @Override
  public void paintComponent(Graphics gg) {
    super.paintComponent(gg);
    Graphics2D g = (Graphics2D) gg;

    if(startNodes != null) drawFigure(g);
  }

}
