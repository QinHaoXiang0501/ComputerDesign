package frustration.rule;

import frustration.model.Player;
import frustration.model.Position;

/**
 * Strategy for what happens when a player lands on a square occupied by another
 * player. The engine always announces the hit; this strategy decides the effect.
 */
public interface HitStrategy {

    /**
     * @return true if {@code victim} should be sent back to their home, or false
     *         if the hit is ignored (players simply share the square).
     */
    boolean shouldSendVictimHome(Player mover, Player victim, Position position);

    /** One-line description of the rule, e.g. "Player will be sent HOME when HIT". */
    String describe();
}
