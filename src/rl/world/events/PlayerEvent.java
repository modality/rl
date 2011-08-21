package rl.world.events;

public class PlayerEvent implements GameEvent {
  public PlayerEvent() { }

  public boolean is(String what) {
    return what.equals("PlayerEvent");
  }
}