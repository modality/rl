package rl.world.object;

import rl.util.Chance;
import rl.util.Colors;
import rl.util.Point;
import rl.util.Scheduler;
import rl.util.ai.AI;
import rl.world.events.NPCEvent;
import rl.world.map.Obstacles;
import rlforj.math.Point2I;
import rlforj.pathfinding.AStar;

public class NPC extends Actor {
	  public Actor target;
	  public AI ai;

	  public NPC() {}

	  public NPC(int x, int y, char chr, int fg, int bg, String name) {
	    super(x, y, chr, fg, bg, name);
	    state = "awake";
	  }

	  public void takeTurn() {
	    if(ai != null) {
  	    ai.action(this);
	    }
	  }


	  public void takeDamage(int amount, Actor source) {
	    stats.resHP -= amount;

	    target = source;
	    state = "hostile";

	    gmap.textEvent(name+" takes "+amount+" damage!");

	    if(stats.resHP <= 0) {
	      die();
	    }
	  }

	  public void die() {
	    System.out.println(name+" has died.");
	    gmap.removeActor(this);
	    gmap.getActors().updated = true;
	    Item corpse = new Item(x, y, chr, Colors.GRAY, Colors.RED, name+" corpse");
	    corpse.has_bg = true;
	    gmap.getObjects().addGameObject(corpse);
	  }

	  public void scheduleMove(Scheduler s) {
	    s.scheduleEvent(new NPCEvent(this), turnDelay());
	  }

	  public boolean is(String what) {
	    return what.equals("NPC");
	  }

    public void moveTo(Point p) {
      Obstacles obstacles = new Obstacles(this.gmap);
      AStar pathfind = new AStar(obstacles, gmap.map_w, gmap.map_h, true);
      
      Point2I[] p2i = pathfind.findPath(this.x, this.y, p.x, p.y);
      if(p2i.length > 1) {
        this.x = p2i[1].x;
        this.y = p2i[1].y;
      }
      this.gmap.getActors().updated = true;
    }
	  

	}