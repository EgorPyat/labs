package ru.nsu.ccfit.pyataev.game.view;

import javax.swing.JFrame;
import ru.nsu.ccfit.pyataev.game.controller.KeyboardController;

public class GameFrame extends JFrame{
  public GameFrame(PlayPanel play, StatsPanel stats){
    super("Arkanoid 0.0.0");
    this.setLocation(0, 0);
    this.setSize(1024, 720);
    this.setVisible(true);
    this.setLayout(null);
    this.setResizable(false);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.add(stats);
    stats.setLocation(0, 0);
    this.add(play);
    play.setLocation(0, 40);

    keyboard = new KeyboardController();
    this.addKeyListener(keyboard);
  }
  
  private KeyboardController keyboard;
}
