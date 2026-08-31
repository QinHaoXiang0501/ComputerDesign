package frustration;

/**
 * Basic game rule: landing on or beyond the End wins (an overshoot is allowed).
 */
public final class OvershootWinningStrategy implements WinningStrategy {

    @Override
    public Position resolveEnd(Player player, int overshootSteps, Board board) {
        // Reaching or passing the End always wins.
        return Position.END;
    }

    @Override public String describe() {
        return "Player can land on or beyond the END position to win";
    }
}
