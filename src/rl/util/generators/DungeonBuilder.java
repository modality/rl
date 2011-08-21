package rl.util.generators;

import rl.util.Chance;
import rl.world.map.GameMap;
import rl.util.Point;
import rl.world.object.Terrain;

public class DungeonBuilder {
  GameMap gmap;
  int totalBuilds, failedBuilds;
  Terrain defaultTerrain;


  public DungeonBuilder(GameMap gmap) {
    this.gmap = gmap;
    this.totalBuilds = 0;
    this.failedBuilds = 0;
    this.defaultTerrain = gmap.terras.WALL;
  }

  public void build() {
    gmap.setAll(defaultTerrain);
    buildStartRoom();
    while(failedBuilds < 120) {
      buildFeature();
    }
  }

  public void buildFeature() {
    Point wall = findWallSquare();

    int feature = Chance.rollDie(1, 100);

    if(feature < 35) {
      buildHall(wall, Chance.rollDie(1, 6)+2, 1);
    } else if (feature < 65) {
      buildRoom(wall, Chance.rollDie(2, 3)+2, Chance.rollDie(2, 3)+2);
    } else if (feature < 90) { 
      buildRoom(wall, Chance.rollDie(3, 4)+3, Chance.rollDie(3, 4)+3);
    } else {
      buildHall(wall, Chance.rollDie(1, 6)+2, 2);
    }
  }

  public void buildStartRoom() {
    fillRect(gmap.terras.EMPTY, (int) Math.floor(gmap.map_w/2)-1, (int) Math.floor(gmap.map_h/2)-1, 4, 4);
    gmap.terrain[(int) Math.floor(gmap.map_w/2)][(int) Math.floor(gmap.map_h/2)] = gmap.terras.START;
  }

  public void buildHall(Point wall, int hallSize, int hallWidth) {

    if(wall.facing("N")) {
      if(scanArea(wall.x-1, wall.y-hallSize, 2+hallWidth, hallSize+1)) {
        fillRect(gmap.terras.EMPTY, wall.x, wall.y-hallSize+1, hallWidth, hallSize);
        success();
        return;
      }
    } else if (wall.facing("S")) {
      if(scanArea(wall.x-1, wall.y, 2+hallWidth, hallSize+1)) {
        fillRect(gmap.terras.EMPTY, wall.x, wall.y, hallWidth, hallSize);
        success();
        return;
      }
    } else if (wall.facing("E")) {
      if(scanArea(wall.x, wall.y-1, hallSize+1, 2+hallWidth)) {
        fillRect(gmap.terras.EMPTY, wall.x, wall.y, hallSize, hallWidth);
        success();
        return;
      }
    } else if (wall.facing("W")) {
      if(scanArea(wall.x-hallSize, wall.y-1, hallSize+1, 2+hallWidth)) {
        fillRect(gmap.terras.EMPTY, wall.x-hallSize+1, wall.y, hallSize, hallWidth);
        success();
        return;
      }
    }

    failedBuilds++;
  }

  public void buildRoom(Point wall, int roomW, int roomH) {
    int roomX = 0, roomY = 0;

    if(wall.facing("N")) {
      roomX = wall.x - (roomW/2);
      roomY = wall.y - roomH;
    } else if (wall.facing("S")) {
      roomX = wall.x - (roomW/2);
      roomY = wall.y + 1;
    } else if (wall.facing("E")) {
      roomX = wall.x + 1;
      roomY = wall.y - (roomH/2);
    } else if (wall.facing("W")) {
      roomX = wall.x - roomW;
      roomY = wall.y - (roomH/2);
    }

    if(scanArea(roomX - 1, roomY - 1, roomW+2, roomH+2)) {
      fillRect(gmap.terras.EMPTY, roomX, roomY, roomW, roomH);
      fillRect(gmap.terras.DOOR, wall.x, wall.y, 1, 1);

      if(roomW >= 8 && roomH >= 8) {
        fillRoom(wall, roomX, roomY, roomW, roomH);
      }
      success();
      return;
    }

    failedBuilds++;
  }

  public void fillRoom(Point wall, int roomX, int roomY, int roomW, int roomH) {
    int filling = Chance.rollDie(1, 7);

    // need a big fucking room for filling 6
    while(filling > 1 && filling < 6) {
      filling = Chance.rollDie(1, 7);
      if(filling == 6 && roomW > 7 && roomH > 7) {
        filling = 2;
      }
    }

    switch(filling) {
      case 1: // dividing wall
        boolean vertical = Chance.flipCoin();
        int funPoint = Chance.rollDie(1, (vertical?roomW:roomH)-4)+1;
        if(wall.facing("N") || wall.facing("S")) {
          while(funPoint+roomX == wall.x && vertical) {
            vertical = Chance.flipCoin();
            funPoint = Chance.rollDie(1, (vertical?roomW:roomH)-4)+1;
          }
        } else {
          while(funPoint+roomY == wall.y && !vertical) {
            vertical = Chance.flipCoin();
            funPoint = Chance.rollDie(1, (vertical?roomW:roomH)-4)+1;
          }
        }

        if(vertical) {
          fillRect(gmap.terras.WALL, roomX+funPoint, roomY, 1, roomH);
          setTerrain(gmap.terras.DOOR, roomX+funPoint, roomY+Chance.rollDie(1, roomH-1));
        } else {
          fillRect(gmap.terras.WALL, roomX, roomY+funPoint, roomW, 1);
          setTerrain(gmap.terras.DOOR, roomX+Chance.rollDie(1, roomW-1), roomY+funPoint);
        }
        break;
      case 2: // closet
        boolean north = Chance.flipCoin(), west = Chance.flipCoin();
        int hPivot = Chance.rollDie(1, roomW-4)+2, vPivot = Chance.rollDie(1, roomH-4)+2;

        if(((wall.facing("N") && north) || (wall.facing("S") && !north)) && ((hPivot + roomX-1) == wall.x)) {
          north = !north;
        } else if (((wall.facing("W") && west) || (wall.facing("E") && !west))  && ((vPivot + roomY-1) == wall.y)) {
          west = !west;
        }

        boolean horizSideDoor = Chance.flipCoin();

        if(north && west) {
          drawRect(gmap.terras.WALL, roomX-1, roomY-1, hPivot+1, vPivot+1, true);
          setTerrain(gmap.terras.DOOR, Chance.from(roomX, roomX+hPivot-1), roomY+vPivot-1);
        } else if (north && !west) {
          drawRect(gmap.terras.WALL, roomX-1 + hPivot, roomY-1, roomW-hPivot+2, vPivot+1, true);
          setTerrain(gmap.terras.DOOR, Chance.from(roomX+hPivot+1, roomX+roomW-1), roomY+vPivot-1);
        } else if(!north && west) {
          drawRect(gmap.terras.WALL, roomX-1, roomY-1 + vPivot, hPivot+1, roomH-vPivot+2, true);
          if(horizSideDoor) {
            setTerrain(gmap.terras.DOOR, Chance.from(roomX+hPivot+1, roomX+roomW-1), roomY-1+vPivot);
          } else {

          }
        } else if(!north && !west) {
          drawRect(gmap.terras.WALL, roomX-1 + hPivot, roomY-1 + vPivot, roomW-hPivot+2, roomH-vPivot+2, true);
          if(horizSideDoor) {

          } else {

          }
        }
        break;
      case 3: // L shaped

        break;
      case 4: // T or Z shaped

        break;
      case 5: // U shaped

        break;
      case 6: // inner enclosure
        buildEnclosure(roomX, roomY, roomW, roomH);
        break;
      case 7: // puddle
        fillCircle(gmap.terras.PUDDLE, Chance.from(roomX-1, roomX+roomW), Chance.from(roomY-1, roomY+roomH), Chance.rollDie(1, 5)+2, true);
        break;
      default: break;
    }
  }

  public void buildEnclosure(int roomX, int roomY, int roomW, int roomH) {
    int doorX = 0, doorY = 0;
    int lBound = roomX+2, tBound = roomY+2,
        rBound = roomX+roomW-3, bBound = roomY+roomH-3;

    Terrain wall = gmap.terras.WALL;
    switch(Chance.rollDie(1, 2)) {
      case 1: wall = gmap.terras.RUSTY; break;
      default: wall = gmap.terras.WALL; break;
    }

    fillRect(wall, roomX+2, roomY+2, roomW-4, roomH-4);
    fillRect(gmap.terras.EMPTY, roomX+3, roomY+3, roomW-6, roomH-6);

    while((doorX == lBound || doorX == rBound) && (doorY == tBound || doorY == bBound) || (doorX != lBound && doorX != rBound && doorY != tBound && doorY != bBound)) {
      doorX = Chance.from(lBound, rBound);
      doorY = Chance.from(tBound, bBound);
    }
    setTerrain(gmap.terras.DOOR, doorX, doorY);
  }

  public void success() {
    totalBuilds++;
    failedBuilds = 0;
  }

  public void fillRect(Terrain t, int x, int y, int w, int h) {
    for(int i=x; i<(x+w); i++) {
      for(int j=y; j<(y+h); j++) {
        gmap.terrain[i][j] = t;
      }
    }
  }

  public void drawRect(Terrain t, int x, int y, int w, int h, boolean onlyEmpty) {
    for(int i=x; i<(x+w); i++) {
      for(int j=y; j<(y+h); j++) {
        if(i == x || i == (x+w-1) || j == y || j == (y+h-1)) {
          gmap.terrain[i][j] = t;
        }
      }
    }
  }

  public void fillCircle(Terrain t, int x, int y, int r, boolean onlyEmpty) {
    for(int i=0;i<gmap.map_w;i++) {
      for(int j=0;j<gmap.map_h;j++) {
        if(Math.pow(i-x,2) + Math.pow(j-y,2) <= r) {
          if(gmap.terrain[i][j] == gmap.terras.EMPTY || !onlyEmpty) {
            gmap.terrain[i][j] = t;
          }
        }
      }
    }
  }

  public void setTerrain(Terrain t, int x, int y) {
    gmap.terrain[x][y] = t;
  }

  public boolean scanArea(int x, int y, int w, int h) {
    if(x < 0 || y < 0) {
      return false;
    }

    if(x+w >= gmap.map_w || y+h > gmap.map_h) {
      return false;
    }

    for(int i=x; i<(x+w); i++) {
      for(int j=y; j<(y+h); j++) {
        if(gmap.terrain[i][j] != defaultTerrain) {
          return false;
        }
      }
    }
    return true;
  }

  public Point findWallSquare() {
    boolean foundSquare = false;
    Point wall = new Point(0, 0);

    while(!foundSquare) {
      int x = (int) Math.floor(Math.random()*gmap.map_w), y = (int) Math.floor(Math.random()*gmap.map_h);
      if(gmap.terrain[x][y] == defaultTerrain) {
        if(x > 0 && gmap.terrain[x-1][y] == gmap.terras.EMPTY) {
          return new Point(x, y, "E");
        } else if (x < gmap.map_w-1 && gmap.terrain[x+1][y] == gmap.terras.EMPTY) {
          return new Point(x, y, "W");
        } else if (y > 0 && gmap.terrain[x][y-1] == gmap.terras.EMPTY) {
          return new Point(x, y, "S");
        } else if (y < gmap.map_h-1 && gmap.terrain[x][y+1] == gmap.terras.EMPTY) {
          return new Point(x, y, "N");
        }
      }
    }

    // shouldn't ever get here
    return wall;
  }

}