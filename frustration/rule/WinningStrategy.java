package frustration.rule;

import frustration.model.Player;
import frustration.model.Position;
import frustration.board.Board;

/**
 * Strategy for how the End square is resolved when a roll reaches or passes it.
 * This is the extension point for the two winning rules:
 *
 *   - Overshoot allowed: any roll that reaches or passes End wins.
 *   - Exact landing:     only an exact landing wins; an overshoot bounces the
 *                        piece back down the tail (and possibly around the board).
 */
public interface WinningStrategy {

    /**
     * Decide the final landing square for a roll that reached (or passed) the End.
     *
     * @param overshootSteps how many steps past the End the roll went (0 = exact).
     * @param board          the board, used to compute a bounce-back landing.
     */
    Position resolveEnd(Player player, int overshootSteps, Board board);

    /** One-line description of the rule. */
    String describe();
}
