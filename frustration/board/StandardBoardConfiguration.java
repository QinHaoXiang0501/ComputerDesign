package frustration.board;

import frustration.model.PlayerColor;

/**
 * The basic game board: 18 squares on the main loop and a 3-square tail
 * (including the End). Red starts at 1, Blue at 10.
 */
public final class StandardBoardConfiguration implements BoardConfiguration {

    @Override public int boardSize() { return 18; }
    @Override public int tailLength() { return 3; }

    @Override
    public int homeOf(PlayerColor colour, int playerCount) {
        if (playerCount == 2) {
            switch (colour) {
                case RED:  return 1;
                case BLUE: return 10;
                default:   throw new IllegalArgumentException("Two-player game supports only Red and Blue: " + colour);
            }
        }
        // 4 players: homes spread evenly around the board (1, 5, 10, 14).
        switch (colour) {
            case RED:    return 1;
            case BLUE:   return 5;
            case GREEN:  return 10;
            case YELLOW: return 14;
            default:     throw new IllegalArgumentException("Unknown colour: " + colour);
        }
    }

    @Override public String describe() { return "Board positions=18 Tail positions=3"; }
}
