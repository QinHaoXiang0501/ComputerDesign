package frustration.demo;

import frustration.board.BoardType;
import frustration.game.GameConfig;
import frustration.game.GameFactory;
import frustration.model.PlayerColor;

/**
 * Focused demonstration of the four-player mode: reproduces the brief's four-player
 * worked example (large board + precise landing + HIT) with Red, Blue, Green and
 * Yellow.
 */
public final class FourPlayersDemo {

    public static void main(String[] args) {
        GameConfig config = GameConfig.getInstance();
        config.reset()
                .useBoard(BoardType.LARGE)
                .enableHit()
                .enablePreciseLanding()
                .usePlayers(PlayerColor.RED, PlayerColor.BLUE, PlayerColor.GREEN, PlayerColor.YELLOW)
                .useFixedDice(12, 5, 2, 12, 7, 4, 2, 12, 3, 5, 2, 12, 5, 4, 4, 8, 2, 2, 12, 3);
        GameFactory.createGame(config).play();
    }
}
