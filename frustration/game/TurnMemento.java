package frustration.game;

import frustration.model.Player;
import frustration.model.Position;

import java.util.List;

/**
 * Memento: an immutable snapshot of the game state at the start of a single turn,
 * captured so that turn can be undone.
 *
 * It records every player's position and turn count - not just the current
 * player's - because a HIT can move a *different* player back home, so the whole
 * table must be saved. It also records whose turn it was.
 *
 * A {@link Turn} captures one of these before rolling, and {@link Turn#undo()}
 * writes it back through {@link #restore(PlayerManager)}.
 */
public final class TurnMemento {

    private final Position[] positions;
    private final int[] plays;
    private final int currentIndex;

    private TurnMemento(Position[] positions, int[] plays, int currentIndex) {
        this.positions = positions;
        this.plays = plays;
        this.currentIndex = currentIndex;
    }

    /** Capture the current state of every player in {@code players}. */
    public static TurnMemento capture(PlayerManager players) {
        List<Player> list = players.players();
        int n = list.size();
        Position[] positions = new Position[n];
        int[] plays = new int[n];
        for (int i = 0; i < n; i++) {
            Player p = list.get(i);
            positions[i] = p.position();
            plays[i] = p.plays();
        }
        return new TurnMemento(positions, plays, players.currentIndex());
    }

    /** Write the saved state back into {@code players}. */
    public void restore(PlayerManager players) {
        List<Player> list = players.players();
        for (int i = 0; i < list.size(); i++) {
            list.get(i).restoreTo(positions[i], plays[i]);
        }
        players.setCurrentIndex(currentIndex);
    }
}
