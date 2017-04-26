package ru.nsu.ccfit.pyataev.game.arkanoid.view;

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

public class Frame extends JFrame{
  private Panel panel;

  public Frame(Panel panel){
    super("Arkanoid 0.0.0.1 pre-alfa");
    this.panel = panel;
    this.setResizable(false);
    this.add(this.panel);
    this.setLocation(500, 300);
    this.setSize(300, 400);
    this.setVisible(true);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }
}
