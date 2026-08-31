import java.util.Random;

/**
 * Simple Frustration - Basic Game (base version, all logic in main).
 *
 * Board model (verified against the Task 1 Assessment Brief, Appendix scenarios):
 *   - The main board is a CLOCKWISE loop of BOARD_SIZE positions (1..BOARD_SIZE).·
 *   - Each player has a Home position. The position just before their Home is the
 *     "divert" position: once a player passes it they go up their private Tail.
 *   - The Tail has TAIL_LEN positions INCLUDING the End: T1, T2, ... , End.
 *   - Red home = 1, Blue home = 10 (basic game).
 *   - Two 6-sided dice are rolled each turn (sum 2..12).
 *   - Basic game: HITs are ignored (players may share a position);
 *     a player wins by landing ON or BEYOND the End (Overshoot allowed).
 *
 * The dice can be made deterministic for testing: pass a sequence on the command
 * line ("java SimpleFrustration 12 12 7 8"), or edit FIXED_ROLLS below.
 * Leave FIXED_ROLLS = null and pass no args for fully random play.
 */
public class SimpleFrustration {

    // ---- Basic-game board configuration ----
    static final int BOARD_SIZE = 18;                       // positions 1..18 on the main board
    static final int TAIL_LEN = 3;                          // tail slots INCLUDING the End (T1, T2, End)
    static final int[] HOME = {1, 10};                      // Red home = 1, Blue home = 10
    static final String[] NAMES = {"Red", "Blue"};

    // Fixed dice sequence reproducing Brief Scenario 1 (set to null for random play).
    static final int[] FIXED_ROLLS = {12, 12, 7, 8};
    // static final int[] FIXED_ROLLS = null;              // <-- uncomment for random dice

    public static void main(String[] args) {
        int[] position = new int[2];   // current board position (or BOARD_SIZE + tailOffset when in tail)
        int[] plays = new int[2];      // running turn count per player
        for (int i = 0; i < 2; i++) position[i] = HOME[i];

        Random random = new Random();
        int[] sequence = resolveDiceSequence(args);
        int seqIdx = 0;

        int current = 0;               // Red always starts
        int winner = -1;

        while (winner == -1) {
            // --- roll two 6-sided dice (sum 2..12) ---
            int roll;
            if (sequence != null) {
                roll = sequence[seqIdx++];
            } else {
                roll = random.nextInt(6) + 1 + random.nextInt(6) + 1;
            }

            int from = position[current];
            plays[current]++;          // running total of turns for the current player

            // --- compute where this roll takes the player ---
            MoveResult res = computeMove(current, from, roll);
            String fromLabel = formatPos(current, from);
            String toLabel = res.toLabel;

            // --- 1 & 2: print player, dice, from/to positions, and running turn count ---
            System.out.println(NAMES[current] + " play " + plays[current] + " rolls " + roll);
            if (res.overshoot) {
                System.out.println(NAMES[current] + " overshoots!");
            }
            System.out.println(NAMES[current] + " moves from " + fromLabel + " to " + toLabel);

            // --- check for a win ---
            if (res.reachedEnd) {
                winner = current;
            } else {
                position[current] = res.newPos;   // only advance if the game is not over
            }

            if (winner == -1) {
                current = 1 - current;           // alternate players
            }
        }

        // --- 3 & 4: winner and total turns for all players ---
        System.out.println(NAMES[winner] + " wins in " + plays[winner] + " moves!");
        System.out.println("Total plays " + (plays[0] + plays[1]));
    }

    /**
     * The board position just before a player's Home is where they divert up the Tail.
     * Red home 1 -> divert 18 ; Blue home 10 -> divert 9.  (General for any board size.)
     */
    static int divertPos(int player) {
        int h = HOME[player];
        return ((h - 2 + BOARD_SIZE) % BOARD_SIZE) + 1;
    }

    /**
     * Compute the result of a move for the given player from a board/tail position.
     * Internal representation: a plain board position is 1..BOARD_SIZE; a tail position
     * is stored as BOARD_SIZE + tailOffset (tailOffset 1..TAIL_LEN-1). End ends the game.
     */
    static MoveResult computeMove(int player, int from, int roll) {
        MoveResult r = new MoveResult();

        if (from > BOARD_SIZE) {
            // Already in the tail: just keep counting forward.
            int t = from - BOARD_SIZE;          // current tail offset (1..TAIL_LEN-1)
            int nt = t + roll;
            if (nt >= TAIL_LEN) {
                r.reachedEnd = true;
                r.toLabel = "END";
                r.overshoot = nt > TAIL_LEN;
            } else {
                r.newPos = BOARD_SIZE + nt;
                r.toLabel = "TAIL (Tail Position " + nt + ")";
            }
            return r;
        }

        // On the main board: move clockwise, diverting into the tail after the divert position.
        int d = divertPos(player);
        int stepsToD = ((d - from) % BOARD_SIZE + BOARD_SIZE) % BOARD_SIZE; // 0 if already at divert
        int distToEnd = stepsToD + TAIL_LEN;   // steps needed to reach End from here

        if (roll <= stepsToD) {
            // Stays on the main board (may wrap around).
            int np = ((from - 1 + roll) % BOARD_SIZE) + 1;
            r.newPos = np;
            r.toLabel = formatPos(player, np);
        } else if (roll >= distToEnd) {
            // Lands on or beyond the End -> wins (Overshoot allowed in the basic game).
            r.reachedEnd = true;
            r.toLabel = "END";
            r.overshoot = roll > distToEnd;
        } else {
            // Lands inside the tail (tailOffset = roll - stepsToD, in 1..TAIL_LEN-1).
            int tailOffset = roll - stepsToD;
            r.newPos = BOARD_SIZE + tailOffset;
            r.toLabel = "TAIL (Tail Position " + tailOffset + ")";
        }
        return r;
    }

    /**
     * Format a position exactly as required by the brief:
     *   board position == home      -> "HOME (Position X)"
     *   other board position        -> "Position X"
     *   tail offset t (1..TAIL_LEN-1) -> "TAIL (Tail Position t)"
     *   End                         -> "END"
     */
    static String formatPos(int player, int pos) {
        if (pos > BOARD_SIZE) {
            return "TAIL (Tail Position " + (pos - BOARD_SIZE) + ")";
        }
        if (pos == HOME[player]) {
            return "HOME (Position " + pos + ")";
        }
        return "Position " + pos;
    }

    /** Pick the dice source: CLI args > FIXED_ROLLS constant > null (random). */
    static int[] resolveDiceSequence(String[] args) {
        if (args.length > 0) {
            int[] seq = new int[args.length];
            for (int i = 0; i < args.length; i++) seq[i] = Integer.parseInt(args[i].trim());
            return seq;
        }
        return FIXED_ROLLS;
    }

    /** Small holder for the outcome of a single move. */
    static class MoveResult {
        int newPos;            // updated board/tail position (used only when not finished)
        String toLabel;        // display text for the destination
        boolean reachedEnd;    // true if this move reached the End
        boolean overshoot;     // true if the dice exceeded the distance to the End
    }
}
