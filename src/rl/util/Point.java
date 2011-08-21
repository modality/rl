package rl.util;

public class Point {
  public int x, y;
  public String direction;

  public Point(int x, int y) {
    this.x = x;
    this.y = y;
    this.direction = "NONE";
  }

  public Point(int x, int y, String dir) {
    this.x = x;
    this.y = y;
    this.direction = dir;
  }

  public boolean facing(String f) {
    return f.equals(direction);
  }

  public boolean equals(Point p) {
    return this.x == p.x && this.y == p.y;
  }
}