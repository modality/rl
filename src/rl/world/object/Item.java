package rl.world.object;

import java.util.HashMap;

import rl.util.Colors;

public class Item extends GameObject {

  public Item() {
    super();
  }

  public Item(int x, int y, char chr, int fg, int bg, String name) {
    super(x, y, chr, fg, bg);
    this.name = name;
  }

  public Item(int x, int y, String name) {    
    super(x, y, '*', Colors.RED, Colors.BLACK);
    this.name = name;
  }
  
  public String describe() {
    return "There is a "+name+" here.";
  }
  
  public boolean is(String what) {
    return what.equals("Item");
  }
  
  public HashMap toHashMap() {
    HashMap hm = super.toHashMap();
    hm.put("type", "Item");
    return hm;
  }
}