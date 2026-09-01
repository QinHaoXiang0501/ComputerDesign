package frustration.dice;

/**
 * Strategy for producing a single dice total each turn. Lets the game switch
 * between two dice, a single die, or a fixed test sequence without changing the
 * game engine.
 */
public interface DiceRollingStrategy {

    /** @return the total value of this roll. */
    int roll();

    /** One-line description of how the dice behave, e.g. "Two random 6 sided dice". */
    String describe();
}
