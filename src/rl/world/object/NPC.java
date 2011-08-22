package rl.world.object;

import rl.util.Chance;
import rl.util.Colors;
import rl.util.Scheduler;
import rl.world.events.NPCEvent;

public class NPC extends Actor {
	  Actor target;

	  public NPC() {}

	  public NPC(int x, int y, char chr, int fg, int bg, String name) {
	    super(x, y, chr, fg, bg, name);
	    state = "awake";
	  }

	  public void takeTurn() {
	    if(state.equals("awake")) {
	      int dieRoll = Chance.rollDie(1, 10);

	      if(dieRoll > 1) {
	        int directionX = Chance.from(-1, 1), directionY = Chance.from(-1, 1);
	        
	        if(gmap.passable(x+directionX, y+directionY)) {
	          x += directionX;
	          y += directionY;
	        }
	      } else {
	        state = "asleep";
	        gmap.textEvent(name+" fell asleep.");
	      }
	    } else if (state.equals("asleep")) {
	      int dieRoll = Chance.rollDie(1, 10);

	      if(dieRoll <= 1) {
	        state = "awake";
	        gmap.textEvent(name+" woke up.");
	      }
	    } else if (state.equals("hostile")) {
	      if(Math.abs(target.x - x) <= 1 && Math.abs(target.y - y) <= 1) {
	        attack(target);
	      }
	    }
	  }


	  public void takeDamage(int amount, Actor source) {
	    stats.resHP -= amount;

	    target = source;
	    state = "hostile";

	    System.out.println(name+" takes "+amount+" damage!");

	    if(stats.resHP <= 0) {
	      die();
	    }
	  }

	  public void die() {
	    System.out.println(name+" has died.");
	    gmap.actors.gameObjects.remove(this);
	    gmap.actors.updated = true;
	    Item corpse = new Item(x, y, chr, Colors.GRAY, Colors.RED, name+" corpse");
	    corpse.has_bg = true;
	    gmap.objects.addGameObject(corpse);
	  }

	  public void scheduleMove(Scheduler s) {
	    s.scheduleEvent(new NPCEvent(this), turnDelay());
	  }

	  public boolean is(String what) {
	    return what.equals("NPC");
	  }
	  

	}