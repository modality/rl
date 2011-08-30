package rl.util.generators;

import rl.util.Point;

public class Hotpoint {
  public int direction;
  public Point loc;
  public boolean[] mandatories;
  public boolean[] maybes;

  public Hotpoint(Point loc, int direction) {
    this.loc = loc;
    this.direction = direction;
    this.mandatories = new boolean[4];
    this.maybes = new boolean[4];
    for(int i=0;i<4;i++) {
      mandatories[i] = false;
      maybes[i] = false;
    }
    include(Pathway.oppositeDirection(direction));
  }
  
  public void include(int direction) {
    mandatories[direction] = true;
  }
  
  public void maybeInclude(int direction) {
    maybes[direction] = true;
  }
  
  public void reset() {
    for(int i=0;i<4;i++) {
      maybes[i] = false;
    }
  }
  
  public void commit() {
    for(int i=0;i<4;i++) {
      mandatories[i] = mandatories[i] || maybes[i];
    }
  }
}
