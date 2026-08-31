package frustration;

/**
 * The basic game board: 18 squares on the main loop and a 3-square tail
 * (including the End). Red starts at 1, Blue at 10.
 */
public final class StandardBoardConfiguration implements BoardConfiguration {

    @Override public int boardSize() { return 18; }
    @Override public int tailLength() { return 3; }

    @Override
    public int homeOf(PlayerColor colour) {
        switch (colour) {
            case RED:    return 1;
            case BLUE:   return 10;
            case GREEN:  return 5;   // used only by the advanced 4-player feature
            case YELLOW: return 14;  // used only by the advanced 4-player feature
            default:     throw new IllegalArgumentException("Unknown colour: " + colour);
        }
    }

    @Override public String describe() { return "Board positions=18 Tail positions=3"; }
}
