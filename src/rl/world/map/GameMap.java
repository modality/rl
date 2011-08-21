package rl.world.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import processing.core.PImage;
import rl.RogueLike;
import rl.world.object.*;
import rl.world.events.*;
import rl.world.map.layers.*;
import rl.util.*;
import rl.util.generators.DungeonBuilder;

public class GameMap extends ViewLayer implements SaveableObject {
	  public int map_w, map_h;
	  public Terrain[][] terrain;
	  public Terrains terras;
	  public GameObjectLayer objects, actors;
	  public FOVLayer fov;

	  public GameMap() {
	    super();
	  }
	  
	  public GameMap(RogueLike rl, int map_w, int map_h) {
	    super(rl, map_w, map_h);
	    
	    this.map_w = map_w;
	    this.map_h = map_h;
	    this.terrain = new Terrain[map_w][map_h];
	    this.terras = new Terrains(rl.bfont);
	    
	    objects = new GameObjectLayer(rl, map_w, map_h);
	    actors = new GameObjectLayer(rl, map_w, map_h);
	    updated = true;

	    DungeonBuilder db = new DungeonBuilder(this);
	    db.build();
	    fov = new FOVLayer(rl, this);
	    render();
	  }

	  public GameMap(RogueLike rl, int map_w, int map_h, int[] terrain_ids) {
	    super(rl, map_w, map_h);

	    this.map_w = map_w;
	    this.map_h = map_h;
	    this.terrain = new Terrain[map_w][map_h];
	    this.terras = new Terrains(rl.bfont);

	    for(int i=terrain_ids.length-1;i>=0;i--) {
	      terrain[i % map_w][(int) Math.floor(i/map_w)] = terras.getTerrainById(terrain_ids[i]);
	    }
	    fov = new FOVLayer(rl, this);
	    updated = true;
	  }
	  
	  public void clearAll() {
	    setAll(terras.NOTHING);
	  }

	  public void drawBuilding(int x, int y, int w, int h) {
	    for(int i=x; i<(x+w); i++) {
	      for(int j=y; j<(y+h); j++) {
	        if(i==x || i==(x+w-1) || j==y || j==(y+h-1)) {
	          terrain[i][j] = terras.WALL;
	        } else {
	          terrain[i][j] = terras.FLOOR;
	        }
	      }
	    }
	    terrain[x+3][y+7] = terras.DOOR;
	    this.updated = true;
	  }

	  public Point startPoint() {
	    for(int i=0; i<map_w; i++) {
	      for(int j=0; j<map_h; j++) {
	        if(terrain[i][j] == terras.START) {
	          return new Point(i, j);
	        }
	      }
	    }
	    return new Point(0, 0);
	  }
	  
	  public boolean passable(int x, int y) {
	    if(x >= 0 && x < map_w && y >= 0 && y < map_h) {
	      return terrain[x][y].passable && actors.passable(x, y) && objects.passable(x, y);
	    }
	    return false;
	  }

	  public void textEvent(String text) {
	    rl.uih.addMessage(text);
	  }
	  
	  public void setAll(Terrain t) {
	    for(int j=0; j<map_h; j++) {
	      for(int i=0; i<map_w; i++) {
	        terrain[i][j] = t;
	      }
	    }
	    this.updated = true;
	  }

	  public Terrain terrainAt(int x, int y) {
	    return terrain[x][y];
	  }

	  public void handleEvent(GameEvent ge, Scheduler scheduler) {
	    if(ge.is("NPCEvent")) {
	      UUID npcId = ((NPCEvent) ge).id;
	      NPC npc = (NPC) actors.findObjectByUUID(npcId);

	      if(npc != null) { // npc could be dead!
	        // let the NPC take their turn
	        npc.takeTurn();

	        // let the NPC schedule their next move
	        npc.scheduleMove(scheduler);
	        actors.updated = true;
	      }
	    }
	  }

	  public String describeSquare(int x, int y, boolean verbose) {
	    ArrayList<GameObject> objs = objects.objectsAt(x, y);
	    ArrayList<GameObject> acts = actors.objectsAt(x, y);

	    String description = "";

	    objs.addAll(acts);

	    Iterator<GameObject> itr = objs.iterator();
	    while(itr.hasNext()) {
	      GameObject go = itr.next();
	      if(verbose || !go.is("Player")) {
	        description += go.describe() + "\n";
	      }
	    }

	    if(verbose) {
	      description += terrainAt(x, y).describe();
	    }

	    if(description.trim().length() == 0 && verbose) {
	      description = "There is nothing here.";
	    }

	    return description.trim();
	  }
	  
	  public void render() {
	    for(int j=0; j<map_h; j++) {
	      for(int i=0; i<map_w; i++) {
	        if(terrain[i][j].id != terras.NOTHING.id) {
	          PImage t_img = terrain[i][j].img;
	          this.copyToTile(t_img, i, j);
	        }
	      }
	    }
	  }

	  public void draw() {
	    super.draw();
	    objects.draw();
	    actors.draw();
	    fov.draw();
	  }

	  public HashMap toHashMap() {
	    HashMap hm = new HashMap();
	    ArrayList t_list = new ArrayList();
	    hm.put("map_w", map_w);
	    hm.put("map_h", map_h);
	    for(int j=0; j<map_h; j++) {
	      for(int i=0; i<map_w; i++) {
	        t_list.add(terrain[i][j].id);
	      }
	    }
	    hm.put("terrain", t_list);
	    return hm;
	  }

	  public JSONObject toJSON() {
	    JSONObject map = new JSONObject(this.toHashMap());
	    try {
	      map.put("npcs", actors.toJSON());
	      map.put("objects", objects.toJSON());
	    } catch (JSONException je) {
	    	System.out.println("failed add objects and NPCS to map");
	    }

	    return map;
	  }
	}