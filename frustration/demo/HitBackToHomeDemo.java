package frustration.demo;

import frustration.game.GameConfig;
import frustration.game.GameFactory;

/**
 * Focused demonstration of Variation 2 (HIT): the piece landed on is sent back to
 * its own Home. Also shows Rule 5 - a player on their own Home square is safe.
 */
public final class HitBackToHomeDemo {

    public static void main(String[] args) {
        GameConfig config = GameConfig.getInstance();

        System.out.println("== HIT: the victim is sent back to Home ==");
        config.reset().enableHit().useFixedDice(8, 2, 3, 4, 9);
        GameFactory.createGame(config).play();

        System.out.println("\n== Rule 5: a player on their Home square is safe ==");
        config.reset().enableHit().useFixedDice(9, 12, 8, 7, 2, 8, 3);
        GameFactory.createGame(config).play();
    }
}
