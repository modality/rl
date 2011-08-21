package rl.world.object;

public class Terrain extends GameObject {
  public int id;
  public boolean obstructs;
  
  public Terrain(int id, char character, int fg, int bg, String name, boolean passable, boolean obstructs) {
    super(0, 0, character, fg, bg);
    this.id = id;
    this.has_bg = true;
    this.passable = passable;
    this.name = name;
    this.obstructs = obstructs;
  }
  
  public boolean is(String what) {
    return what.equals("Terrain");
  }
  
  public String describe() {
    if(name.trim().length() > 0) {
      return "There is a "+name+" here.";
    } else {
      return "";
    }
  }
}