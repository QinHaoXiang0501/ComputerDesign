package frustration;

import java.util.ArrayList;
import java.util.List;

/**
 * The game engine. Runs the turn loop, alternates players, applies moves, resolves
 * hits and detects the winner. It depends only on the {@link Board}, {@link Dice}
 * and {@link HitStrategy} abstractions, so every rule variation is assembled for
 * it by the {@link GameFactory}.
 */
public final class Game {

    private final Board board;
    private final Dice dice;
    private final HitStrategy hitStrategy;
    private final List<Player> players;

    private int current = 0;
    private Player winner = null;

    public Game(Board board, Dice dice, HitStrategy hitStrategy, PlayerColor... colours) {
        this.board = board;
        this.dice = dice;
        this.hitStrategy = hitStrategy;
        this.players = new ArrayList<>();
        for (PlayerColor colour : colours) {
            players.add(new Player(colour, board.homeOf(colour)));
        }
    }

    /** Print the board / players / rules / dice configuration. */
    public void printConfiguration() {
        System.out.println(board + " Players=" + playerList());
        System.out.println(board.winningRuleDescription());
        System.out.println(hitStrategy.describe());
        System.out.println("Dice: " + dice.describe());
    }

    /** Play the game to completion and print the result. */
    public void play() {
        printConfiguration();
        while (winner == null) {
            playTurn();
            if (winner == null) {
                current = (current + 1) % players.size();
            }
        }
        System.out.println(winner.name() + " wins in " + winner.plays() + " moves!");
        System.out.println("Total plays " + totalPlays());
    }

    private String playerList() {
        StringBuilder sb = new StringBuilder("{");
        for (int i = 0; i < players.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(players.get(i).name());
        }
        return sb.append("}").toString();
    }

    private int totalPlays() {
        int total = 0;
        for (Player p : players) total += p.plays();
        return total;
    }

    private void playTurn() {
        Player player = players.get(current);
        int roll = dice.roll();
        player.incrementPlays();

        Position from = player.position();
        MoveResult result = board.move(player, roll);
        Position landing = result.landing();

        System.out.println(player.name() + " play " + player.plays() + " rolls " + roll);
        if (result.overshot()) {
            System.out.println(player.name() + " overshoots!");
        }
        System.out.println(player.name() + " moves from " + from.label() + " to " + landing.label());

        if (landing.isEnd()) {
            winner = player;
            return;
        }

        player.moveTo(landing);
        resolveHit(player, landing);
    }

    private void resolveHit(Player mover, Position landing) {
        if (!landing.isBoard()) {
            return; // tails are private to each player, so hits only occur on the main loop
        }
        Player victim = findVictim(mover, landing);
        if (victim == null) {
            return;
        }
        // Always announce the hit (the brief prints this even when hits are ignored).
        System.out.println(victim.name() + " " + landing.label() + " hit!");
        if (hitStrategy.shouldSendVictimHome(mover, victim, landing)) {
            Position home = Position.home(victim.home());
            System.out.println(victim.name() + " moves from " + landing.label() + " to " + home.label());
            victim.moveTo(home);
        }
    }

    private Player findVictim(Player mover, Position landing) {
        int square = landing.value();
        for (Player p : players) {
            if (p == mover) continue;
            Position pos = p.position();
            // A player occupies the square whether on the board or still on their
            // home square (a home square is also a numbered board square).
            if ((pos.isBoard() || pos.isHome()) && pos.value() == square) {
                return p;
            }
        }
        return null;
    }
}
