package frustration.board;

import frustration.model.Player;
import frustration.model.PlayerColor;
import frustration.model.Position;
import frustration.model.MoveResult;
import frustration.rule.WinningStrategy;

/**
 * The board. Owns the geometry of the game:
 *
 *   - the mapping between the circular main loop and each player's private tail;
 *   - the square just before a player's home, where they divert up their tail;
 *   - forward movement (with wrap-around on the main loop) and backward movement
 *     (used by the exact-landing bounce-back).
 *
 * The board is parameterized entirely by its {@link BoardConfiguration}, so the
 * same class drives both the standard (18/3) and large (36/6) layouts without
 * any hard-coded sizes.
 *
 * Movement is a pure function here: {@link #move(Player, int)} returns a
 * {@link MoveResult} and never mutates the player; the {@link frustration.game.Game}
 * applies it. The End-resolution policy is delegated to a {@link WinningStrategy}.
 */
public final class Board {

    private final BoardConfiguration configuration;
    private final WinningStrategy winningStrategy;

    public Board(BoardConfiguration configuration, WinningStrategy winningStrategy) {
        this.configuration = configuration;
        this.winningStrategy = winningStrategy;
    }

    public int boardSize()  { return configuration.boardSize(); }
    public int tailLength() { return configuration.tailLength(); }
    public int homeOf(PlayerColor colour, int playerCount) { return configuration.homeOf(colour, playerCount); }

    /** The rule description, exposed for the configuration header. */
    public String winningRuleDescription() {
        return winningStrategy.describe();
    }

    /**
     * The board square immediately before a player's home: once the player reaches
     * this square they divert up their private tail.
     * Red home 1 -> 18 ; Blue home 10 -> 9 (for the standard board).
     */
    public int divertPosition(int home) {
        int n = boardSize();
        return ((home - 2 + n) % n) + 1;
    }

    /**
     * Compute where a roll takes the player from their current square.
     * Pure: does not mutate the player.
     */
    public MoveResult move(Player player, int roll) {
        Position from = player.position();
        if (from.isTail()) {
            return moveFromTail(player, from.value(), roll);
        }
        // HOME and BOARD are both squares on the main loop.
        return moveFromBoard(player, from.value(), roll);
    }

    private MoveResult moveFromBoard(Player player, int p, int roll) {
        int n = boardSize();
        int d = divertPosition(player.home());
        int stepsToDivert = ((d - p) % n + n) % n;      // 0 if already at the divert square
        int distanceToEnd = stepsToDivert + tailLength();

        if (roll <= stepsToDivert) {
            int landing = ((p - 1 + roll) % n) + 1;      // stays on the main loop (may wrap)
            return new MoveResult(Position.board(landing), false);
        }
        if (roll < distanceToEnd) {
            int tailOffset = roll - stepsToDivert;       // 1..tailLength-1
            return new MoveResult(Position.tail(tailOffset), false);
        }
        // reached or passed the End
        int overshootSteps = roll - distanceToEnd;
        boolean overshot = overshootSteps > 0;
        Position landing = winningStrategy.resolveEnd(player, overshootSteps, this);
        return new MoveResult(landing, overshot);
    }

    private MoveResult moveFromTail(Player player, int tailOffset, int roll) {
        int k = tailLength();
        int stepsToEnd = k - tailOffset;                 // steps remaining to reach End
        int next = tailOffset + roll;

        if (next < k) {
            return new MoveResult(Position.tail(next), false);
        }
        // reached or passed the End
        int overshootSteps = next - k;
        boolean overshot = overshootSteps > 0;
        Position landing = winningStrategy.resolveEnd(player, overshootSteps, this);
        return new MoveResult(landing, overshot);
    }

    /**
     * Bounce a player backward {@code steps} squares from the End (used by the
     * Precise Landing rule when a roll overshoots):
     *   - walk back down the tail (End -> T_{k-1} -> ... -> T1);
     *   - then continue anticlockwise around the main loop from the divert square;
     *   - keep going until all {@code steps} are used up (the modulo wrap handles
     *     any number of full laps automatically).
     */
    public Position bounceBack(Player player, int steps) {
        int k = tailLength();
        int n = boardSize();
        int d = divertPosition(player.home());

        if (steps < k) {
            return Position.tail(k - steps);             // still on the tail
        }
        int extra = steps - k;                           // steps back around the main loop
        int p = ((d - 1 - extra) % n + n) % n + 1;
        return Position.board(p);
    }

    @Override
    public String toString() {
        return configuration.describe();
    }
}
