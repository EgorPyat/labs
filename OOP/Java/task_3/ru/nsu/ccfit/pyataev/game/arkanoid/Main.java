package ru.nsu.ccfit.pyataev.game.arkanoid;

import ru.nsu.ccfit.pyataev.game.arkanoid.controller.Space;
import ru.nsu.ccfit.pyataev.game.arkanoid.model.World;
import ru.nsu.ccfit.pyataev.game.arkanoid.view.Frame;
import ru.nsu.ccfit.pyataev.game.arkanoid.view.Panel;

public class Main{
  public static void main(String args[]) throws InterruptedException{
    World w = new World();
    Space space = new Space();
    Panel panel = new Panel();
    Frame fr = new Frame(panel);
    space.addWorld(w);
    panel.addSpace(space);

    while(true){
      panel.change();
      panel.repaint();
      Thread.sleep(5);
    }
  }
}
