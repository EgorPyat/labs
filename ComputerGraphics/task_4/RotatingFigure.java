import java.awt.*;
// import java.awt.event.ActionEvent;
import static java.lang.Math.*;
import javax.swing.*;
import java.awt.event.*;

public class RotatingFigure extends JPanel {
  int N = 6;
  int nodesN = 3;
  int edgesN = nodesN - 1;
  double ox = 0, dx = 0;
  double oy = 0, dy = 0;
  double[][] startNodes = {{0, -0.25, 1}, {0, -0.6, 0}, {0, -0.25, -1}};
  double[][][] nodes;
  int[][] edges;

  public RotatingFigure(){
    setPreferredSize(new Dimension(640, 640));
    setBackground(Color.white);

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
    for(double[][] edge : nodes) {
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
  }

  void drawFigure(Graphics2D g) {
    g.translate(getWidth() / 2, getHeight() / 2);

    for(int[] edge : edges) {
      double[] xy1 = nodes[0][edge[0]];
      double[] xy2 = nodes[0][edge[1]];
      g.drawLine((int) round(xy1[0]), (int) round(xy1[1]), (int) round(xy2[0]), (int) round(xy2[1]));
      for(int j = 0; j < nodesN; j++){
        xy1 = nodes[N - 1][j];
        xy2 = nodes[0][j];
        g.drawLine((int) round(xy1[0]), (int) round(xy1[1]), (int) round(xy2[0]), (int) round(xy2[1]));
      }
    }

    for(int[] edge : edges) {
      for(int i = 1; i < N; i++){
        double[] xy1 = nodes[i][edge[0]];
        double[] xy2 = nodes[i][edge[1]];
        g.drawLine((int) round(xy1[0]), (int) round(xy1[1]), (int) round(xy2[0]), (int) round(xy2[1]));
        for(int j = 0; j < nodesN; j++){
          xy1 = nodes[i - 1][j];
          xy2 = nodes[i]    [j];
          g.drawLine((int) round(xy1[0]), (int) round(xy1[1]), (int) round(xy2[0]), (int) round(xy2[1]));
        }
      }
    }

    for (double[][] edge : nodes){
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

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      JFrame f = new JFrame();
      f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      f.setTitle("Rotating Figure");
      f.setResizable(false);
      f.add(new RotatingFigure(), BorderLayout.CENTER);
      f.pack();
      f.setLocationRelativeTo(null);
      f.setVisible(true);
    });
  }
}
