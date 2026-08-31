package frustration;

/**
 * A player in the game. Holds identity (colour + display name), home square,
 * current position and the running count of turns taken.
 *
 * Encapsulation: the position can be read via {@link #position()} (an immutable
 * {@link Position}, so safe to share), but the only way to change it is the
 * package-private {@link #moveTo(Position)} method, reserved for the game engine.
 * External code cannot move a player directly.
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
     * Move the player to a new position. Package-private on purpose: only the game
     * engine (classes in this package) may mutate a player's position.
     */
    void moveTo(Position newPosition) {
        this.position = newPosition;
    }
}
