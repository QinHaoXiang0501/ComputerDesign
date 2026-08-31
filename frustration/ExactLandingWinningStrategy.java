package frustration;

/**
 * Variation: the player must land exactly on the End to win. If the roll is too
 * high the piece bounces back off the End, going back down the tail and (if
 * necessary) anticlockwise around the main board.
 */
public final class ExactLandingWinningStrategy implements WinningStrategy {

    @Override
    public Position resolveEnd(Player player, int overshootSteps, Board board) {
        if (overshootSteps == 0) {
            return Position.END;                       // exact landing -> win
        }
        return board.bounceBack(player, overshootSteps); // overshoot -> back away
    }

    @Override public String describe() {
        return "Player must land exactly on the END position to win";
    }
}
