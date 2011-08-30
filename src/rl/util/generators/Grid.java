package rl.util.generators;

import rl.util.Point;

public class Grid {
  public int width, height;
  Object[][] grid;
  
  public Grid(int width, int height) {
    this.width = width;
    this.height = height;
    this.grid = new Object[width][height];
  }
  
  public Object get(int x, int y) {
    return grid[x][y];
  }
  
  public Object get(Point p) {
    return grid[p.x][p.y];
  }
  
  public void set(int x, int y, Object value) {
    if(contains(x, y)) {
      grid[x][y] = value;
    }
  }
  
  public void setAll(Object value) {
    for(int i=0;i<width;i++) {
      for(int j=0;j<height;j++) {
        grid[i][j] = value;
      }
    }
  }
  
  public boolean contains(int x, int y) {
    return x >= 0 && x < width && y >= 0 && y < height;
  }
  
  public boolean contains(Point p) {
    return contains(p.x, p.y);
  }
}
