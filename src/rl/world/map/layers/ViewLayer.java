package rl.world.map.layers;

import processing.core.PConstants;
import processing.core.PImage;
import rl.RogueLike;
import rl.util.GameConstants;

public abstract class ViewLayer extends PImage {
  public boolean updated;
  public RogueLike rl;
  
  public ViewLayer() {}

  public ViewLayer(RogueLike rl, int width, int height) {
    super(width * GameConstants.TILE_W, height * GameConstants.TILE_H, PConstants.ARGB);
    this.rl = rl;
    updated = false;
  }
  
  public void copyToTile(PImage img, int x, int y) {
    this.copy(
      img,
      0, 0, img.width, img.height,
      x * GameConstants.TILE_W, y * GameConstants.TILE_H, img.width, img.height
    ); 
  }
  
  public abstract void render();
  
  public void clear() {
    this.mask(new PImage(this.width, this.height));
  }
  
  public void draw() {
    if(updated) {
      render();
      updated = false;
    }
    rl.image(this, 0, 0);
  }
  
}