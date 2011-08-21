package rl.world.object;

import java.util.HashMap;
import java.util.UUID;

import org.json.JSONObject;

import processing.core.PApplet;
import processing.core.PImage;

import rl.util.BFont;
import rl.util.Point;
import rl.util.SaveableObject;

public abstract class GameObject implements SaveableObject {
	  public PImage img;
	  public int x, y;
	  public char chr;
	  public int fg, bg;
	  public boolean passable, has_bg, updated;
	  public String name, state;
	  public UUID id;

	  public GameObject() {}
	  
	  public GameObject(int x, int y, char chr, int fg, int bg) {
	    this.x = x;
	    this.y = y;
	    this.chr = chr;
	    this.fg = fg;
	    this.bg = bg;
	    this.id = UUID.randomUUID();

	    has_bg = false;
	    updated = true;
	    passable = true;
	    name = "generic object";
	    state = "";
	  }

	  public void renderImage(BFont font) {
	    if(has_bg) {
	      img = font.get(PApplet.parseInt(chr), fg, bg);
	    } else {
	      img = font.get(PApplet.parseInt(chr), fg);
	    }
	    updated = false;
	  }
	  
	  public abstract String describe();
	  public abstract boolean is(String what);

	  public HashMap toHashMap() {
	    HashMap hm = new HashMap();
	    hm.put("name", name);
	    hm.put("type", "GameObject");
	    hm.put("chr", (int) chr);
	    hm.put("fg", fg);
	    hm.put("bg", bg);
	    hm.put("x", x);
	    hm.put("y", y);
	    return hm;
	  }

	  public Point getPosition() {
	    return new Point(x, y);
	  }

	  public boolean nextTo(GameObject go) {
	    return Math.abs(go.x - x) <= 1 && Math.abs(go.y - y) <= 1;
	  }

	  public JSONObject toJSON() {
	    return new JSONObject(this.toHashMap());
	  }
	  
	  public String toString() {
	    return name;
	  }
	}
