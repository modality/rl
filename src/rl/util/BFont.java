package rl.util;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;

public class BFont {
  public PImage[] font;
  public int characters;
  PApplet pa;

  public BFont(PApplet pa, String filename) {
	this.pa = pa;
    PImage fontImage = pa.loadImage(filename);    
    int rows = (int) Math.floor(fontImage.height/GameConstants.TILE_H);
    int columns = (int) Math.floor(fontImage.width/GameConstants.TILE_W);

    characters = rows*columns;
    font = new PImage[characters];

    for(int j=0;j<rows;j++) {
      for(int i=0;i<columns;i++) {
        font[(j*columns)+i] = fontImage.get(i*GameConstants.TILE_W, j*GameConstants.TILE_H, GameConstants.TILE_W, GameConstants.TILE_H);
      }
    }
  }

  public PImage get(int index, int fg, int bg) {
    PImage msk = font[index];
    PImage img = pa.createImage(msk.width, msk.height, PConstants.RGB);
    msk.loadPixels();
    img.loadPixels();
    for(int i=0;i<msk.pixels.length;i++) {
      if(msk.pixels[i] == Colors.WHITE) {
        img.pixels[i] = fg;
      } else {
        img.pixels[i] = bg;
      }
    }
    return img;
  }
  
  public PImage get(int index, int fg) {
    PImage msk = font[index];
    PImage img = pa.createImage(msk.width, msk.height, PConstants.RGB);
    msk.loadPixels();
    img.loadPixels();
    for(int i=0;i<img.pixels.length;i++) {
      img.pixels[i] = fg;
    }
    img.mask(msk);
    return img;
  }
}