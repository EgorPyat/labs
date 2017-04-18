package ru.nsu.ccfit.pyataev.game;

import ru.nsu.ccfit.pyataev.game.view.GameFrame;
import ru.nsu.ccfit.pyataev.game.view.PlayPanel;
import ru.nsu.ccfit.pyataev.game.view.StatsPanel;

public class Arkanoid{
  public static void main(String[] args){
    PlayPanel play = new PlayPanel();
    StatsPanel stats = new StatsPanel();
    GameFrame frame = new GameFrame(play, stats);
    while(true){
      HashSet<Integer> currentKeys = KeyboardController.getActiveKeys();
      if(currentKeys.contains(KeyEvent.VK_RIGHT)){
			play.move(KeyEvent.VK_RIGHT);
		}
		else if(currentKeys.contains(KeyEvent.VK_LEFT)){
			plat.move(KeyEvent.VK_LEFT);
		}
      play.repaintGame();
    }

  }
}
