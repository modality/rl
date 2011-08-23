package rl.world.object;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import rl.world.map.GameMap;
import rl.util.Chance;
import rl.util.Scheduler;

public abstract class Actor extends GameObject {
  public ArrayList<GameObject> inventory;
  public ActorStats stats;
  public GameMap gmap;

  public Actor() {
    super();
  }

  public Actor(int x, int y, char chr, int fg, int bg, String name) {
    super(x, y, chr, fg, bg);
    this.passable = false;
    this.name = name;

    inventory = new ArrayList();
    stats = new ActorStats();
    stats.resHP = 10;
    stats.attrSTR = 1;
    stats.attrDEX = 10;
  }

  public String describe() {
    return this.name+" is here";
  }
  
  public boolean canSee(Actor a) {
    return gmap.visiblity(this.getPosition(), a.getPosition());
  }

  public abstract void scheduleMove(Scheduler s);
  public abstract void takeDamage(int amount, Actor source);

  public float turnDelay() {
    return 100.0f * (float) Math.pow(0.9f, (stats.attrDEX-10));
  }
  
  public void attack(Actor target) {
    gmap.textEvent(name+" attacks "+target.name+"!");
    int dieRoll = Chance.rollDie(1, 20);

    if(dieRoll == 20) {
      int damage = stats.getDamage() * 2;
      target.takeDamage(damage, this);
    } else if (dieRoll + stats.getAttackRating() > target.stats.getDefenseRating()) {
      int damage = stats.getDamage();
	    target.takeDamage(damage, this);
	  } else {
	    gmap.textEvent(name+" misses "+target.name+"!");
	  }
	}

  public ArrayList<GameObject> getInventory(String type) {
    ArrayList<GameObject> typedItems = new ArrayList<GameObject>();
    Iterator<GameObject> itr = inventory.iterator();
    while(itr.hasNext()) {
      GameObject go = itr.next();
      if(go.is(type)) {
        typedItems.add(go);
      }
    }
    return typedItems;
  }

  public boolean is(String what) {
    return what.equals("Actor");
  }
  
  public HashMap toHashMap() {
    HashMap hm = super.toHashMap();
    hm.put("type", "Actor");
    return hm;
  }
}
