package frustration.model;

/**
 * The four kinds of location a {@link Position} can represent.
 *
 *   HOME  - a player's starting square (numbered like a board square)
 *   BOARD - a numbered square on the main (circular) loop
 *   TAIL  - a square on the player's private tail leading to the End
 *   END   - the finishing square
 */
public enum PositionType {
    HOME,
    BOARD,
    TAIL,
    END
}
