package frustration.game;

import frustration.model.Player;
import frustration.model.PlayerColor;
import frustration.model.Position;

import java.util.List;

/**
 * Manages the players of a game: the ordered list, whose turn it is, and locating
 * a player who occupies a given square (for the HIT rule). Supports any number of
 * players (2 or 4), so the game engine stays agnostic of the player count.
 */
public final class PlayerManager {

    private final List<Player> players;
    private int currentIndex = 0;

    public PlayerManager(List<Player> players) {
        this.players = players;
    }

    /** @return the ordered list of players. */
    public List<Player> players() {
        return players;
    }

    /** @return the player whose turn it currently is. */
    public Player current() {
        return players.get(currentIndex);
    }

    /** Advance to the next player (wraps around). */
    public void advance() {
        currentIndex = (currentIndex + 1) % players.size();
    }

    /** @return the index of the current player in the ordered list. */
    public int currentIndex() {
        return currentIndex;
    }

    /** Set the current player by index (used when undoing back to a player). */
    public void setCurrentIndex(int index) {
        this.currentIndex = index;
    }

    /** @return the total number of turns taken by all players. */
    public int totalPlays() {
        int total = 0;
        for (Player p : players) total += p.plays();
        return total;
    }

    /**
     * Find another player occupying {@code landing}. Only a piece actively on the
     * board can be hit; a piece sitting on its Home square is safe (its home is
     * occupied only by itself), so it is never returned as a victim.
     */
    public Player findVictim(Player mover, Position landing) {
        int square = landing.value();
        for (Player p : players) {
            if (p == mover) continue;
            Position pos = p.position();
            if (pos.isBoard() && pos.value() == square) {
                return p;
            }
        }
        return null;
    }
}
