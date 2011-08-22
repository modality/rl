package rl.world.map.layers;

import java.util.ArrayList;
import java.util.Iterator;

import rl.RogueLike;
import rl.util.BFont;
import rl.util.GameConstants;
import rl.util.Point;
import rl.world.map.GameMap;
import rlforj.los.ILosAlgorithm;
import rlforj.los.ILosBoard;
import rlforj.los.PrecisePermissive;

public class FOVLayer extends ViewLayer implements ILosBoard {
	  int map_w, map_h;
	  boolean[][] obstructs, viewing, visited;
	  int playerX, playerY, range;
	  PrecisePermissive algo;

	  public FOVLayer(RogueLike rl, GameMap gmap) {
	    super(rl, gmap.map_w, gmap.map_h);
	    
	    this.map_w = gmap.map_w;
	    this.map_h = gmap.map_h;
	    this.obstructs = new boolean[map_w][map_h];
	    this.visited = new boolean[map_w][map_h];
	    this.playerX = 0;
	    this.playerY = 0;
	    this.range = 10;

	    this.algo = new PrecisePermissive();

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
	    int hazePixel = 0x77000000;
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
	    viewing = getEmpty();
	    algo.visitFieldOfView(this, playerX, playerY, range);
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

    @Override
    public boolean isObstacle(int x, int y) {
      return obstructs[x][y];
    }

    @Override
    public void visit(int x, int y) {
      visited[x][y] = true;
      viewing[x][y] = true;
    }

    @Override
    public boolean contains(int x, int y) {
      return (x >= 0 && x < map_w && y >= 0 && y < map_h);
    }

	}