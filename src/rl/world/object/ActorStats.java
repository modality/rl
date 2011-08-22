package rl.world.object;

import java.util.HashMap;

import rl.util.Chance;

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

  public int getBonus(int attr) {
    return (attr-10) / 2;
  }

  public int getDamage() {
    int damage = getBonus(attrSTR);

    Item weapon = equipItems.get("Weapon");
    if(weapon != emptyItem) {
      damage += ((Weapon) weapon).getDamage();
    } else {
      damage += Chance.rollDie(1, 2);
    }

    return damage;
  }

  public int getAttackRating() {
    int AR = getBonus(attrSTR);

    Item weapon = equipItems.get("Weapon");
    if(weapon != emptyItem) {
      AR += ((Weapon) weapon).bonusMod;
    }
    return AR;
  }

  public int getDefenseRating() {
    int DR = 10 + getBonus(attrDEX);
    return DR;
  }
}
