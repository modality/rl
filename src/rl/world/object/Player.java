package rl.world.object;


import java.util.HashMap;

import rl.util.Colors;
import rl.world.events.PlayerEvent;
import rl.util.Scheduler;


public class Player extends Actor {

  public Player() {
    super();
  }

  public Player(int x, int y, char character, int fg, int bg, String name) {
    super(x, y, character, fg, bg, name);
    this.passable = true;
    this.name = name;
    stats.attrSTR = 2;
  }

  public void takeDamage(int amount, Actor source) {
    gmap.textEvent(name+" takes "+amount+" damage!");
    stats.resHP -= amount;

    if(stats.resHP <= 0) {
      die();
    }
  }

  public void die() {
    updated = true;
    passable = true;
    fg = Colors.GRAY;
    bg = Colors.RED;
    has_bg = true;
    System.out.println(name+" has died.");
  }

  public void scheduleMove(Scheduler s) {
    PlayerEvent pe = new PlayerEvent();
    float delay = turnDelay();
    s.scheduleEvent(pe, delay);
  }

  public boolean is(String what) {
    return what.equals("Player");
  }
  
  public HashMap toHashMap() {
    HashMap hm = super.toHashMap();
    hm.put("type", "Player");
    return hm;
  }
}