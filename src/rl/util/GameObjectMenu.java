package rl.util;

import java.util.ArrayList;
import rl.world.object.GameObject;

public class GameObjectMenu extends GameMenu {
  ArrayList<GameObject> gameObjects;
  
  public GameObjectMenu(ArrayList<GameObject> gobjs) {
    super();
    this.gameObjects = gobjs;
    currentPage = 1;
    totalPages = (int) (gobjs.size() / PER_PAGE) + 1;
    if(totalPages > 0) {
      showPagination = true;
    }
  }
  
  public String display() {
    String menuDisplay = "";
    
    menuDisplay += listPage(currentPage, gameObjects);
    
    if(showPagination) {
      menuDisplay += displayPagination();
    }
    return menuDisplay;
  }
  
  public Object handleInput(int key, int keyCode) {
    System.out.println(key);
    if (key >= 'a' && key <= 'j') {
      int itemIndex = (key - 'a') + ((currentPage-1) * PER_PAGE);
      if(itemIndex < gameObjects.size()) {
        GameObject go = gameObjects.get(itemIndex);
        messageQueue = "Picked up a "+go;
        return go;
      }
    }
    return super.handleInput(key, keyCode);
  }
}