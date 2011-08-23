package rl.world.map;

import rlforj.los.ILosBoard;

public class Obstacles implements ILosBoard {
  boolean[][] obstacles;
  int map_w, map_h;

  public Obstacles(GameMap gmap) {
    this.map_w = gmap.map_w;
    this.map_h = gmap.map_h;

    obstacles = new boolean[map_w][map_h];
    
    for(int i=0;i<map_w;i++) {
      for(int j=0;j<map_h;j++) {
        obstacles[i][j] = !gmap.terrain[i][j].passable;
      }
    }
  }

  @Override
  public boolean contains(int x, int y) {
    return x >= 0 && x < map_w && y >= 0 && y < map_h;
  }

  @Override
  public boolean isObstacle(int x, int y) {
    return obstacles[x][y];
  }

  @Override
  public void visit(int arg0, int arg1) {
  }
}
