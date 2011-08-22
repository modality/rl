package rl.world.object;

import rl.util.Chance;

public class Weapon extends Item {
  int dieCount, dieSize, bonusMod;
  
  public Weapon(int x, int y, char chr, int fg, int bg, String name, int dieCount, int dieSize, int bonusMod) {
    super(x, y, chr, fg, bg, name);
    this.dieCount = dieCount;
    this.dieSize = dieSize;
    this.bonusMod = bonusMod;
  }
  
  int getDamage() {
    return Chance.rollDie(dieCount, dieSize) + bonusMod;
  }

  String longDescription() {
    String longDesc = "";

    if(bonusMod > 0) {
      longDesc += "+" + bonusMod;
    }
    longDesc += name + " (weapon), " + (dieCount+bonusMod) + "-" + ((dieCount*dieSize)+bonusMod) + " damage";
    return longDesc;
  }

  public boolean is(String what) {
    return what.equals("Weapon");
  }
}
