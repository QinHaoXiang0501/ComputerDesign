package frustration.game;

import frustration.model.Player;
import frustration.model.PlayerColor;
import frustration.board.Board;
import frustration.dice.Dice;
import frustration.rule.HitStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * The game engine. Runs the turn loop, alternates players, applies moves, resolves
 * hits and detects the winner. It depends only on the {@link Board}, {@link Dice}
 * and {@link HitStrategy} abstractions, so every rule variation is assembled for
 * it by the {@link GameFactory}.
 *
 * Each turn is wrapped as a {@link Turn} command that records a {@link TurnMemento}
 * snapshot; an optional {@link UndoDecision} lets a player undo a just-rolled turn
 * (before it is committed) through the {@link UndoManager}, which caps each player
 * at {@link UndoManager#DEFAULT_MAX_UNDOS} undos per game.
 *
 * A Game is single-use: call {@link #play()} (no undo),
 * {@link #playWith(UndoDecision)} (with undo), or {@link #simulate()} (silent,
 * returns the winner for win-rate statistics). Any number of players (2 or 4) is
 * supported via {@link PlayerManager}.
 */
public final class Game {

    private final Board board;
    private final Dice dice;
    private final HitStrategy hitStrategy;
    private final PlayerManager players;
    private final UndoManager undoManager;
    private Player winner = null;

    public Game(Board board, Dice dice, HitStrategy hitStrategy, PlayerColor... colours) {
        this.board = board;
        this.dice = dice;
        this.hitStrategy = hitStrategy;
        List<Player> list = new ArrayList<>();
        for (PlayerColor colour : colours) {
            list.add(new Player(colour, board.homeOf(colour, colours.length)));
        }
        this.players = new PlayerManager(list);
        this.undoManager = new UndoManager(list);
    }

    /** Play to completion with no undo, printing the configuration and every move. */
    public void play() {
        playWith(null);
    }

    /**
     * Play to completion, consulting {@code decision} after each roll to let the
     * player undo before the move is committed. Pass {@code null} to disable undo.
     */
    public void playWith(UndoDecision decision) {
        printConfiguration();
        if (decision != null) {
            System.out.println("Undo: enabled (up to " + UndoManager.DEFAULT_MAX_UNDOS
                    + " per player)");
        }
        Player w = run(true, decision);
        System.out.println(w.name() + " wins in " + w.plays() + " moves!");
        System.out.println("Total plays " + players.totalPlays());
    }

    /** Run the game silently (no output) and return the winner. For win-rate stats. */
    public Player simulate() {
        return run(false, null);
    }

    /** Print the board / players / rules / dice configuration. */
    public void printConfiguration() {
        System.out.println(board + " Players=" + playerList());
        System.out.println(board.winningRuleDescription());
        System.out.println(hitStrategy.describe());
        System.out.println("Dice: " + dice.describe());
    }

    private Player run(boolean verbose, UndoDecision decision) {
        while (winner == null) {
            Player current = players.current();

            Turn turn = new Turn(board, dice, hitStrategy, players, verbose);
            turn.roll();

            boolean undoRequested = decision != null && decision.shouldUndo(turn);
            if (undoRequested) {
                if (undoManager.canUndo(current)) {
                    undoManager.undo(turn);
                    if (verbose) {
                        System.out.println(current.name() + " undoes! "
                                + undoManager.remainingUndos(current) + " undos remaining.");
                    }
                    continue; // same player rolls again
                } else if (verbose) {
                    System.out.println(current.name()
                            + " has no undos left; move committed.");
                }
            }

            if (turn.commit()) {
                winner = current;
            } else {
                players.advance();
            }
        }
        return winner;
    }

    private String playerList() {
        StringBuilder sb = new StringBuilder("{");
        List<Player> list = players.players();
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(list.get(i).name());
        }
        return sb.append("}").toString();
    }
}
