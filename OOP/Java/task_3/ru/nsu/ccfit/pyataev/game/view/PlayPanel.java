package ru.nsu.ccfit.pyataev.game.view;

import ru.nsu.ccfit.pyataev.game.model.Platform;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class PlayPanel extends JPanel{
  public PlayPanel(){
    this.plat = new Platform();
    this.setSize(1024, 680);
    this.setBackground(Color.lightGray);
    this.setLayout(null);
  }

  @Override
  protected void paintComponent(Graphics g){
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D)g;
    g2.drawImage(plat.getFrame(), plat.getCurrentX(), 600, null);
  }

  public void repaintGame(){
		this.repaint();
	}

  private Platform plat;
}
