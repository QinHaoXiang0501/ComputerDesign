package frustration.rule;

import frustration.model.Player;
import frustration.model.Position;

/**
 * Variation 2: HIT - the piece that is landed on is sent back to its own Home.
 *
 * Only the victim is affected, never the moving piece. Home squares are safe:
 * a player sitting on their own Home cannot be hit (the engine skips them, since
 * a Home square is occupied only by its owner). Each tail is private to one
 * player, so collisions can only happen on the shared main loop.
 */
public final class HitBackToHomeStrategy implements HitStrategy {

    @Override
    public boolean shouldSendVictimHome(Player mover, Player victim, Position position) {
        return true; // the victim (the piece landed on) is sent home
    }

    @Override public String describe() {
        return "Player will be sent HOME when HIT";
    }
}
