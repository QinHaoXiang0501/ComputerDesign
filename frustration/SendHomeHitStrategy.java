package frustration;

/**
 * Variation: a player who is landed on is sent back to their home square.
 */
public final class SendHomeHitStrategy implements HitStrategy {

    @Override
    public boolean shouldSendVictimHome(Player mover, Player victim, Position position) {
        return true;
    }

    @Override public String describe() {
        return "Player will be sent HOME when HIT";
    }
}
