package rl.util.generators;

import java.util.ArrayList;

import rl.util.Chance;
import rl.util.Point;

public class LevelBuilder {
  public static class Markers {
    public static Integer EMPTY = new Integer(0);
    public static Integer FILLED = new Integer(1);
    public static Integer HOTPOINT = new Integer(2);
  }
  
  public static class BuilderParams {
    public int width, height, nodes, failThreshold;
    
    public BuilderParams(int width, int height, int nodes, int failThreshold) {
      this.width = width;
      this.height = height;
      this.nodes = nodes;
      this.failThreshold = failThreshold;
    }
  }
  
  public BuilderParams params;
  public Grid levelLayout, levelStates;
  ArrayList<Hotpoint> hotPoints;
  
  public LevelBuilder(BuilderParams bp) {
    this.params = bp;
    this.levelLayout = new Grid(params.width, params.height);
    this.levelStates = new Grid(params.width, params.height);
    this.hotPoints = new ArrayList<Hotpoint>();

    compute();
  }
  
  public void compute() {
    Pathway starting = getRandom(Chance.from(0, 3));
    levelStates.setAll(Markers.EMPTY);

    updateMap(new Point(19, 19), starting);
    
    int failedTries = 0;
    int built = 1;
    
    while(built < (params.nodes) && failedTries < params.failThreshold && hotPoints.size() > 0) {
      // get random hotpoint
      int index = (int) Math.floor(Math.random() * hotPoints.size());
      Hotpoint hp = hotPoints.remove(index);
      
      // generate pathway from hotpoint
      Pathway path = getRandom(hp.direction);
      
      if((hotPoints.size() + path.numOpenings() > 2) && (updateMap(hp.loc, path))) {
        built++;
        failedTries = 0;
      } else {
        hotPoints.add(hp);
        failedTries++;
      }
    }
  }
  
  public boolean updateMap(Point p, Pathway path) {
    if(levelStates.get(p) == Markers.HOTPOINT) {
      Hotpoint replacer = (Hotpoint) levelLayout.get(p);
      for(int i=0;i<path.openings.length;i++) {
        if(replacer.mandatories[i] && !path.openings[i]) {
          return false;
        }
      }
    }

    boolean[] hotpoints = path.openings;
    for(int i=0;i<hotpoints.length;i++) {
      if(hotpoints[i]) {
        Point hp = Pathway.relativeDirection(p, i);
        if(!levelStates.contains(hp)) {
          return false;
        }
        if(levelStates.get(hp) == Markers.FILLED) {
          Pathway existing = (Pathway) levelLayout.get(hp);
          if(!existing.openings[Pathway.oppositeDirection(i)]) {
            return false;
          }
        } else if(levelStates.get(hp) == Markers.HOTPOINT) {
          Hotpoint existing = (Hotpoint) levelLayout.get(hp);
          existing.reset();
          existing.maybeInclude(Pathway.oppositeDirection(i));
          levelLayout.set(hp.x, hp.y, existing);
        }
      }
    }
    
    if(levelStates.get(p) == Markers.HOTPOINT) {
      path.mandatories = ((Hotpoint) levelLayout.get(p)).mandatories;
    }
    levelLayout.set(p.x, p.y, path);
    levelStates.set(p.x, p.y, Markers.FILLED);
    for(int i=0;i<hotpoints.length;i++) {
      if(hotpoints[i]) {
        Point hp = Pathway.relativeDirection(p, i);
        if(levelStates.get(hp) == Markers.EMPTY) {
          hotPoints.add(new Hotpoint(hp, i));
          levelLayout.set(hp.x, hp.y, new Hotpoint(hp, i));
          levelStates.set(hp.x, hp.y, Markers.HOTPOINT);
        } else if(levelStates.get(hp) == Markers.HOTPOINT) {
          Hotpoint existing = (Hotpoint) levelLayout.get(hp);
          existing.commit();
          levelLayout.set(hp.x, hp.y, existing);
        }
      }
    }
    
    return true;
  }

  public Pathway getRandom(int direction) {
    int type = Chance.from(1, 6);
    
    if(type == 1) { // dead end
      return new Pathway(direction, 4);
    } else if(type == 2 || type == 3) { // one way
      return new Pathway(direction, Chance.from(1, 3));
    } else if(type == 4 || type == 5) { // t-junction
      return new Pathway(direction, -Chance.from(1, 3));
    } else { // 4 way
      return new Pathway(direction, -4);
    }
  }
  
}
