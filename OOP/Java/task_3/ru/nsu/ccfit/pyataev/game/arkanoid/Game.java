package ru.nsu.ccfit.pyataev.game.arkanoid;

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

@SuppressWarnings("serial")
public class Game extends JPanel{

	Ball ball = new Ball(this);
	Racquet racquet = new Racquet(this);

	int bn = 3;
	int lbn = 3;

	Brick bricks[] = new Brick[bn];
	{
		for(int i = 0; i < bn; i++){
			bricks[i] = new Brick(this, 50 + i * 80, 20);
		}
	}

	int speed = 1;

	private int getScore(){
		return speed - 1;
	}

	public Game(){
		addKeyListener(new KeyListener(){
			@Override
			public void keyTyped(KeyEvent e){
			}

			@Override
			public void keyReleased(KeyEvent e){
				racquet.keyReleased(e);
			}

			@Override
			public void keyPressed(KeyEvent e){
				racquet.keyPressed(e);
			}
		});
		setFocusable(true);
	}

	private void move(){
		ball.move();
		racquet.move();
	}

	@Override
	public void paint(Graphics g){
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		ball.paint(g2d);
		racquet.paint(g2d);

		for(int i = 0; i < bn; i++){
			bricks[i].paint(g2d);
		}

		g2d.setColor(Color.GRAY);
		g2d.setFont(new Font("Verdana", Font.BOLD, 30));
		g2d.drawString(String.valueOf(getScore()), 10, 30);
		g2d.draw(new Line2D.Double(0, 330, 300, 330));
		g2d.draw(new Line2D.Double(0, 340, 300, 340));
	}

	public void gameOver(){
		JOptionPane.showMessageDialog(this, "Your score is: " + getScore(), "Game Over", JOptionPane.YES_NO_OPTION);
		System.exit(ABORT);
	}

	public void gameWin(){
		JOptionPane.showMessageDialog(this, "Your score is: " + getScore(), "You win!!!", JOptionPane.YES_NO_OPTION);
		System.exit(ABORT);
	}

	public static void main(String[] args) throws InterruptedException{
		JFrame frame = new JFrame("Arkanoid 0.0.1 pre-alfa");
		Game game = new Game();
		frame.setResizable(false);
		frame.add(game);
		frame.setLocation(500, 300);
		frame.setSize(300, 400);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		while (true){
			game.move();
			game.repaint();
			if(game.lbn == 0) game.gameWin();
			Thread.sleep(5);
		}
	}
}
