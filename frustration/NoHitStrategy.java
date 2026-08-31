package frustration;

/**
 * Basic game rule: multiple players may occupy the same square. A hit has no
 * effect (the engine still prints the "hit!" notification, matching the brief).
 */
public final class NoHitStrategy implements HitStrategy {

    @Override
    public boolean shouldSendVictimHome(Player mover, Player victim, Position position) {
        return false;
    }

    @Override public String describe() {
        return "HITS are ignored, multiple players can occupy the same position";
    }
}
