package frustration.demo;

import frustration.game.GameConfig;
import frustration.game.GameFactory;
import frustration.game.Turn;
import frustration.game.UndoDecision;
import frustration.model.PlayerColor;

/**
 * Demonstrates the undo feature (Command + Memento + UndoManager):
 *
 *   Scenario 1 - undo and re-roll: Red undoes his first roll, the board is
 *                restored, and he rolls again.
 *   Scenario 2 - three-undo limit: Red keeps asking to undo his first turn, but
 *                the UndoManager caps him at 3 undos and forces the 4th roll to
 *                commit.
 */
public final class UndoDemo {

    public static void main(String[] args) {
        scenario1();
        System.out.println();
        scenario2();
    }

    private static void scenario1() {
        System.out.println("=== Undo demo 1: undo an unlucky roll and re-roll ===");

        GameConfig config = GameConfig.getInstance();
        config.reset().useFixedDice(12, 12, 7, 8);

        // Red undoes exactly once (his very first roll); every other turn commits.
        UndoDecision undoFirstRollOnce = new UndoDecision() {
            private boolean used = false;

            @Override
            public boolean shouldUndo(Turn turn) {
                if (turn.player().colour() == PlayerColor.RED && !used) {
                    used = true;
                    return true;
                }
                return false;
            }
        };

        GameFactory.createGame(config).playWith(undoFirstRollOnce);
    }

    private static void scenario2() {
        System.out.println("=== Undo demo 2: three-undo limit per player ===");

        GameConfig config = GameConfig.getInstance();
        config.reset().useFixedDice(5, 5, 5, 5, 5, 5, 5, 5, 5, 5);

        // Red asks to undo every roll while he is on his first turn (a turn whose
        // play number is 1). The UndoManager allows at most 3 undos, so the fourth
        // roll is committed and the turn finally stands.
        UndoDecision keepUndoingFirstTurn = new UndoDecision() {
            @Override
            public boolean shouldUndo(Turn turn) {
                return turn.player().colour() == PlayerColor.RED
                        && turn.player().plays() == 1;
            }
        };

        GameFactory.createGame(config).playWith(keepUndoingFirstTurn);
    }
}
