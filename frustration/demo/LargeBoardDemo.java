package frustration.demo;

import frustration.board.Board;
import frustration.board.BoardType;
import frustration.board.LargeBoardConfiguration;
import frustration.model.Player;
import frustration.model.PlayerColor;
import frustration.model.Position;
import frustration.rule.PreciseLandingStrategy;
import frustration.game.GameConfig;
import frustration.game.GameFactory;

/**
 * Focused demonstration of Variation 4 (Large Board): the 36-square board, and its
 * compatibility with the single-die, precise-landing and HIT variations.
 */
public final class LargeBoardDemo {

    public static void main(String[] args) {
        GameConfig config = GameConfig.getInstance();

        System.out.println("== Large board (basic) ==");
        config.reset().useBoard(BoardType.LARGE).useFixedDice(12, 12, 12, 12, 12, 12, 4, 5);
        GameFactory.createGame(config).play();

        System.out.println("\n== Large board + single die ==");
        config.reset().useBoard(BoardType.LARGE).useSingleDice()
                .useFixedDice(5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5);
        GameFactory.createGame(config).play();

        System.out.println("\n== Large board + precise landing (bounce-back) ==");
        Board board = new Board(new LargeBoardConfiguration(), new PreciseLandingStrategy());
        Player blue = new Player(PlayerColor.BLUE, 19);
        blue.moveTo(Position.tail(1));
        System.out.println("T1 + 7 -> " + board.move(blue, 7).landing().label());
        blue.moveTo(Position.tail(1));
        System.out.println("T1 + 5 -> " + board.move(blue, 5).landing().label());

        System.out.println("\n== Large board + HIT ==");
        config.reset().useBoard(BoardType.LARGE).enableHit().useFixedDice(12, 6, 12, 12, 11, 12, 6);
        GameFactory.createGame(config).play();
    }
}
