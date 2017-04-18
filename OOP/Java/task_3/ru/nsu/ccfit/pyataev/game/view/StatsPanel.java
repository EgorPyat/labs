package ru.nsu.ccfit.pyataev.game.view;

import javax.swing.JPanel;
import java.awt.Color;

public class StatsPanel extends JPanel{
  public StatsPanel(){
    this.setSize(1024, 40);
    this.setBackground(Color.darkGray);
  }

  private int score = 0;
}
