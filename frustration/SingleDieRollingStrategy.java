package frustration;

import java.util.Random;

/** Rolls a single six-sided die (1..6). */
public final class SingleDieRollingStrategy implements DiceRollingStrategy {

    private final Random random;

    public SingleDieRollingStrategy() {
        this(new Random());
    }

    /** Injectable Random, useful for repeatable tests. */
    public SingleDieRollingStrategy(Random random) {
        this.random = random;
    }

    @Override
    public int roll() {
        return random.nextInt(6) + 1;
    }

    @Override public String describe() { return "Single 6 sided die"; }
}
