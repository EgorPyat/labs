/* DrawingArea.java
 * Class for drawing shapes
 * Created on February 4, 2004, 12:03 PM
 */

/**
 *
 * Randall Twede
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.ArrayDeque;

public class DrawingArea extends JPanel
{
   private MyMouseListener theListener = new MyMouseListener();
   private Point start = new Point();
   private Point end = new Point();
   private Paint parent;    // Reference to the JFrame containing this Panel
   private Shape current;
   private int x1 = 0;
   private int x2 = 0;
   private int y1 = 0;
   private int y2 = 0;
   private int w = 0;
   private int h = 0;
   private int stroke = 1;
   private float alpha = 1f;
   private JWindow size = new JWindow();
   private JLabel coordinates = new JLabel();
   private boolean done = true;
   private BufferedImage free;  //for free hand drawing
   private BufferedImage overlayImage;  //for composite images

    /** Creates a new instance of DrawingArea */
    public DrawingArea(Paint p)
    {
        parent = p;
        addMouseListener(theListener);
        addMouseMotionListener(theListener);
        size.setSize(50, 20);
        size.setAlwaysOnTop(true);
        Container c = size.getContentPane();
        c.add(coordinates);
    }

    public Dimension getPreferredSize()
    {
        BufferedImage i = ImageStack.getImage();
        Dimension d = super.getPreferredSize();
        if(i != null)
        {
            d = new Dimension(i.getWidth(), i.getHeight());
        }
        return d;
    }

    public void paintComponent(Graphics g)
    {
        Shape shape;
        Graphics2D g2d = (Graphics2D)g;
        super.paintComponent(g);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        setBackground(parent.getBack());    // Set the background color
        if(free != null)
        {
            g2d.drawImage(free, 0, 0, null);
        }
        else if(ImageStack.getImage() != null)  // Draws the image if there is one
        {
            g2d.drawImage(ImageStack.getImage(), 0, 0, null);
        }
        if(overlayImage != null)
        {
            g2d.drawImage(overlayImage, (int)end.getX(), (int)end.getY(), null);
        }
        if(parent.getCrop() && !done)
        {
            g2d.setColor(Color.BLACK);
            g2d.drawRect(x1, y1, w, h);
            g2d.setColor(Color.WHITE);
            float[] dash = {5.0f, 5.0f};
            BasicStroke dashed = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 10f, dash, 0.0f);
            g2d.setStroke(dashed);
            g2d.drawRect(x1, y1, w, h);
        }
        // Draws the current Shape
        if(current != null)
        {
            current.draw(g);
        }
        g2d.dispose();
    }

    // Sets the width of the lines
    public void setStroke(int stroke)
    {
        this.stroke = stroke;
    }

    // Sets the alpha of the image
    public void setAlpha(float alpha)
    {
        this.alpha = alpha;
        repaint();
    }

    public BufferedImage createImage()
    {
        BufferedImage temp;
        Dimension d = getSize();
        Rectangle region = new Rectangle(0, 0, d.width, d.height);
        BufferedImage last = ImageStack.getImage();
        if(last != null)
        {
            temp = new BufferedImage(region.width, region.height, last.getType());
        }
        else
        {
            temp = new BufferedImage(region.width, region.height, BufferedImage.TYPE_INT_RGB);
        }
        Graphics2D g2d = temp.createGraphics();
        paint( g2d );
        g2d.dispose();
        return temp;
    }

    public void setOverlayImage(BufferedImage overlayImage)
    {
        this.overlayImage = overlayImage;
    }

    // Determines top left coordinates, width, and height
    private void setParameters()
    {
        x1 = start.x;
        y1 = start.y;
        x2 = end.x;
        y2 = end.y;
        int temp = 0;
        // Swap if necessary
        if (x1 > x2)
        {
            temp = x1;
            x1 = x2;
            x2 = temp;
        }
        // Swap if necessary
        if (y1 > y2)
        {
            temp = y1;
            y1 = y2;
            y2 = temp;
        }
        w = x2 - x1;
        h = y2 - y1;
    }

    private void setCursorLabel()
    {
        Point p = MouseInfo.getPointerInfo().getLocation();
        int x = (int) p.getX();
        int y = (int) p.getY();
        size.setLocation(x, y - 20);
        coordinates.setText((w - 2) + ", " + (h - 2 ));
    }

    class MyMouseListener extends MouseAdapter
    {
        BufferedImage in;

        public void mouseMoved(java.awt.event.MouseEvent mouseEvent)
        {
            end = mouseEvent.getPoint();        // Gets the ending point
            setParameters();            // Calls a private method
            if(overlayImage != null)
            {
                setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
                repaint();
            }
        }

        public void mousePressed(java.awt.event.MouseEvent mouseEvent)
        {
            if(parent.getTransparent() || parent.getFloodFill() || parent.getReplace()) //only do something on mouseReleased()
            {
            }
            else
            {
                done = false;
                start = mouseEvent.getPoint();
                if(parent.getCrop())
                {
                    setCursorLabel();
                    size.setVisible(true);
                }
            }
        }

        public void mouseDragged(java.awt.event.MouseEvent mouseEvent)
        {
            end = mouseEvent.getPoint();        // Gets the ending point
            setParameters();            // Calls a private method
            if(parent.getTransparent() || parent.getFloodFill() || parent.getReplace()) //only do something on mouseReleased()
            {
            }
            else if(parent.getCrop())
            {
                setCursorLabel();
                repaint();
            }
            else
            {
                JRadioButton shape = parent.getSelected();  // Retrieves a referenced to the selected JRadioButton
                String type = shape.getText();          // Sets the type of shape to draw
                Color color = parent.getFore();     // Sets the drawing color
                if(type.equals("Draw Free"))
                {
                    current = new Shape(start.x, start.y, end.x, end.y, type, color, stroke);
                    repaint();
                    free = createImage();
                    start = end;
                }
                else if(type.equals("Draw Line"))
                {
                    current = new Shape(start.x, start.y, end.x, end.y, type, color, stroke);
                }
                else
                {
                    current = new Shape(x1, y1, w, h, type, color, stroke);
                }
                repaint();
            }
        }

        public void mouseReleased(java.awt.event.MouseEvent mouseEvent)
        {
            end = mouseEvent.getPoint();
            in = ImageStack.getImage();
            BufferedImage i = createImage();

            if(parent.getFloodFill())
            {
                floodFill();
            }
            else if(parent.getReplace())
            {
                replace();
            }
            else if(parent.getTransparent())
            {
                if(end.x < in.getWidth() && end.y < in.getHeight())   //the user clicked on the image
                {
                    Color color = new Color(in.getRGB(end.x, end.y));   //get the color
                    BufferedImage out = new BufferedImage(in.getWidth(), in.getHeight(),BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g2d = out.createGraphics();
                    g2d.drawImage(TransparencyUtil.makeColorTransparent(in, color), 0, 0, null);
                    g2d.dispose();
                    ImageStack.addImage(out);
                    parent.setTransparent(false);
                }
            }
            else if(parent.getCrop())
            {
                size.setVisible(false);
                //crop image
                i = i.getSubimage(x1 + 1, y1 + 1, w - 1, h - 1);
                //pop up a dialog
                int choice = JOptionPane.showConfirmDialog(parent,"Width = " + (w - 2) + "\nHeight = " + (h - 2), "Crop", JOptionPane.OK_CANCEL_OPTION);
                if(choice == JOptionPane.OK_OPTION)
                {
                    ImageStack.addImage(i);
                }
                parent.setCrop(false);
            }
            else
            {
                BufferedImage old = ImageStack.getImage();
                if(parent.getConstrain() && old != null)
                {
                    i = i.getSubimage(0, 0, old.getWidth(), old.getHeight());
                }
                ImageStack.addImage(i);
                current = null;
            }
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            repaint();
            revalidate();
            free = null;    //probably very important. should maybe come before repaint()
            overlayImage = null;
            done = true;
        }
        private void floodFill()
        {
            Point p;
            ArrayDeque<Point> q = new ArrayDeque<Point>();
            BufferedImage out = new BufferedImage(in.getWidth(), in.getHeight(), in.getType());
            Graphics2D g2d = out.createGraphics();
            g2d.drawImage(in, 0, 0, null);
            g2d.dispose();
            if(end.x < out.getWidth() && end.y < out.getHeight()) //the user clicked on the image
            {
                setCursor(new Cursor(Cursor.WAIT_CURSOR));
                Color color = new Color(in.getRGB(end.x, end.y));   //get the color
                if(!color.equals(parent.getFore()))
                {
                    q.add(end);
                }
                while(!q.isEmpty())
                {
                    p = q.remove();
                    out.setRGB(p.x, p.y, parent.getFore().getRGB());
                    for(int x = p.x - 1; x < p.x + 2; x++)
                    {
                        for(int y = p.y - 1; y < p.y + 2; y++)
                        {
                            if(x == p.x || y == p.y)    //no catty corner
                            {
                                if(x > -1 && y > -1 && x < out.getWidth() && y < out.getHeight())   //part image
                                {
                                    Point candidate = new Point(x, y);
                                    Color c = new Color(out.getRGB(candidate.x, candidate.y));
                                    if(!c.equals(parent.getFore())) //not the target color
                                    {
                                        if(!q.contains(candidate))  //no duplicates
                                        {
                                            q.add(candidate);
                                        }   //end if
                                    }   //end if
                                }   //end if
                            }   //end if
                        }   //end for
                    }   //end for
                }   //end while
            }   //end if
            ImageStack.addImage(out);
            parent.setFloodFill(false);
        }   //end floodFill()

        private void replace()
        {
            Point point = null;
            Color color = null;
            BufferedImage out = new BufferedImage(in.getWidth(), in.getHeight(), in.getType());
            Graphics2D g2d = out.createGraphics();
            g2d.drawImage(in, 0, 0, null);
            g2d.dispose();
            if(end.x < out.getWidth() && end.y < out.getHeight()) //the user clicked on the image
            {
                point = new Point(end.x, end.y);    //get the point
                color = new Color(in.getRGB(end.x, end.y)); //get the color
                for(int w = 0; w < out.getWidth(); w++)
                {
                    for(int h = 0; h < out.getHeight(); h++)
                    {
                        point.setLocation(w, h);
                        Color candidate = new Color(in.getRGB(point.x, point.y));
                        if(candidate.equals(color))
                        {
                            out.setRGB(point.x, point.y, parent.getFore().getRGB());
                        }
                    }
                }
                ImageStack.addImage(out);
                parent.setReplace(false);
            }
        }   //end replace()
    }   // end class MyMouseListener
}   // end class DrawingArea
