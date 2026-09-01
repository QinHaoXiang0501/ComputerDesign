package frustration.dice;

/**
 * The dice object the game rolls each turn. This class is the Strategy-pattern
 * "context": it holds a {@link DiceRollingStrategy} and delegates to it, so the
 * number of dice (or a fixed test sequence) can be swapped without touching the
 * game loop.
 */
public final class Dice {

    private final DiceRollingStrategy strategy;

    public Dice(DiceRollingStrategy strategy) {
        this.strategy = strategy;
    }

    /** @return the total of one roll. */
    public int roll() {
        return strategy.roll();
    }

    /** One-line description of how the dice behave. */
    public String describe() {
        return strategy.describe();
    }
}
