package frustration.demo;

import frustration.game.GameConfig;
import frustration.game.GameFactory;
import frustration.board.BoardType;

/**
 * Runs the five worked examples from the assessment brief, one after another.
 *
 * The command-line entry point is now {@link frustration.Main}; this class is kept
 * as a convenience to reproduce all the brief's example outputs at once.
 */
public final class Demos {

    public static void main(String[] args) {
        GameConfig config = GameConfig.getInstance();

        System.out.println("== Basic game (overshoot allowed, no hits, two dice) ==");
        config.reset().useFixedDice(12, 12, 7, 8);
        GameFactory.createGame(config).play();

        System.out.println("\n== Hit variation (victim sent home) ==");
        config.reset().enableHit().useFixedDice(8, 2, 3, 4, 9);
        GameFactory.createGame(config).play();

        System.out.println("\n== Exact-landing variation (overshoot bounces back) ==");
        config.reset().enableExactLanding().useFixedDice(12, 12, 7, 11, 3, 3);
        GameFactory.createGame(config).play();

        System.out.println("\n== Single-die variation ==");
        config.reset().useDiceCount(1).useFixedDice(6, 6, 6, 6, 3, 4, 3, 4);
        GameFactory.createGame(config).play();

        System.out.println("\n== Large board variation ==");
        config.reset().useBoard(BoardType.LARGE).useFixedDice(12, 12, 12, 12, 12, 12, 4, 5);
        GameFactory.createGame(config).play();
    }
}
