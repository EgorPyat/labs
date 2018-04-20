import java.awt.*;
// import java.awt.event.ActionEvent;
import static java.lang.Math.*;
import javax.swing.*;
import java.awt.event.*;

public class RotatingCube extends JPanel {
    double ox = 0;
    double oy = 0;
    double[][] nodes = {{0, -0.25, 1}, {0, -0.25, -1}, {0, -0.6, 0}/*{1, 0, 0}, {0, 1, 0}, {0, 0, 1}, {0, 0, 0}*//*, {-1, 1, -1}, {-1, 1, 1},
  {1, -1, -1}, {1, -1, 1}, {1, 1, -1}, {1, 1, 1}*/};

    int[][] edges = {{0, 2}, {2, 1}/*{0, 3}, {1, 3}, {2, 3}*//*, {1, 3}, {3, 2}, {2, 0}, {4, 5}, {5, 7}, {7, 6},
  {6, 4}, {0, 4}, {1, 5}, {2, 6}, {3, 7}*/};

    public RotatingCube() {
        setPreferredSize(new Dimension(640, 640));
        setBackground(Color.white);

        scale(100);
        rotateCube(0, 0);

        addMouseMotionListener(new MouseMotionAdapter(){
          @Override
          public void mouseDragged(MouseEvent e){
            int x = e.getX();
            int y = e.getY();

            rotateCube(PI * (x - ox) / 180, PI * (y - oy) / 180);
            ox = x;
            oy = y;
            repaint();
          }
        });
        // new Timer(17, (ActionEvent e) -> {
        //     rotateCube(PI / 180, PI / 60);
        //     repaint();
        // }).start();
    }

    final void scale(double s) {
        for (double[] node : nodes) {
            node[0] *= s;
            node[1] *= s;
            node[2] *= s;
        }
    }

    final void rotateCube(double angleX, double angleY) {
        double sinX = sin(angleX);
        double cosX = cos(angleX);

        double sinY = sin(angleY);
        double cosY = cos(angleY);

        for (double[] node : nodes) {
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

    void drawCube(Graphics2D g) {
        g.translate(getWidth() / 2, getHeight() / 2);
        Color[] cs = new Color[3];
        cs[0] = new Color(255, 0, 0);
        cs[1] = new Color(0, 255, 0);
        cs[2] = new Color(0, 0, 255);
        int i = 0;
        for (int[] edge : edges) {
            // g.setColor(cs[i++]);
            double[] xy1 = nodes[edge[0]];
            double[] xy2 = nodes[edge[1]];
            g.drawLine((int) round(xy1[0]), (int) round(xy1[1]),
                    (int) round(xy2[0]), (int) round(xy2[1]));
        }
        g.setColor(Color.BLACK);
        for (double[] node : nodes)
            g.fillOval((int) round(node[0]) - 4, (int) round(node[1]) - 4, 8, 8);
    }

    @Override
    public void paintComponent(Graphics gg) {
        super.paintComponent(gg);
        Graphics2D g = (Graphics2D) gg;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        drawCube(g);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame();
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setTitle("Rotating Cube");
            f.setResizable(false);
            f.add(new RotatingCube(), BorderLayout.CENTER);
            f.pack();
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });
    }
}
