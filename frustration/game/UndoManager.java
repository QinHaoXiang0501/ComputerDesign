package frustration.game;

import frustration.model.Player;
import frustration.model.PlayerColor;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The undo caretaker. It gives every player a limited number of undo credits
 * (default 3 per game) and applies an undo by restoring the {@link Turn}'s
 * {@link TurnMemento} snapshot. Capping the credits stops a player from retrying
 * a turn indefinitely.
 */
public final class UndoManager {

    /** How many times each player may undo per game. */
    public static final int DEFAULT_MAX_UNDOS = 3;

    private final Map<PlayerColor, Integer> remaining;

    public UndoManager(List<Player> players) {
        this(players, DEFAULT_MAX_UNDOS);
    }

    public UndoManager(List<Player> players, int maxUndos) {
        remaining = new LinkedHashMap<>();
        for (Player p : players) {
            remaining.put(p.colour(), maxUndos);
        }
    }

    /** @return true if {@code player} may still undo. */
    public boolean canUndo(Player player) {
        return remainingUndos(player) > 0;
    }

    /** @return how many undo credits {@code player} has left. */
    public int remainingUndos(Player player) {
        return remaining.getOrDefault(player.colour(), 0);
    }

    /**
     * Undo {@code turn} and consume one of that player's credits.
     *
     * @throws IllegalStateException if the player has no credits left.
     */
    public void undo(Turn turn) {
        Player player = turn.player();
        int left = remainingUndos(player);
        if (left <= 0) {
            throw new IllegalStateException(player.name() + " has no undos left");
        }
        turn.undo();
        remaining.put(player.colour(), left - 1);
    }
}
