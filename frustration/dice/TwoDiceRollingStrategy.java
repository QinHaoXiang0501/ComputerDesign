package frustration.dice;

import java.util.Random;

/** Rolls two six-sided dice and returns their sum (2..12). */
public final class TwoDiceRollingStrategy implements DiceRollingStrategy {

    private final Random random;

    public TwoDiceRollingStrategy() {
        this(new Random());
    }

    /** Injectable Random, useful for repeatable tests. */
    public TwoDiceRollingStrategy(Random random) {
        this.random = random;
    }

    @Override
    public int roll() {
        return (random.nextInt(6) + 1) + (random.nextInt(6) + 1);
    }

    @Override public String describe() { return "Two random 6 sided dice"; }
}
