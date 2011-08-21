package rl.world;

import rl.RogueLike;
import rl.world.events.*;
import rl.world.map.*;
import rl.world.map.layers.*;
import rl.world.object.*;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import processing.core.PConstants;

import rl.util.EquipMenu;
import rl.util.GameConstants;
import rl.util.GameObjectMenu;
import rl.util.Point;
import rl.util.SaveableObject;
import rl.util.Scheduler;

public class GameWorld implements SaveableObject {
	  public int map_w, map_h;
	  RogueLike rl;
	  Player player;
	  Scheduler scheduler;
	  GameMap gmap;
	  CursorLayer cursors;
	  int GAME_MODE;
	  boolean player_turn;

	  public GameWorld() {}

	  public GameWorld(RogueLike rl, int map_w, int map_h) {
  		this.rl = rl;
	    this.gmap = new GameMap(rl, map_w, map_h);

	    GAME_MODE = GameConstants.MODE_WALK;
	    this.map_w = map_w;
	    this.map_h = map_h;
	    this.cursors = new CursorLayer(rl, map_w, map_h);
	    this.player_turn = false;

	    this.scheduler = new Scheduler();
	  }

	  public GameWorld(RogueLike rl, GameMap gmap) {
	    this.rl = rl;

	    this.gmap = gmap;

	    GAME_MODE = GameConstants.MODE_WALK;
	    this.map_w = gmap.map_w;
	    this.map_h = gmap.map_h;
	    this.cursors = new CursorLayer(rl, gmap.map_w, gmap.map_h);
	    this.player_turn = false;

	    this.scheduler = new Scheduler();
	  }

	  public void addPlayer(Player player) {
	    this.player = player;
	    player.passable = true;
	    Point mapStart = gmap.startPoint();
	    player.x = mapStart.x;
	    player.y = mapStart.y;
	    gmap.fov.setPlayerPos(mapStart);
	    addActor(player);
	  }

	  public void addActor(Actor a) {
	    gmap.actors.addGameObject(a);
	    a.gmap = gmap;
	    a.scheduleMove(scheduler);
	  }

	  public void addGameObject(GameObject go) {
	    gmap.objects.addGameObject(go);
	  }

	  public void draw(boolean withUpdate) {
	    rl.background(0);

	    if(withUpdate) {
	      updateWorld();
	    }

	    gmap.draw();
	    if(GAME_MODE == GameConstants.MODE_LOOK || GAME_MODE == GameConstants.MODE_ATTACK) {
	      cursors.draw();
	    }
	  }

	  public void tryMove(int x, int y) {
	    if(gmap.passable(x, y)) {
	      player.x = x;
	      player.y = y;
	      rl.uih.addMessage(gmap.describeSquare(player.x, player.y, false));
	    }
	    gmap.actors.updated = true;
	    gmap.fov.updated = true;
	    gmap.fov.setPlayerPos(player.getPosition());
	  }
	  
	  public void tryGrab() {
	  }
	  
	  public void printInventory() {
	    Iterator<GameObject> itr = player.inventory.iterator();
	    String items = "";
	    while(itr.hasNext()) {
	      items += ((GameObject) itr.next()).describe() + "\n";
	    }
	    rl.uih.setHudText(items);
	  }

	  public void lookAround() {
	    rl.uih.setHudText(gmap.describeSquare(cursors.cursorX, cursors.cursorY, true));
	  }

	  public void updateWorld() {
	    while(!player_turn) {
	      GameEvent ge = (GameEvent) scheduler.nextEvent();

	      if (ge.is("PlayerEvent")) {
	        player_turn = true;
	      } else {
	        // its an NPC event for now
	        gmap.handleEvent(ge, scheduler);
	      }

	      // do a basic screen refresh
	      draw(false);
	    }
	  }

	  public void handleInput(int key, int keyCode) {
	    boolean valid_turn_input = false;
	    if(player_turn) {
	      if(GAME_MODE == GameConstants.MODE_WALK) {
	        if(key == PConstants.CODED) {
	          if(keyCode == PConstants.UP) {
	            tryMove(player.x, player.y-1);
	            valid_turn_input = true;
	          } else if (keyCode == PConstants.DOWN) {
	            tryMove(player.x, player.y+1);
	            valid_turn_input = true;
	          } else if (keyCode == PConstants.RIGHT) {
	            tryMove(player.x+1, player.y);
	            valid_turn_input = true;
	          } else if (keyCode == PConstants.LEFT) {
	            tryMove(player.x-1, player.y);
	            valid_turn_input = true;
	          }
	        } else {
	          if(key == 'l') {
	            changeMode(GameConstants.MODE_LOOK);
	          } else if(key == 'a') {
	            changeMode(GameConstants.MODE_ATTACK);
	          } else if(key == 'g') {
	            changeMode(GameConstants.MODE_GRAB);
	          } else if(key == 'i') {
	            printInventory();
	          } else if(key == 'e') {
	            changeMode(GameConstants.MODE_EQUIP);
	          }
	        }
	      } else if (GAME_MODE == GameConstants.MODE_LOOK) {
	        if (key == 27) {
	          changeMode(GameConstants.MODE_WALK);
	        } else {
	          cursors.handleInput(key, keyCode);
	          lookAround();
	        }
	      } else if (GAME_MODE == GameConstants.MODE_ATTACK) {
	        if (key == 27) {
	          changeMode(GameConstants.MODE_WALK);
	        } else {
	          if(key == 10 || keyCode == PConstants.ENTER || keyCode == PConstants.RETURN) {
	            if(!cursors.getPosition().equals(player.getPosition())) {
	              ArrayList<GameObject> targets = gmap.actors.objectsAt(cursors.getPosition());
	              if(targets.size() > 0) {
	                NPC target = (NPC) targets.get(0);
	                player.attack(target);
	                gmap.actors.updated = true;
	                changeMode(GameConstants.MODE_WALK);
	                valid_turn_input = true;
	              }
	            }
	          } else {
	            cursors.handleInput(key, keyCode);
	            lookAround();
	          }
	        }
	      } else if (GAME_MODE == GameConstants.MODE_GRAB) {
	        if (key == 27) {
	          rl.uih.clearMenu();
	          changeMode(GameConstants.MODE_WALK);
	        } else {
	          GameObject selectedItem;
	          selectedItem = (GameObject) rl.uih.currentMenu.handleInput(key, keyCode);
	          if(selectedItem != null) {
        	    gmap.objects.gameObjects.remove(selectedItem);
        	    gmap.objects.updated = true;
        	    player.inventory.add(selectedItem);
        	    changeMode(GameConstants.MODE_WALK);
        	    rl.uih.clearMenu();
	          } else {
  	          rl.uih.updateMenu();
	          }
	        }
	      } else if (GAME_MODE == GameConstants.MODE_EQUIP) {
	        if(key == 27) {
	          rl.uih.clearMenu();
	          changeMode(GameConstants.MODE_WALK);
	        } else {
	          rl.uih.currentMenu.handleInput(key, keyCode);
	          rl.uih.updateMenu();
	        }
	      }

	      // if the player made a legit move this turn
	      if(valid_turn_input) {
	        player_turn = false;
	        player.scheduleMove(scheduler);
	      }
	      updateWorld();
	    }
	    draw(false);
	  }

	  public void changeMode(int mode) {
	    GAME_MODE = mode;
	    if(mode == GameConstants.MODE_WALK) {
	      rl.uih.clearHudText();
	    } else if(mode == GameConstants.MODE_LOOK || mode == GameConstants.MODE_ATTACK) {
	      cursors.setMode(mode);
	      cursors.setPlayerPosition(player.x, player.y);
	      lookAround();
	    } else if(mode == GameConstants.MODE_GRAB) {
  	    ArrayList<GameObject> items = gmap.objects.objectsAt(player.getPosition());
  	    if(items.size() > 0) {
  	      GameObjectMenu gmenu = new GameObjectMenu(items);
  	      rl.uih.showMenu(gmenu);
  	    } else {
  	      changeMode(GameConstants.MODE_WALK);
  	    }
	    } else if(mode == GameConstants.MODE_EQUIP) {
	      EquipMenu emenu = new EquipMenu(player);
	      rl.uih.showMenu(emenu);
	    }
	  }

	  public JSONObject toJSON() {
	    JSONObject json = new JSONObject(), map = new JSONObject();

	    try {
	      json.put("player", player.toJSON());

	      map = gmap.toJSON();

	      json.put("map", map);
	    } catch (JSONException je) {
	      System.out.println("### Problem creating GameWorld JSON");
	      System.out.println(je);
	    }

	    return json;
	  }
	}