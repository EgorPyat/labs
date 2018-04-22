import java.awt.*;
// import java.awt.event.ActionEvent;
import static java.lang.Math.*;
import javax.swing.*;
import java.awt.event.*;

public class RotatingFigure extends JPanel {
  int N = 6;
  int nodesN = 4;
  int edgesN = nodesN - 1;
  double ox = 0, dx = 0;
  double oy = 0, dy = 0;
  double[][] startNodes = {{2, 0, -2}, {2 / Math.sqrt(2), 0, -1}, {2 / Math.sqrt(2), 0, 1}, {2, 0, 2}};
  double[][][] nodes;
  double[][][] nodesWorld;
  int[][] edges;
  double[][] perspecMatrix;
  public RotatingFigure(){
    setPreferredSize(new Dimension(787, 498));
    setBackground(Color.white);

    perspecMatrix = new double[4][4];

    double rads = Math.toRadians(90 / 2);
    double f = 1.0 / Math.tan(rads);
    double aspect = 1;
    double zNear = 1.;
    double zFar = 2.;
    perspecMatrix[0][0] = f / aspect;
    perspecMatrix[1][1] = f;
    perspecMatrix[2][2] = (+zNear + zFar) / (zNear - zFar);
    perspecMatrix[2][3] = (2 * zNear * zFar) / (zNear - zFar);
    perspecMatrix[3][2] = -1;

    nodes = new double[N][nodesN][3];
    nodes[0] = startNodes;
    edges = new int[edgesN][2];

    for(int i = 0; i < edgesN; i++){
      edges[i][0] = i;
      edges[i][1] = i + 1;
    }

    for(int i = 1; i < N; i++){
      double sina = sin(PI * i * (360. / N) / 180);
      double cosa = cos(PI * i * (360. / N) / 180);
      for(int j = 0; j < nodesN; j++){
        nodes[i][j][0] = startNodes[j][0] * cosa - startNodes[j][1] * sina;
        nodes[i][j][1] = startNodes[j][0] * sina + startNodes[j][1] * cosa;
        nodes[i][j][2] = startNodes[j][2];
      }
    }

    nodesWorld = new double[N][nodesN][3];

    for(int i = 0; i < N; i++){
      for(int j = 0; j < nodesN; j++){
        nodesWorld[i][j][0] = nodes[i][j][0];
        nodesWorld[i][j][1] = nodes[i][j][1];
        nodesWorld[i][j][2] = nodes[i][j][2];
      }
    }

    scale(100);
    rotateFigure(0, 0);

    addMouseListener(new MouseAdapter(){
      @Override
      public void mousePressed(MouseEvent e){
        dx = e.getX();
        dy = e.getY();
        ox = dx;
        oy = dy;
      }
    });

    addMouseMotionListener(new MouseMotionAdapter(){
      @Override
      public void mouseDragged(MouseEvent e){
        int x = e.getX();
        int y = e.getY();
        dx = x - ox;
        dy = y - oy;

        rotateFigure(PI * (dx) / 180, PI * (dy) / 180);
        ox = x;
        oy = y;
        repaint();
      }
    });
  }

  final void scale(double s) {
    for(double[][] edge : nodesWorld) {
      for(double[] node : edge) {
        node[0] *= s;
        node[1] *= s;
        node[2] *= s;
      }
    }
  }

  final void rotateFigure(double angleX, double angleY) {
    double sinX = sin(angleX);
    double cosX = cos(angleX);

    double sinY = sin(angleY);
    double cosY = cos(angleY);
    for(double[][] edge : nodes) {
      for(double[] node : edge) {
        double x = node[0];
        double y = node[1];
        double z = node[2];

        node[0] = x * cosX - z * sinX;
        node[2] = z * cosX + x * sinX;

        z = node[2];

        node[1] = y * cosY - z * sinY;
        node[2] = z * cosY + y * sinY;
      }
    }
    for(int i = 0; i < N; i++){
      for(int j = 0; j < nodesN; j++){
        nodesWorld[i][j][0] = nodes[i][j][0];
        nodesWorld[i][j][1] = nodes[i][j][1];
        nodesWorld[i][j][2] = nodes[i][j][2];
      }
    }
    for(int i = 0; i < N; i++){
      for(int j = 0; j < nodesN; j++){
        double x = nodes[i][j][0];
        double y = nodes[i][j][1];
        double z = nodes[i][j][2] + 5;
        nodesWorld[i][j][0] = x * perspecMatrix[0][0];
        nodesWorld[i][j][1] = y * perspecMatrix[1][1];
        nodesWorld[i][j][2] = z * perspecMatrix[2][2] + perspecMatrix[2][3];
        double w = z * perspecMatrix[3][2];
        nodesWorld[i][j][0] /= w;
        nodesWorld[i][j][1] /= w;
        nodesWorld[i][j][2] /= w;
      }
    }
    scale(100);

  }

  void drawFigure(Graphics2D g) {
    g.translate(getWidth() / 2, getHeight() / 2);

    for(int[] edge : edges) {
      double[] xy1 = nodesWorld[0][edge[0]];
      double[] xy2 = nodesWorld[0][edge[1]];
      g.drawLine((int) round(xy1[0]), (int) round(xy1[1]), (int) round(xy2[0]), (int) round(xy2[1]));
      for(int j = 0; j < nodesN; j++){
        xy1 = nodesWorld[N - 1][j];
        xy2 = nodesWorld[0][j];
        g.drawLine((int) round(xy1[0]), (int) round(xy1[1]), (int) round(xy2[0]), (int) round(xy2[1]));
      }
    }

    for(int[] edge : edges) {
      for(int i = 1; i < N; i++){
        double[] xy1 = nodesWorld[i][edge[0]];
        double[] xy2 = nodesWorld[i][edge[1]];
        g.drawLine((int) round(xy1[0]), (int) round(xy1[1]), (int) round(xy2[0]), (int) round(xy2[1]));
        for(int j = 0; j < nodesN; j++){
          xy1 = nodesWorld[i - 1][j];
          xy2 = nodesWorld[i]    [j];
          g.drawLine((int) round(xy1[0]), (int) round(xy1[1]), (int) round(xy2[0]), (int) round(xy2[1]));
        }
      }
    }

    for (double[][] edge : nodesWorld){
      for(double[] node : edge){
        g.fillOval((int) round(node[0]) - 4, (int) round(node[1]) - 4, 8, 8);
      }
    }
  }

  @Override
  public void paintComponent(Graphics gg) {
    super.paintComponent(gg);
    Graphics2D g = (Graphics2D) gg;
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    drawFigure(g);
  }

}
