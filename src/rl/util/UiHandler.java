package rl.util;

import controlP5.ControlP5;
import controlP5.Textarea;

public class UiHandler {
  ControlP5 controlP5;
  Textarea messagesArea, hudArea;
  boolean showingMenu;
  public GameMenu currentMenu;
  
  public UiHandler(ControlP5 cp5) {
    controlP5 = cp5;
      
    messagesArea = controlP5.addTextarea(
      "message", 
      "Welcome to ???", 
      2, 482, 786, 116
    );
    messagesArea.setMoveable(false);
    messagesArea.setColorBackground(0xff333333);
    messagesArea.showScrollbar();

    hudArea = controlP5.addTextarea(
      "hud",
      "",
      640, 0, 160, 240
    );
    hudArea.setMoveable(false);
    hudArea.setColorBackground(0xff333333);
    hudArea.showScrollbar();
    
    showingMenu = false;
  }
  
  public void addMessage(String message) {
    if(message.length() > 0) {
      System.out.println(message);
      messagesArea.setText(message + "\n" + messagesArea.text());
    }
  }
  
  public void setHudText(String hud_text) {
    hudArea.setText(hud_text);
  }
  
  public void clearHudText() {
    hudArea.setText("");
  }
  
  public void showMenu(GameMenu gmenu) {
    showingMenu = true;
    currentMenu = gmenu;
    updateMenu();
  }
  
  public void updateMenu() {
    setHudText(currentMenu.display());
    addMessage(currentMenu.flushMessages());
  }
  
  public void clearMenu() {
    addMessage(currentMenu.flushMessages());
    currentMenu = null;
    clearHudText();
  }
  
  
}