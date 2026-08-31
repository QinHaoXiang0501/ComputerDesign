package frustration;

import java.util.Arrays;

/**
 * A dice strategy that replays a fixed sequence of totals. This is what the brief
 * recommends for testing: it lets us reproduce the worked examples deterministically.
 * Throws if the sequence is exhausted before the game ends.
 */
public final class FixedDiceRollingStrategy implements DiceRollingStrategy {

    private final int[] totals;
    private int index = 0;

    public FixedDiceRollingStrategy(int... totals) {
        this.totals = Arrays.copyOf(totals, totals.length);
    }

    @Override
    public int roll() {
        if (index >= totals.length) {
            throw new IllegalStateException("Fixed dice sequence exhausted (length=" + totals.length + ")");
        }
        return totals[index++];
    }

    @Override
    public String describe() {
        StringBuilder sb = new StringBuilder("Sequence of dice rolls (");
        for (int i = 0; i < totals.length; i++) {
            if (i > 0) sb.append(",");
            sb.append(totals[i]);
        }
        return sb.append(")").toString();
    }
}
