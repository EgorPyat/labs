package ru.nsu.ccfit.pyataev.game.arkanoid;

import ru.nsu.ccfit.pyataev.game.arkanoid.controller.Space;
import ru.nsu.ccfit.pyataev.game.arkanoid.model.World;
import ru.nsu.ccfit.pyataev.game.arkanoid.view.Frame;
import ru.nsu.ccfit.pyataev.game.arkanoid.view.Panel;

public class Main{
  public static void main(String args[]) throws InterruptedException{
    World w = new World(args[0]);
    Space space = new Space();
    Panel panel = new Panel();
    panel.addWorld(w);
    Frame fr = new Frame(panel);
    space.addWorld(w);
    panel.addSpace(space);
    w.addPanel(panel);

    while(true){
      panel.change();
      panel.repaint();
      Thread.sleep(4);
    }
  }
}
