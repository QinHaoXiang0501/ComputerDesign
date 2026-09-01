package frustration.model;

/**
 * A player in the game. Holds identity (colour + display name), home square,
 * current position and the running count of turns taken.
 *
 * Encapsulation: the position can be read via {@link #position()} (an immutable
 * {@link Position}, so safe to share). It can only be changed through
 * {@link #moveTo(Position)}, which is intended for the game engine.
 */
public final class Player {

    private final PlayerColor colour;
    private final String name;
    private final int home;      // board position number of the home square
    private Position position;
    private int plays;

    public Player(PlayerColor colour, int home) {
        this.colour = colour;
        this.name = colour.displayName();
        this.home = home;
        this.position = Position.home(home);
        this.plays = 0;
    }

    public PlayerColor colour() { return colour; }
    public String name() { return name; }

    /** The board position number of this player's home square. */
    public int home() { return home; }

    /** @return the player's current position (immutable, safe to share). */
    public Position position() { return position; }

    /** @return the number of turns this player has taken so far. */
    public int plays() { return plays; }

    /** Increment the running turn count (called once per turn by the engine). */
    public void incrementPlays() {
        plays++;
    }

    /**
     * Move the player to a new position. Intended to be called only by the game
     * engine; the position is otherwise read-only via the immutable {@link Position}.
     */
    public void moveTo(Position newPosition) {
        this.position = newPosition;
    }

    /**
     * Restore the player to a previously captured state (position and turn count).
     * Used by the undo feature to rewind a turn back to where it started.
     */
    public void restoreTo(Position newPosition, int plays) {
        this.position = newPosition;
        this.plays = plays;
    }
}
