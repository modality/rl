package rl;

import processing.core.*; 
import rl.util.generators.LevelBuilder;
import rl.util.generators.Hotpoint;
import rl.util.generators.LevelBuilder.BuilderParams;
import rl.util.generators.Pathway;

public class LevelBuilderTest extends PApplet {
  private static final long serialVersionUID = 7845089357502987752L;
  
  LevelBuilder lb;
  BuilderParams bp;

  public void setup() {
    bp = new BuilderParams(40, 40, 250, 100);
    size(bp.width*20, bp.height*20);

    lb = new LevelBuilder(bp);
    noStroke();
  }

  public void draw() {
    background(0);
    
    for(int i=0;i<lb.levelStates.width;i++) {
      for(int j=0;j<lb.levelStates.height;j++) {
        if(lb.levelStates.get(i, j) == LevelBuilder.Markers.FILLED) {
          paintFilled(i, j, (Pathway)lb.levelLayout.get(i, j));
        } else if(lb.levelStates.get(i, j) == LevelBuilder.Markers.HOTPOINT) {
          paintHotpoint(i, j, (Hotpoint)lb.levelLayout.get(i, j));
        }
      }
    }
  }
  
  public void keyPressed() {
    if (key == 'r') {
      lb = new LevelBuilder(bp);
    }
  }
  
  public void paintHotpoint(int x, int y, Hotpoint hp) {
    boolean[] openings = hp.mandatories;

    stroke(0);
    fill(color(255, 70, 70));
    rect(x*20, y*20, 20, 20);

    noStroke();
    fill(color(0, 0, 0));
    if(openings[0]) {
      rect(x*20+8, y*20, 4, 6);
    }
    if(openings[1]) {
      rect(x*20+14, y*20+8, 6, 4);
    }
    if(openings[2]) {
      rect(x*20+8, y*20+14, 4, 6);
    }
    if(openings[3]) {
      rect(x*20, y*20+8, 6, 4);
    }
  }
  
  public void paintFilled(int x, int y, Pathway p) {
    boolean[] openings = p.openings;
    boolean[] mandatories = p.mandatories;
    
    stroke(0);
    fill(color(255));
    rect(x*20, y*20, 20, 20);
    
    noStroke();
    
    fill(color(0, 192, 0));
    if(mandatories[0]) {
      rect(x*20+6, y*20, 8, 6);
    }
    if(mandatories[1]) {
      rect(x*20+14, y*20+6, 6, 8);
    }
    if(mandatories[2]) {
      rect(x*20+6, y*20+13, 8, 6);
    }
    if(mandatories[3]) {
      rect(x*20, y*20+6, 6, 8);
    }

    fill(color(0, 0, 0));
    if(openings[0]) {
      rect(x*20+8, y*20, 4, 12);
    }
    if(openings[1]) {
      rect(x*20+8, y*20+8, 12, 4);
    }
    if(openings[2]) {
      rect(x*20+8, y*20+8, 4, 12);
    }
    if(openings[3]) {
      rect(x*20, y*20+8, 12, 4);
    }
  }

  static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#FFFFFF", "rl" });
  }
}
