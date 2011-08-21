package rl.world.map.layers;
import processing.core.PImage;

import rl.RogueLike;
import rl.util.GameConstants;
import rl.util.Point;
import rl.util.Colors;
import rl.world.map.layers.ViewLayer;


public class CursorLayer extends ViewLayer {
  PImage cursorImg;
  RogueLike rl;
  public int cursorX, cursorY;
  int playerX, playerY;
  int ticks;
  boolean cursorOn;
  int mode;
  
  public CursorLayer(RogueLike rl, int width, int height) {
    super(rl, width, height);
    this.rl = rl;
    cursorImg = rl.bfont.get('X', Colors.YELLOW);
    cursorX = 0;
    cursorY = 0;
    playerX = 0;
    playerY = 0;
    ticks = 0;
    cursorOn = true;
    mode = GameConstants.MODE_WALK;
  }

  public void setMode(int mode) {
    this.mode = mode;
  }

  public Point getPosition() {
    return new Point(cursorX, cursorY);
  }
  
  public void setPosition(int x, int y) {
    cursorX = x;
    cursorY = y;
    reset();
  }

  public void setPlayerPosition(int x, int y) {
    playerX = x;
    playerY = y;
    setPosition(x, y);
  }

  public void reset() {
    cursorOn = true;
    ticks = 0;
    updated = true;
  }
  
  public void render() {
    clear();
    if(cursorOn) {
      this.copyToTile(cursorImg, cursorX, cursorY);
    }
  }
  
  public void draw() {
    ticks += 1;
    if(ticks >= 9) {
      ticks = 0;
      cursorOn = !cursorOn;
      updated = true;
    }
    
    if(updated) {
      render();
      updated = false;
    }
    rl.image(this, 0, 0);    
  }
  
  public void handleInput(int key, int keyCode) {
    if(mode == GameConstants.MODE_LOOK) {
      if(key == CODED) {
        if(keyCode == UP) {
          setPosition(cursorX, cursorY - 1);
        }
        else if (keyCode == DOWN) {
          setPosition(cursorX, cursorY + 1);
        }
        else if (keyCode == RIGHT) {
          setPosition(cursorX + 1, cursorY);
        }
        else if (keyCode == LEFT) {
          setPosition(cursorX - 1, cursorY);
        }
      }
    } else if (mode == GameConstants.MODE_ATTACK) {
      if(key == CODED) {
        if(keyCode == UP) {
          if(cursorY >= playerY) {
            setPosition(cursorX, cursorY - 1);
          }
        }
        else if (keyCode == DOWN) {
          if(cursorY <= playerY) {
            setPosition(cursorX, cursorY + 1);
          }
        }
        else if (keyCode == RIGHT) {
          if(cursorX <= playerX) {
            setPosition(cursorX + 1, cursorY);
          }
        }
        else if (keyCode == LEFT) {
          if(cursorX >= playerX) {
            setPosition(cursorX - 1, cursorY);
          }
        }
      } 
    }
  }
}