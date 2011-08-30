package rl.util.generators;

import rl.util.Point;

public class Pathway {
  /*
    starting
    direction
        0
        |
    3 - + - 1
        |
        2 
                     entrance                 negation            entrance
    name      value   symbol   geometry         name      value    symbol    geometry
    ---------------------------------------------------------------------------------
    STRAIGHT   (1)      ->        --     = !  T-END        (-1)      ->        -||
    L-TURN     (2)      ->        -^     = !  T-RIGHT      (-2)      ->        -v-
    R-TURN     (3)      ->        -v     = !  T-LEFT       (-3)      ->        _^_
    DEAD-END   (4)      ->        -]     = !  4-WAY        (-4)      ->        -|-
    
 */
  int orientation;
  int geometry;
  public boolean[] openings;
  public boolean[] mandatories;
  
  public Pathway(int orientation, int geometry) {
    this.orientation = orientation;
    this.geometry = geometry;
    this.openings = resolveOpenings();
    
    mandatories = new boolean[4];
    for(int i=0;i<4;i++) {
      mandatories[i] = false;
    }
  }
  
  public boolean[] resolveOpenings() {
    boolean[] openings = new boolean[4];
    
    for(int i=0;i<4;i++) {
      openings[i] = false;
    }
    
    switch(Math.abs(geometry)) {
      case 1:
        openings[orientation] = true;
        break;
      case 2:
        openings[normalizeDirection(orientation-1)] = true;
        break;
      case 3:
        openings[normalizeDirection(orientation+1)] = true;
        break;
      case 4:
        break;
      default:
        break;
    }
    
    if(geometry < 0) {
      openings = negateArray(openings);
    }
    
    // set entrance direction to be open
    openings[oppositeDirection(orientation)] = true;
   
    return openings;
  }
  
  public int numOpenings() {
    int count = 0;
    for(int i=0;i<openings.length;i++) {
      if(openings[i]) {
        count++;
      }
    }
    return count;
  }
    
  static boolean[] negateArray(boolean[] array) {
    boolean[] negated = new boolean[array.length];
    
    for(int i=0;i<array.length;i++) {
      negated[i] = !array[i];
    }
    
    return negated;
  }

  static int oppositeDirection(int direction) {
    return normalizeDirection(direction + 2);
  }
  
  static int normalizeDirection(int direction) {
    if(direction > 3) {
      return direction - 4;
    } else if(direction < 0) {
      return direction + 4;
    } else {
      return direction;
    }
  }
  
  public static Point relativeDirection(Point p, int direction) {
    Point r = new Point(p.x, p.y);
    
    switch(direction) {
      case 0:
        r.y = r.y - 1;
        break;
      case 1:
        r.x = r.x + 1;
        break;
      case 2:
        r.y = r.y + 1;
        break;
      case 3:
        r.x = r.x - 1;
        break;
      default:
        break;
    }

    return r;
  }
  
}
