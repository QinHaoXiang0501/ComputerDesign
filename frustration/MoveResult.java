package frustration;

/**
 * The outcome of a single move computed by the {@link Board}: the landing square
 * and whether the roll overshot (passed) the End.
 *
 * Whether the move won the game is simply {@code landing().isEnd()}; the
 * {@code overshot} flag drives the optional "X overshoots!" output line.
 */
public final class MoveResult {

    private final Position landing;
    private final boolean overshot;

    public MoveResult(Position landing, boolean overshot) {
        this.landing = landing;
        this.overshot = overshot;
    }

    /** @return where the piece ends up after this move. */
    public Position landing() { return landing; }

    /** @return true if the roll passed the End (roll > distance to End). */
    public boolean overshot() { return overshot; }
}
