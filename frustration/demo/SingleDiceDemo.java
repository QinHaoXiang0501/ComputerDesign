package frustration.demo;

import frustration.dice.DiceRollingStrategy;
import frustration.dice.TwoDiceRollingStrategy;
import frustration.dice.SingleDiceStrategy;
import frustration.game.GameConfig;
import frustration.game.GameFactory;

/**
 * Focused demonstration of Variation 3 (Single Die): a single six-sided die rolls
 * 1..6, swappable with the two-dice strategy, and freely combinable with the
 * other variations.
 */
public final class SingleDiceDemo {

    public static void main(String[] args) {
        System.out.println("Two dice: " + new TwoDiceRollingStrategy().describe());
        System.out.println("Single die: " + new SingleDiceStrategy().describe());

        DiceRollingStrategy die = new SingleDiceStrategy();
        System.out.print("Sample rolls: ");
        for (int i = 0; i < 8; i++) {
            System.out.print(die.roll() + " ");
        }
        System.out.println();

        System.out.println("\n== Single-die game (brief example) ==");
        GameConfig config = GameConfig.getInstance();
        config.reset().useSingleDice().useFixedDice(6, 6, 6, 6, 3, 4, 3, 4);
        GameFactory.createGame(config).play();
    }
}
