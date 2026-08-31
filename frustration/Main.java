package frustration;

/**
 * Demonstrates the Singleton + Simple Factory wiring: configure the singleton
 * {@link GameConfig}, then ask the {@link GameFactory} for a ready-to-play game.
 *
 * Each demo uses a fixed dice sequence so its output exactly reproduces a worked
 * example from the assessment brief.
 */
public final class Main {

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
