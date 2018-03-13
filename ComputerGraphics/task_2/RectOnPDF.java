import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class RectOnPDF extends JPanel
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
        private Point start = new Point();
    private Point end   = new Point();
    Rectangle rect = new Rectangle();
    Icon icon = new ImageIcon("p.jpeg");
    JLabel label = new JLabel(icon);
    public RectOnPDF(){
     this.add(label);
     label.addMouseMotionListener( new MouseMotionAdapter(){
            public void mouseDragged(MouseEvent ev)
            {
                end = ev.getPoint();
                System.out.println(end);
                //rect.setFrameFromDiagonal(start, end);
                repaint();
                }//end mouse drag
            });
           label.addMouseListener(new MouseAdapter()
        {
            public void mousePressed(MouseEvent e)
            {
                start = e.getPoint();
                System.out.println(start);
                }
            public void mouseReleased(MouseEvent ev)
            {
                /*points[pointCount] = start;
                points2[pointCount] = ev.getPoint();
                pointCount++;  */
                rect.setFrameFromDiagonal(start, start);
                repaint();
                }
            });
        }
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 =(Graphics2D) label.getGraphics();
        //Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        // Draw line being dragged.
        g2.setPaint(Color.red);
        g2.draw(rect);
        // Draw lines between points in arrays.
        //g2.setPaint(Color.blue);
        Rectangle rs = new Rectangle();
        rs.setFrameFromDiagonal(start,end);
        g2.draw(rs);


        }
    public static void main(String[] args)
    {
        RectOnPDF test = new RectOnPDF();
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(test);

        f.setSize(841,595);
        //f.setLocation(200,200);
        f.setVisible(true);


        }




    }
