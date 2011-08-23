package rl.world;

import rl.RogueLike;
import rl.world.map.*;
import rl.world.map.layers.GameObjectLayer;
import rl.world.object.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GameWorldFactory {
	  RogueLike rl;

	  public GameWorldFactory(RogueLike rl) {
	    this.rl = rl;
	  }

	  public GameWorld createGameWorld(JSONObject world_json) {
	    GameWorld world = new GameWorld();
	    GameMap gmap;
	    Player player;

	    try { 
	      /* generate the map */
	      JSONObject map_json = world_json.getJSONObject("map");
	      JSONObject player_json = world_json.getJSONObject("player");

	      gmap = createGameMap(map_json);
	      world = new GameWorld(rl, gmap);

	      player = createPlayer(player_json);
	      world.addPlayer(player);
	    } catch (JSONException je) {
	      System.out.println("problem building world!");
	      System.out.println(je);
	    }

	    return world;
	  }

	  public GameMap createGameMap(JSONObject map_json) {
	    GameMap gmap = new GameMap();

	    try {
	      JSONArray terrain_array = map_json.getJSONArray("terrain");
	      int map_w = map_json.getInt("map_w"), map_h = map_json.getInt("map_h");
	      int[] terrain_ids = new int[map_w*map_h];

	      for(int i=(terrain_ids.length-1);i>=0;i--) {
	        terrain_ids[i] = terrain_array.getInt(i);
	      }

	      gmap = new GameMap(rl, map_w, map_h, terrain_ids);

	      JSONArray objects_json = map_json.getJSONArray("objects");
	      JSONArray actors_json = map_json.getJSONArray("npcs");

	      gmap.setObjects(createGameObjectLayer(objects_json, gmap));
	      gmap.setActors(createGameObjectLayer(actors_json, gmap));
	    } catch (JSONException je) {
	      System.out.println("error in createGameMap");
	      System.out.println(je);
	    }

	    return gmap;
	  }

	  public GameObjectLayer createGameObjectLayer(JSONArray layer_json, GameMap gmap) {
	    GameObjectLayer gol = new GameObjectLayer(rl, gmap.map_w, gmap.map_h);

	    try {
	      for(int i=layer_json.length()-1;i>=0;i--) {
	        JSONObject jo = layer_json.getJSONObject(i);
	        String type = jo.getString("type");

	        if(type.equals("NPC")) {
	          gol.addGameObject(createNPC(jo));
	        } else if (type.equals("Item")) {
	          gol.addGameObject(createItem(jo));
	        } else {
	          System.out.println("encountered typeless object! :: "+type);
	        }
	      }
	    } catch (JSONException je) {
	      System.out.println("error in createGameObjectLayer");
	      System.out.println(je);
	    }

	    return gol;
	  }

	  public Actor createNPC(JSONObject npc_json) {
	    Actor n = new NPC();

	    try {
	      n = new NPC(
	        npc_json.getInt("x"),
	        npc_json.getInt("y"),
	        (char)  npc_json.getInt("character"),
	        (int) npc_json.getInt("fg"),
	        (int) npc_json.getInt("bg"),
	        npc_json.getString("name")
	      );
	    } catch (JSONException je) {
	      System.out.println("error in createNPC");
	      System.out.println(je);
	    }

	    return n;
	  }

	  public Player createPlayer(JSONObject player_json) {
	    Player p = new Player();

	    try {
	      p = new Player(
	        player_json.getInt("x"),
	        player_json.getInt("y"),
	        (char)  player_json.getInt("character"),
	        (int) player_json.getInt("fg"),
	        (int) player_json.getInt("bg"),
	        player_json.getString("name")
	      );
	    } catch (JSONException je) {
	      System.out.println("error in createPlayer");
	      System.out.println(je);
	    }

	    return p;
	  }

	  public Item createItem(JSONObject item_json) {
	    Item i = new Item();

	    try {
	      i = new Item(
	        item_json.getInt("x"),
	        item_json.getInt("y"),
	        (char)  item_json.getInt("character"),
	        (int) item_json.getInt("fg"),
	        (int) item_json.getInt("bg"),
	        item_json.getString("name")
	      );
	    } catch (JSONException je) {
	    	System.out.println("error in createItem");
	    	System.out.println(je);
	    }

	    return i;
	  }

	}