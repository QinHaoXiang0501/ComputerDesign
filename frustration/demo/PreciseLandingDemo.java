package frustration.demo;

import frustration.board.Board;
import frustration.board.StandardBoardConfiguration;
import frustration.model.Player;
import frustration.model.PlayerColor;
import frustration.model.Position;
import frustration.rule.PreciseLandingStrategy;

/**
 * Focused demonstration of Variation 1 (Precise Landing): a piece on the tail
 * that overshoots the End bounces back down the tail and around the board.
 */
public final class PreciseLandingDemo {

    public static void main(String[] args) {
        Board board = new Board(new StandardBoardConfiguration(), new PreciseLandingStrategy());
        Player red = new Player(PlayerColor.RED, 1);

        // The task's worked example: Tail Position 2 + roll 5 -> Position 17.
        red.moveTo(Position.tail(2));
        System.out.println("T2 + 5 -> " + board.move(red, 5).landing().label());

        red.moveTo(Position.tail(2));
        System.out.println("T2 + 1 -> " + board.move(red, 1).landing().label());

        red.moveTo(Position.tail(2));
        System.out.println("T2 + 3 -> " + board.move(red, 3).landing().label());

        red.moveTo(Position.tail(2));
        System.out.println("T2 + 4 -> " + board.move(red, 4).landing().label());
    }
}
