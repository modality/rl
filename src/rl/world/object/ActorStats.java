package rl.world.object;

import java.util.HashMap;

public class ActorStats {
  // resource stats (hp, mana, stamina, etc.)
  public int resHP;

  // attributes
  public int attrSTR, attrDEX, attrCON, attrINT, attrPSY, attrCOM;
  
  // Weapon, Armor
  public HashMap<String, Item> equipItems;
  static Item emptyItem = new Item(0, 0, "empty");

  public ActorStats() { 
    equipItems = new HashMap<String, Item>();
    equip("Weapon", emptyItem);
    equip("Armor", emptyItem);
  }
  
  public void equip(String slot, Item item) {
    equipItems.put(slot, item);
  }
}
