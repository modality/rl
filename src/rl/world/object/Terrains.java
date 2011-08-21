package rl.world.object;

import rl.util.BFont;
import rl.util.Colors;

import java.util.ArrayList;

import processing.core.PApplet;

public class Terrains {
  public ArrayList<Terrain> terrainList;

  public Terrain NOTHING, EMPTY, GRASS, WALL, FLOOR, DOOR, START, RUSTY, PUDDLE;
  public Terrains(BFont font) {
    terrainList = new ArrayList<Terrain>();

    NOTHING = new Terrain(0, PApplet.parseChar(0), Colors.BLACK, Colors.GRAY, "", false, true);
    EMPTY = new Terrain(1, '.', Colors.DK_GRAY, Colors.BLACK, "", true, false);
    GRASS = new Terrain(2, PApplet.parseChar(175), Colors.GREEN, Colors.DK_GREEN, "patch of grass", true, false);
    WALL = new Terrain(3, PApplet.parseChar(175), 0xff777777, Colors.BLACK, "wall", false, true);
    FLOOR = new Terrain(4, PApplet.parseChar(206), 0xffC7915F, 0xffB08053, "wooden floor", true, false); // 198 / 206
    DOOR = new Terrain(5, '+', 0xff99704E, 0xff6B3910, "door", true, true);
    START = new Terrain(6, '.', Colors.DK_GRAY, Colors.BLACK, "", true, false);
    RUSTY = new Terrain(7, '#', 0xff82402A, Colors.BLACK, "rusty grate", false, false);
    PUDDLE = new Terrain(8, '.', Colors.PURPLE, Colors.DK_PURPLE, "puddle of muck", true, false);

    terrainList.add(NOTHING);
    terrainList.add(EMPTY);
    terrainList.add(GRASS);
    terrainList.add(WALL);
    terrainList.add(FLOOR);
    terrainList.add(DOOR);
    terrainList.add(START);
    terrainList.add(RUSTY);
    terrainList.add(PUDDLE);
    
    NOTHING.renderImage(font);
    EMPTY.renderImage(font);
    GRASS.renderImage(font);
    WALL.renderImage(font);
    FLOOR.renderImage(font);
    DOOR.renderImage(font);
    START.renderImage(font);
    RUSTY.renderImage(font);
    PUDDLE.renderImage(font);
  }

  public Terrain getTerrainById(int id) {
    return terrainList.get(id);
  }
}