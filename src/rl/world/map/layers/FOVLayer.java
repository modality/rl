package rl.world.map.layers;

import java.util.ArrayList;
import java.util.Iterator;

import rl.RogueLike;
import rl.util.BFont;
import rl.util.GameConstants;
import rl.util.Point;
import rl.world.map.GameMap;

public class FOVLayer extends ViewLayer {
	  int map_w, map_h;
	  boolean[][] obstructs, viewing, visited;
	  BFont font;
	  int playerX, playerY, range;

	  public FOVLayer(RogueLike rl, GameMap gmap) {
	    super(rl, gmap.map_w, gmap.map_h);
	    
	    this.font = rl.bfont;
	    this.map_w = gmap.map_w;
	    this.map_h = gmap.map_h;
	    this.obstructs = new boolean[map_w][map_h];
	    this.visited = new boolean[map_w][map_h];
	    this.playerX = 0;
	    this.playerY = 0;
	    this.range = 10;

	    for(int i=0;i<map_w;i++) {
	      for(int j=0;j<map_h;j++) {
	        obstructs[i][j] = gmap.terrain[i][j].obstructs;
	        visited[i][j] = false;
	      }
	    }
	    updated = true;
	  }

	  public void setPlayerPos(Point p) {
	    playerX = p.x;
	    playerY = p.y;
	  }

	  public boolean[][] getEmpty() {  
	    boolean[][] empty = new boolean[map_w][map_h];
	    for(int i=0;i<map_w;i++) {
	      for(int j=0;j<map_h;j++) {
	        empty[i][j] = false;
	      }
	    }
	    return empty;
	  }


	  public ArrayList<Point> bresenhamLine(int x,int y,int x2, int y2) {
	    ArrayList<Point> points = new ArrayList<Point>();

	    int w = x2 - x ;
	    int h = y2 - y ;
	    int dx1 = 0, dy1 = 0, dx2 = 0, dy2 = 0 ;
	    if (w<0) dx1 = -1 ; else if (w>0) dx1 = 1 ;
	    if (h<0) dy1 = -1 ; else if (h>0) dy1 = 1 ;
	    if (w<0) dx2 = -1 ; else if (w>0) dx2 = 1 ;
	    int longest = Math.abs(w) ;
	    int shortest = Math.abs(h) ;
	    if (!(longest>shortest)) {
	      longest = Math.abs(h) ;
	      shortest = Math.abs(w) ;
	      if (h<0) dy2 = -1 ; else if (h>0) dy2 = 1 ;
	      dx2 = 0 ;            
	    }
	    int numerator = longest >> 1 ;
	    for (int i=0;i<=longest;i++) {
	      points.add(new Point(x, y));
	      numerator += shortest ;
	      if (!(numerator<longest)) {
	        numerator -= longest ;
	        x += dx1 ;
	        y += dy1 ;
	      } else {
	        x += dx2 ;
	        y += dy2 ;
	      }
	    }

	    return points;
	  }

	  public void drawBresenham(int x, int y, int x2, int y2) {
	    ArrayList<Point> points = bresenhamLine(x, y, x2, y2);
	    Iterator<Point> itr = points.iterator();
	    while(itr.hasNext()) {
	      Point p = itr.next();
	      if(p.x == x && p.y == y) {
	        viewing[p.x][p.y] = true;
	      } else if(!obstructs[p.x][p.y]) {
	        viewing[p.x][p.y] = true;
	      } else {
	        viewing[p.x][p.y] = true;
	        return;
	      }
	    }
	  }

	  public void drawLine(double x, double y, double x2, double y2) {
	    double ex = Math.abs(x-x2), ey = Math.abs(y-y2);
	    int dx, dy;

	    // we want the step to be < 1 because we to guarantee a higher granularity
	    // than the maximum number of squares away from the origin
	    //
	    // if the range is 10, and we're pointing directly upward, we want at least 10 steps
	    // to make sure we hit every square between there (x2, y2) and here (x, y)
	    double step = 0.9f/(ex>ey ? ex : ey);
	    double time = 0.0f;

	    while(time < 1.0f) {
	      // use round() because casting to int causes it to round the target square down
	      // and run into walls to the left and top quicker
	      dx = (int) Math.round(lerp(x, x2, time));
	      dy = (int) Math.round(lerp(y, y2, time));

	      if(dx == x && dy == y) {
	        viewing[dx][dy] = true;
	        time += step;
	      } else if(dx >= 0 && dx < map_w && dy >= 0 && dy < map_h) {
	        if(!obstructs[dx][dy]) {
	          viewing[dx][dy] = true;
	        } else {
	          // return as soon as you hit an obstruction
	          viewing[dx][dy] = true;
	          return;
	        }
	        time += step;
	      } else {
	        // if the dx/dy is off the amap, stop the loop
	        return;
	      }
	    }
	  }
	  
	  double lerp(double a, double b, double t) {
		  return a + ((b-a) * t);
	  }

	  public void lineOfSight(int plx, int ply, int range) {
	    // the destination (x, y) is now a double so that we don't keep redrawing the same squares
	    double x1 = (double) plx, y1 = (double) ply, x2, y2;
	    viewing = getEmpty();


	    // we're going to sweep 1/8 of the unit circle
	    double sweep = (PI/4.0f);

	    // the step should be relative to the sweep amount and the view
	    // range, 2.2 seems to be the sweet spot for a range of 10
	    double step = sweep/(range*2.2f);

	    // the outer loop is cardinal directions: east, south, west, north
	    // REMEMBER: the unit circle is inverted in computers, PI/2 is the down direction
	    for(double cardinal=0;cardinal<TWO_PI;cardinal+=HALF_PI) {
	      // the direction loop specifies which way (clockwise or anticlockwise)
	      // that we will be sweeping to draw lines
	      for(int direction=-1;direction<2;direction+=2) {
	        double end = cardinal + (direction * sweep);

	        if(direction > 0) {
	          // sweep from a cardinal direction, clockwise
	          for(double f=cardinal;f<=end;f+=step) {
	            x2 = (range * Math.cos(f)) + x1;
	            y2 = (range * Math.sin(f)) + y1;
	            drawBresenham((int) Math.round(x1), (int) Math.round(y1), (int) Math.round(x2), (int) Math.round(y2));
	            //drawLine(x1, y1, x2, y2);
	          }
	        } else {
	          // sweep from a cardinal direction, anticlockwise
	          for(double f=cardinal;f>=end;f-=step) {
	            x2 = (range * Math.cos(f)) + x1;
	            y2 = (range * Math.sin(f)) + y1;
	            drawBresenham((int) Math.round(x1), (int) Math.round(y1), (int) Math.round(x2), (int) Math.round(y2));
	            //drawLine(x1, y1, x2, y2);
	          }
	        }
	      }
	    }
	  }

	  public void clear() {
	    this.loadPixels();
	    for(int i=0;i<this.pixels.length;i++) {
	      this.pixels[i] = 0xff000000;
	    }
	    this.updatePixels();
	  }

	  public void clearTile(int x, int y) {
	    int clearPixel = 0x00000000;
	    paintTile(x, y, clearPixel);
	  }

	  public void hazeTile(int x, int y) {
	    int hazePixel = 0xEE000000;
	    paintTile(x, y, hazePixel);
	  }

	  public void paintTile(int x, int y, int tile_col) {
	    int tx, ty, x_base, y_base;

	    x_base = x * GameConstants.TILE_W;
	    y_base = y * GameConstants.TILE_H;

	    this.loadPixels();
	    for(int i=GameConstants.TILE_W-1;i>=0;i--) {
	      for(int j=GameConstants.TILE_H-1;j>=0;j--) {
	        tx = x_base+i;
	        ty = y_base+j;
	        this.pixels[ty*this.width+tx] = tile_col;
	      }
	    }
	    this.updatePixels();
	  }

	  public void render() {
	    clear();
	    lineOfSight(playerX, playerY, range);
	    for(int i=0;i<map_w;i++) {
	      for(int j=0;j<map_h;j++) {
	        if(viewing[i][j]) {
	          visited[i][j] = true;
	          clearTile(i, j);
	        } else if(visited[i][j]) {
	          hazeTile(i, j);
	        }
	      }
	    }
	  }

	}