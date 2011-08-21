package rl.util;

import rl.world.GameWorld;
import rl.world.GameWorldFactory;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import processing.core.PApplet;

public class SaveFileHandler {
  String saveRoot;
  PApplet pa;

  public SaveFileHandler(PApplet pa) {
	this.pa = pa;
    saveRoot = "saves/";
  }
  
  public GameWorld loadWorld(GameWorldFactory factory, String filename) {
    String[] strings = pa.loadStrings(saveRoot+filename);
    JSONTokener jt;
    JSONObject world_json = new JSONObject();

    try {
      jt = new JSONTokener(strings[0]);
      world_json = new JSONObject(jt);
    } catch (JSONException je) {
      System.out.println(je);
    }

    return factory.createGameWorld(world_json);
  }

  public void saveWorld(GameWorld gw, String filename) {
    String[] strings = new String[1];
    JSONObject json = gw.toJSON();
    
    strings[0] = json.toString();

    System.out.println("Saving JSON to file...");    
    pa.saveStrings(saveRoot+filename, strings);
  }
  
  //ArrayList listSavefiles() {  
  //}
}