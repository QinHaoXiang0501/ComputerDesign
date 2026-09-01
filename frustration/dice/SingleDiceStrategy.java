package frustration.dice;

import java.util.Random;

/**
 * Variation 3: rolls a single six-sided die each turn (1..6).
 *
 * Swappable with {@link TwoDiceRollingStrategy} (2..12) because both implement
 * {@link DiceRollingStrategy}; the {@link frustration.game.GameFactory} chooses
 * one based on {@link frustration.game.GameConfig#diceCount()}. All other
 * variations (hit, winning, board) are unaffected, so they remain freely
 * combinable with this one.
 */
public final class SingleDiceStrategy implements DiceRollingStrategy {

    private final Random random;

    public SingleDiceStrategy() {
        this(new Random());
    }

    /** Injectable Random, useful for repeatable tests. */
    public SingleDiceStrategy(Random random) {
        this.random = random;
    }

    @Override
    public int roll() {
        return random.nextInt(6) + 1;   // 1..6
    }

    @Override public String describe() {
        return "Single 6 sided die";
    }
}
