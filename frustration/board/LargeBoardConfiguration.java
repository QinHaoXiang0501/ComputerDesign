package frustration.board;

import frustration.model.PlayerColor;

/**
 * The large board: 36 squares on the main loop and a 6-square tail (including the
 * End). Red starts at 1, Blue at 19.
 */
public final class LargeBoardConfiguration implements BoardConfiguration {

    @Override public int boardSize() { return 36; }
    @Override public int tailLength() { return 6; }

    @Override
    public int homeOf(PlayerColor colour, int playerCount) {
        if (playerCount == 2) {
            switch (colour) {
                case RED:  return 1;
                case BLUE: return 19;
                default:   throw new IllegalArgumentException("Two-player game supports only Red and Blue: " + colour);
            }
        }
        // 4 players: homes spread evenly around the board (1, 10, 19, 28).
        switch (colour) {
            case RED:    return 1;
            case BLUE:   return 10;
            case GREEN:  return 19;
            case YELLOW: return 28;
            default:     throw new IllegalArgumentException("Unknown colour: " + colour);
        }
    }

    @Override public String describe() { return "Board positions=36 Tail positions=6"; }
}
