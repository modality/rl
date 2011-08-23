package rl.util.ai;

import rl.world.object.NPC;
import rl.world.object.Player;

public class AttackPlayerAI implements AI {
  Player player;

  public AttackPlayerAI(Player p) {
    this.player = p;
  }

  public void action(NPC npc) {
    if(npc.canSee(player)) {
      npc.target = player;
      if(npc.nextTo(npc.target)) {
        npc.attack(npc.target);
      } else {
        npc.moveTo(npc.target.getPosition());
      }
    }
  }
}
