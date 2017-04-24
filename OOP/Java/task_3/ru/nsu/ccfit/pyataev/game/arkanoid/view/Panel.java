package ru.nsu.ccfit.pyataev.game.arkanoid.view;

import ru.nsu.ccfit.pyataev.game.arkanoid.model.Ball;
import ru.nsu.ccfit.pyataev.game.arkanoid.model.Brick;
import ru.nsu.ccfit.pyataev.game.arkanoid.model.Racquet;
import ru.nsu.ccfit.pyataev.game.arkanoid.controller.Space;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Line2D;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Panel extends JPanel{
  private Space space;

  public Panel(){
    this.addHandler();
    this.setFocusable(true);
  }

  public void addHandler(){
    this.addKeyListener(new KeyListener(){
			@Override
			public void keyTyped(KeyEvent e){}

			@Override
			public void keyReleased(KeyEvent e){
				space.keyReleased(e);
			}

			@Override
			public void keyPressed(KeyEvent e){
				space.keyPressed(e);
      }
		});
  }

  public void addSpace(Space space){
    this.space = space;
  }

  @Override
	public void paint(Graphics g){
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    space.paint(g2d);

		g2d.setColor(Color.GRAY);
		g2d.setFont(new Font("Verdana", Font.BOLD, 30));
		g2d.drawString(String.valueOf(space.getScore()), 10, 30);
		g2d.draw(new Line2D.Double(0, 330, 300, 330));
		g2d.draw(new Line2D.Double(0, 340, 300, 340));
	}

  public void change(){
    if(space.checkSave() == true) {
      space.change();
      this.gameWin();
    }
    if(space.checkEnd() == true){
      space.change();
      this.gameOver();
    }
    space.change();
  }

  public void gameOver(){
		JOptionPane.showMessageDialog(this, "Your score is: " + space.getScore(), "Game Over", JOptionPane.YES_NO_OPTION);
		System.exit(ABORT);
	}

	public void gameWin(){
		JOptionPane.showMessageDialog(this, "Your score is: " + space.getScore(), "You win!!!", JOptionPane.YES_NO_OPTION);
		System.exit(ABORT);
	}
}
