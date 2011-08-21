package rl.world.object;

import rl.util.Chance;

public class Weapon extends Item {
  int dieCount, dieSize, damageMod;
  
  public Weapon(int x, int y, char chr, int fg, int bg, String name, int dieCount, int dieSize, int damageMod) {
    super(x, y, chr, fg, bg, name);
    this.dieCount = dieCount;
    this.dieSize = dieSize;
    this.damageMod = damageMod;
  }
  
  int getDamage() {
    return Chance.rollDie(dieCount, dieSize) + damageMod;
  }
  
  String longDescription() {
    return name + " (weapon), " + (dieCount+damageMod) + "-" + ((dieCount*dieSize)+damageMod) + " damage";
  }
  
  public boolean is(String what) {
    return what.equals("Weapon");
  }
}
