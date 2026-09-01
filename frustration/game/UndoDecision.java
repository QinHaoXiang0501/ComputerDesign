package frustration.game;

/**
 * Decides, after a turn has been rolled but before it is committed, whether the
 * current player should undo and re-roll. The default game never undoes; a
 * driver (a test, a demo, or an interactive UI) injects this hook to enable undo.
 *
 * <p>The hook can inspect the rolled turn - e.g. {@link Turn#lastRoll()} or
 * {@link Turn#landing()} - to implement a policy such as "undo an unlucky roll".
 */
@FunctionalInterface
public interface UndoDecision {

    /**
     * @param turn the just-rolled turn (its roll and landing are readable).
     * @return true to undo the turn (if the player still has credits), false to
     *         commit it.
     */
    boolean shouldUndo(Turn turn);
}
