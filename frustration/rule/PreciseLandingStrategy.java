package frustration.rule;

import frustration.model.Player;
import frustration.model.Position;
import frustration.board.Board;

/**
 * Variation 1: Precise Landing (exact landing to win).
 *
 * A player must throw the exact number of steps needed to reach the End. If the
 * roll is too high the piece overshoots and must bounce back off the End:
 *
 *   1. walk back down the tail (End -> T_{k-1} -> ... -> T1);
 *   2. then continue anticlockwise around the main board;
 *   3. until every excess step has been used up.
 *
 * The backward movement itself lives in {@link Board#bounceBack(Player, int)};
 * this strategy only decides "win" (exact) versus "bounce back" (overshoot).
 */
public final class PreciseLandingStrategy implements WinningStrategy {

    @Override
    public Position resolveEnd(Player player, int overshootSteps, Board board) {
        if (overshootSteps == 0) {
            return Position.END;                          // exact landing -> win
        }
        return board.bounceBack(player, overshootSteps);  // overshoot -> back away
    }

    @Override public String describe() {
        return "Player must land exactly on the END position to win";
    }
}
