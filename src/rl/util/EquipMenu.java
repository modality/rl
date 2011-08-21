package rl.util;

import java.util.ArrayList;
import java.util.Iterator;

import rl.world.object.Item;
import rl.world.object.Player;

public class EquipMenu extends GameMenu {
  static int MODE_SLOT = 1, MODE_ITEM = 2;
  int mode;
  Player player;
  ArrayList<String> slots;
  String currentSlot;
  
  
  public EquipMenu(Player p) {
    super();
    this.player = p;
    mode = MODE_SLOT;
    slots = new ArrayList<String>();
    
    Iterator<String> keys = player.stats.equipItems.keySet().iterator();
    while(keys.hasNext()) {
      slots.add(keys.next());
    }
    totalPages = (int) (slots.size() / PER_PAGE) + 1;
    if(totalPages > 0) {
      showPagination = true;
    }
  }
  
  public String display() {
    String menuDisplay = "";

    if(mode == MODE_SLOT) {
      menuDisplay += listPage(currentPage, slots);
      menuDisplay += "(a-j) Choose equipment slot";
    } else if(mode == MODE_ITEM) {
      menuDisplay += listPage(currentPage, player.getInventory(currentSlot));
      menuDisplay += "(a-j) Equip item to " + currentSlot + " slot";
    }
    
    if(showPagination) {
      menuDisplay += displayPagination();
    }
    
    return menuDisplay;
  }
  
  public Object handleInput(int key, int keyCode) {
    System.out.println(key);
      
    if (key >= 'a' && key <= 'j') {
      int itemIndex = (key - 'a') + ((currentPage-1) * PER_PAGE);
      if(mode == MODE_SLOT) {
        if(itemIndex < slots.size()) {
          currentSlot = slots.get(itemIndex);
          mode = MODE_ITEM;
        }
      } else if(mode == MODE_ITEM) {
        if(itemIndex < player.getInventory(currentSlot).size()) {
          Item selectedItem = (Item) player.getInventory(currentSlot).get(itemIndex);
          messageQueue = "Equipped " + selectedItem + " to " + currentSlot + " slot.";
          player.stats.equip(currentSlot, selectedItem);
          mode = MODE_SLOT;
        }
      }
    }
    return super.handleInput(key, keyCode);
  }
}
