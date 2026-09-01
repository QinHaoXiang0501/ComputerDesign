package frustration.model;

import java.util.Objects;

/**
 * An immutable value object describing a single square on the board.
 *
 * The meaning of {@link #value()} depends on the {@link PositionType}:
 *   HOME  -> the board position number of the home (e.g. 1, 10)
 *   BOARD -> the board position number (1..boardSize)
 *   TAIL  -> the tail offset (1..tailLength-1)
 *   END   -> unused
 *
 * Because Position is immutable it can safely be returned from {@link Player}
 * without exposing any way to mutate the player's location.
 */
public final class Position {

    /** The single shared End position. */
    public static final Position END = new Position(PositionType.END, 0);

    private final PositionType type;
    private final int value;

    private Position(PositionType type, int value) {
        this.type = type;
        this.value = value;
    }

    /** A player's home square on the main board. */
    public static Position home(int boardPosition) {
        return new Position(PositionType.HOME, boardPosition);
    }

    /** A numbered square on the main board. */
    public static Position board(int boardPosition) {
        return new Position(PositionType.BOARD, boardPosition);
    }

    /** A square on the tail (offset 1..tailLength-1). */
    public static Position tail(int tailOffset) {
        return new Position(PositionType.TAIL, tailOffset);
    }

    public PositionType type() {
        return type;
    }

    public int value() {
        return value;
    }

    public boolean isHome()  { return type == PositionType.HOME; }
    public boolean isBoard() { return type == PositionType.BOARD; }
    public boolean isTail()  { return type == PositionType.TAIL; }
    public boolean isEnd()   { return type == PositionType.END; }

    /**
     * The label used in move output, e.g. "HOME (Position 1)", "Position 13",
     * "TAIL (Tail Position 2)" or "END".
     */
    public String label() {
        switch (type) {
            case HOME:  return "HOME (Position " + value + ")";
            case BOARD: return "Position " + value;
            case TAIL:  return "TAIL (Tail Position " + value + ")";
            case END:   return "END";
            default:    return "?";
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position)) return false;
        Position p = (Position) o;
        return value == p.value && type == p.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, value);
    }

    @Override
    public String toString() {
        return label();
    }
}
