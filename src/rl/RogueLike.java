package rl;

import processing.core.*; 
import controlP5.*;
import rl.util.BFont;
import rl.util.Colors;
import rl.util.SaveFileHandler;
import rl.util.UiHandler;
import rl.world.GameWorld;
import rl.world.GameWorldFactory;
import rl.world.object.*; 

public class RogueLike extends PApplet {
	private static final long serialVersionUID = -5459930499980402593L;
	public BFont bfont;
	public GameWorld gw;
	public String savefile;
	public UiHandler uih;
	public SaveFileHandler sfh;

	public void setup() {
		size(800, 600);
		smooth();

		ControlP5 cp5 = new ControlP5(this);
  uih = new UiHandler(cp5);
  sfh = new SaveFileHandler(this);
  bfont = new BFont(this, "data/font.gif");
  
  gw = new GameWorld(this, 64, 48);
  savefile = "savefile.gz";
  
  frameRate(24);

  Player player = new Player(0, 0, '@', Colors.WHITE, Colors.BLACK, "Throg");
  player.stats.attrSTR = 14;
  player.stats.attrDEX = 14;
  player.stats.resHP = 10;
  gw.addPlayer(player);
  
  NPC npc = new NPC(floor(random(64)), floor(random(48)), PApplet.parseChar(235), Colors.PURPLE, Colors.BLACK, "Wumpus");
  npc.stats.attrSTR = 10;
  npc.stats.attrDEX = 10;
  npc.stats.resHP = 4;
  gw.addActor(npc);
  
  Weapon sword = new Weapon(player.x-1, player.y-1, '/', Colors.GRAY, Colors.BLACK, "sword", 1, 6, 0); 
  gw.addGameObject(sword);
  
  gw.draw(true);
}

public void draw() {
  background(0);
  gw.draw(true);
}

public void keyPressed() {
  if (key == 's') {
    println("save state");

    sfh.saveWorld(gw, savefile);
  } else if (key == 'o') {
    println("load state");
    
    gw = sfh.loadWorld(new GameWorldFactory(this), savefile);
  } else {
    gw.handleInput(key, keyCode);
  }
  
  if (key == ESC) {
    key = 0;  // Fools! don't let them escape!
  }
}

  static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#FFFFFF", "rl" });
  }
}
