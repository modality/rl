package rl.world.map.layers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

import rl.RogueLike;
import rl.util.BFont;
import rl.util.Point;
import rl.util.SaveableCollection;
import rl.util.SaveableObject;
import rl.world.object.GameObject;

public class GameObjectLayer extends ViewLayer implements SaveableCollection {
	  public int map_w, map_h;
	  public ArrayList<GameObject> gameObjects;
	  public BFont font;
	  
	  public GameObjectLayer(RogueLike rl, int map_w, int map_h) {
	    super(rl, map_w, map_h);
	    this.map_w = map_w;
	    this.map_h = map_h;
	    this.font = rl.bfont;
	    
	    gameObjects = new ArrayList<GameObject>();
	  }

	  public void addGameObject(GameObject go) {
	    this.updated = true;
	    gameObjects.add(go);
	  }

	  public GameObject findObjectByUUID(UUID id) {
	    Iterator<GameObject> itr = gameObjects.iterator();
	    while(itr.hasNext()) {
	      GameObject go = itr.next();
	      if(id.equals(go.id)) {
	        return go;
	      }
	    }
	    return null;
	  }
	  
	  public ArrayList<GameObject> objectsAt(int x, int y) {
	    ArrayList<GameObject> objs = new ArrayList<GameObject>();
	    Iterator<GameObject> itr = gameObjects.iterator();
	    while(itr.hasNext()) {
	      GameObject go = itr.next();
	      if(go.x == x && go.y == y) {
	        objs.add(go);
	      }
	    }
	    return objs;
	  }

	  public ArrayList<GameObject> objectsAt(Point p) {
	    return objectsAt(p.x, p.y);
	  }
	  
	  public boolean passable(int x, int y) {
	    Iterator<GameObject> itr = gameObjects.iterator();
	    while(itr.hasNext()) {
	      GameObject go = itr.next();
	      if(go.x == x && go.y == y && !go.passable) {
	        return false;
	      }
	    }
	    return true;
	  }

	  /**
	   * Graphics Methods
	   **/

	  public void render() {
	    clear();
	    Iterator<GameObject> itr = gameObjects.iterator();
	    while(itr.hasNext()) {
	      GameObject go = itr.next();
	      if(go.updated) {
	        go.renderImage(font);
	      }
	      this.copyToTile(go.img, go.x, go.y);
	    }
	  }

	  /**
	   * Saving Methods
	   **/

  public JSONArray toJSON() {
    ArrayList<JSONObject> json_objects = new ArrayList<JSONObject>();

    Iterator<GameObject> itr = gameObjects.iterator();
    while(itr.hasNext()) {
      SaveableObject so = (SaveableObject) itr.next();
      json_objects.add(so.toJSON());
    }
    return new JSONArray(json_objects);
  }
}
