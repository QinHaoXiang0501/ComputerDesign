package frustration.board;

import frustration.model.PlayerColor;

/**
 * Strategy for the physical layout of the board: how many squares sit on the main
 * loop, how many sit in the tail (including the End) and where each player's home
 * sits. This lets the game switch between the standard 18-square board and the
 * 36-square large board without touching any other class.
 */
public interface BoardConfiguration {

    /** Number of squares on the main (circular) board. */
    int boardSize();

    /** Number of squares in the tail, INCLUDING the End (3 or 6). */
    int tailLength();

    /**
     * The home board position for the given player colour in a game with
     * {@code playerCount} players. Homes depend on both the board size and the
     * number of players, because they are spread evenly around the board.
     */
    int homeOf(PlayerColor colour, int playerCount);

    /** One-line description of the layout, e.g. "Board positions=18 Tail positions=3". */
    String describe();
}
