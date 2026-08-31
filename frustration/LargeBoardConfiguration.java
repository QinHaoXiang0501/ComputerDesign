package frustration;

/**
 * The large board: 36 squares on the main loop and a 6-square tail (including the
 * End). Red starts at 1, Blue at 19.
 */
public final class LargeBoardConfiguration implements BoardConfiguration {

    @Override public int boardSize() { return 36; }
    @Override public int tailLength() { return 6; }

    @Override
    public int homeOf(PlayerColor colour) {
        switch (colour) {
            case RED:    return 1;
            case BLUE:   return 19;
            case GREEN:  return 10;  // used only by the advanced 4-player feature
            case YELLOW: return 28;  // used only by the advanced 4-player feature
            default:     throw new IllegalArgumentException("Unknown colour: " + colour);
        }
    }

    @Override public String describe() { return "Board positions=36 Tail positions=6"; }
}
