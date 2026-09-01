package frustration.game;

/**
 * Command pattern: a reversible operation on the game. A single turn is wrapped
 * as a command object so it can be executed and later undone by restoring a
 * saved snapshot.
 */
public interface TurnCommand {

    /**
     * Run the command: roll the dice, then commit the move and any resulting hit.
     *
     * @return true if this turn won the game.
     */
    boolean execute();

    /**
     * Undo the command, restoring the state that existed before it was executed.
     */
    void undo();
}
