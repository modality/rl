package rl.world.events;

import rl.world.object.NPC;

import java.util.UUID;

public class NPCEvent implements GameEvent {
  public UUID id;

  public NPCEvent(NPC npc) {
    this.id = npc.id;
  }

  public boolean is(String what) {
    return what.equals("NPCEvent");
  }
}