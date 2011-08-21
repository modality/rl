package rl.world.object;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import rl.world.map.GameMap;
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

  public abstract void scheduleMove(Scheduler s);
  public abstract void attack(Actor target);
  public abstract void takeDamage(int amount, Actor source);

  public float turnDelay() {
    return 100.0f * (float) Math.pow(0.9f, (stats.attrDEX-10));
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