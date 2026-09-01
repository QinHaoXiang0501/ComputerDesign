package frustration.game;

import frustration.board.Board;
import frustration.dice.Dice;
import frustration.model.MoveResult;
import frustration.model.Player;
import frustration.model.Position;
import frustration.rule.HitStrategy;

/**
 * Command-pattern object for a single turn: roll the dice, preview the landing
 * (without mutating the board), then on {@link #commit()} apply the move and any
 * resulting hit. A {@link TurnMemento} snapshot taken in the constructor lets
 * {@link #undo()} restore the state that existed before the turn.
 *
 * The two-phase design (roll, then commit) is what makes undo possible: the
 * player rolls, sees the result, and either confirms the move or asks the
 * {@link UndoManager} to undo before anything is committed.
 */
public final class Turn implements TurnCommand {

    private final Board board;
    private final Dice dice;
    private final HitStrategy hitStrategy;
    private final PlayerManager players;
    private final boolean verbose;

    private final Player player;
    private final TurnMemento memento;

    private int roll;
    private Position from;
    private Position landing;
    private boolean overshot;
    private boolean rolled = false;
    private boolean committed = false;

    public Turn(Board board, Dice dice, HitStrategy hitStrategy,
                PlayerManager players, boolean verbose) {
        this.board = board;
        this.dice = dice;
        this.hitStrategy = hitStrategy;
        this.players = players;
        this.verbose = verbose;
        this.player = players.current();
        this.memento = TurnMemento.capture(players); // snapshot BEFORE the roll
    }

    /** The player whose turn this command represents. */
    public Player player() {
        return player;
    }

    /** The dice value rolled (available after {@link #roll()}). */
    public int lastRoll() {
        return roll;
    }

    /** The square the piece would land on (available after {@link #roll()}). */
    public Position landing() {
        return landing;
    }

    /** Roll the dice and preview the landing, leaving the board unchanged. */
    public void roll() {
        if (rolled) {
            throw new IllegalStateException("already rolled");
        }
        rolled = true;
        roll = dice.roll();
        player.incrementPlays();
        from = player.position();
        MoveResult result = board.move(player, roll);
        landing = result.landing();
        overshot = result.overshot();

        if (verbose) {
            System.out.println(player.name() + " play " + player.plays() + " rolls " + roll);
            if (overshot) {
                System.out.println(player.name() + " overshoots!");
            }
            System.out.println(player.name() + " moves from " + from.label()
                    + " to " + landing.label());
        }
    }

    /** Apply the move (and any resulting hit). */
    public boolean commit() {
        if (!rolled) {
            throw new IllegalStateException("must roll before commit");
        }
        if (committed) {
            return false;
        }
        committed = true;
        if (landing.isEnd()) {
            return true; // game won: the piece is not moved any further
        }
        player.moveTo(landing);
        resolveHit();
        return false;
    }

    @Override
    public boolean execute() {
        roll();
        return commit();
    }

    @Override
    public void undo() {
        if (committed) {
            throw new IllegalStateException("cannot undo a committed turn");
        }
        memento.restore(players);
    }

    private void resolveHit() {
        if (!landing.isBoard()) {
            return; // tails are private, hits only occur on the shared loop
        }
        Player victim = players.findVictim(player, landing);
        if (victim == null) {
            return;
        }
        if (verbose) {
            System.out.println(victim.name() + " " + landing.label() + " hit!");
        }
        if (hitStrategy.shouldSendVictimHome(player, victim, landing)) {
            Position home = Position.home(victim.home());
            if (verbose) {
                System.out.println(victim.name() + " moves from " + landing.label()
                        + " to " + home.label());
            }
            victim.moveTo(home);
        }
    }
}
