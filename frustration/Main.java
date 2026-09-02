package frustration;

import frustration.game.GameConfig;
import frustration.game.GameFactory;
import frustration.game.UndoDecision;
import frustration.model.Player;
import frustration.model.PlayerColor;
import frustration.board.BoardType;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Command-line runner for Simple Frustration.
 *
 * Reads the variant combination from command-line flags, configures the singleton
 * {@link GameConfig}, and uses the {@link GameFactory} to build and play a game.
 * Supports every combination of the four variations, 2 or 4 players, a fixed dice
 * sequence for testing, and (optionally) running many random simulations to
 * report a win rate.
 *
 * Examples:
 *   java frustration.Main --rolls 12,12,7,8
 *   java frustration.Main --dice single --hit on --precision off --board large
 *   java frustration.Main --board large --hit on --precision on --players 4 --rolls 12,5,2,12,7,4,2,12,3,5,2,12,5,4,4,8,2,2,12,3
 *   java frustration.Main --players 4 --games 1000
 */
public final class Main {

    public static void main(String[] args) {
        if (args.length == 0) {
            runInteractiveMenu();  // no flags -> the interactive menu (matches the brief)
            return;
        }

        Args a = Args.parse(args);
        if (a.help) {
            printUsage();
            return;
        }

        GameConfig config = GameConfig.getInstance();
        config.reset().useBoard(a.largeBoard ? BoardType.LARGE : BoardType.STANDARD);
        if (a.singleDie) config.useSingleDice(); else config.useTwoDice();
        if (a.hit) config.enableHit();
        if (a.precision) config.enablePreciseLanding();
        if (a.players == 4) {
            config.usePlayers(PlayerColor.RED, PlayerColor.BLUE, PlayerColor.GREEN, PlayerColor.YELLOW);
        }

        if (a.games > 1) {
            runSimulations(config, a.games);
        } else {
            if (a.rolls != null) config.useFixedDice(a.rolls);
            GameFactory.createGame(config).play();
        }
    }

    /** Interactive menu shown when the program is run without command-line flags. */
    private static void runInteractiveMenu() {
        Scanner in = new Scanner(System.in);
        GameConfig config = GameConfig.getInstance();

        boolean play = false;
        while (!play) {
            System.out.println("========");
            System.out.println("Simple Frustration");
            System.out.println("========");
            System.out.print("[Enter] Basic Game, [C] Configure Game, [U] Interactive Undo, or [Q] Exit: ");
            String choice = in.nextLine().trim().toLowerCase();
            switch (choice) {
                case "":                     // Basic Game - defaults already in place
                    config.reset();
                    play = true;
                    break;

                case "c":                    // Configure Game
                    configure(config, in);
                    play = true;
                    break;

                case "u":                    // Interactive Undo - basic game with undo
                    config.reset();
                    GameFactory.createGame(config).playWith(interactiveUndo(in));
                    return;

                case "q":                    // Exit
                    System.out.println("Bye.");
                    return;

                default:
                    System.out.println("Unrecognised option - try Enter, C, U or Q.");
                    System.out.println();
                    break;
            }
        }

        GameFactory.createGame(config).play();
    }

    /** Ask the five variant questions and apply them to {@code config}. */
    private static void configure(GameConfig config, Scanner in) {
        System.out.println();
        System.out.println("Configure Game");
        System.out.println("--------------");
        boolean large = promptChoice(in, "Board:    [1] Standard  [2] Large: ", new int[]{1, 2}, 1) == 2;
        int players = promptChoice(in, "Players:  [2] Two  [4] Four: ", new int[]{2, 4}, 2);
        boolean exact = promptChoice(in, "END:      [1] Land on or beyond  [2] Exact: ", new int[]{1, 2}, 1) == 2;
        boolean hit = promptChoice(in, "HIT:      [1] Ignore  [2] Send HOME: ", new int[]{1, 2}, 1) == 2;
        boolean single = promptChoice(in, "Dice:     [1] Single  [2] Two dice: ", new int[]{1, 2}, 2) == 1;

        config.reset();
        config.useBoard(large ? BoardType.LARGE : BoardType.STANDARD);
        config.usePlayers(players == 4
                ? new PlayerColor[]{PlayerColor.RED, PlayerColor.BLUE, PlayerColor.GREEN, PlayerColor.YELLOW}
                : new PlayerColor[]{PlayerColor.RED, PlayerColor.BLUE});
        if (exact) config.enableExactLanding(); else config.disableExactLanding();
        if (hit) config.enableHit(); else config.disableHit();
        if (single) config.useSingleDice(); else config.useTwoDice();
        System.out.println();
    }

    /** Read one of {@code allowed}, or {@code defaultValue} on a blank line. */
    private static int promptChoice(Scanner in, String label, int[] allowed, int defaultValue) {
        while (true) {
            System.out.print(label);
            String line = in.nextLine().trim();
            if (line.isEmpty()) return defaultValue;
            try {
                int v = Integer.parseInt(line);
                for (int a : allowed) if (v == a) return v;
            } catch (NumberFormatException ignored) {
                // fall through to the error message
            }
            System.out.println("  -> please enter one of " + Arrays.toString(allowed)
                    + " (or press Enter to keep " + defaultValue + ").");
        }
    }

    /** An undo hook that asks the player Y/N after each roll. */
    private static UndoDecision interactiveUndo(Scanner in) {
        return turn -> {
            System.out.print("Undo this roll? [Y/N]: ");
            String ans = in.nextLine().trim().toLowerCase();
            return ans.startsWith("y");
        };
    }

    /** Run many random games and report each player's win rate. */
    private static void runSimulations(GameConfig config, int games) {
        Map<PlayerColor, Integer> wins = new LinkedHashMap<>();
        for (PlayerColor c : config.players()) wins.put(c, 0);
        for (int i = 0; i < games; i++) {
            Player winner = GameFactory.createGame(config).simulate();
            wins.merge(winner.colour(), 1, (a, b) -> a + b);
        }
        System.out.println("Simulated " + games + " games");
        for (PlayerColor c : config.players()) {
            int w = wins.get(c);
            System.out.printf("%-6s won %d games (%.2f%%)%n", c.displayName(), w, 100.0 * w / games);
        }
    }

    private static void printUsage() {
        System.out.println("Usage: java frustration.Main [options]");
        System.out.println();
        System.out.println("Options:");
        System.out.println("  --dice two|single       dice mode (default: two)");
        System.out.println("  --hit on|off            HIT rule: victim sent home (default: off)");
        System.out.println("  --precision on|off      precise landing to win (default: off)");
        System.out.println("  --board standard|large  board size (default: standard)");
        System.out.println("  --players 2|4           number of players (default: 2)");
        System.out.println("  --rolls n,n,n,...       fixed dice sequence for a single run (testing)");
        System.out.println("  --games N               run N random games and report win rate (default: 1)");
        System.out.println("  --help                  show this help");
        System.out.println();
        System.out.println("Examples:");
        System.out.println("  java frustration.Main --rolls 12,12,7,8");
        System.out.println("  java frustration.Main --players 4");
        System.out.println("  java frustration.Main --dice single --hit on --precision on --board large --players 4");
        System.out.println("  java frustration.Main --games 1000");
        System.out.println();
        System.out.println("Run the brief's worked examples: java frustration.demo.Demos");
    }

    /** Parsed command-line arguments. */
    private static final class Args {
        boolean singleDie = false;
        boolean hit = false;
        boolean precision = false;
        boolean largeBoard = false;
        int players = 2;
        int[] rolls = null;
        int games = 1;
        boolean help = false;

        static Args parse(String[] args) {
            Args a = new Args();
            for (int i = 0; i < args.length; i++) {
                String flag = args[i];
                switch (flag) {
                    case "--dice":
                        a.singleDie = "single".equals(require(args, ++i, flag));
                        break;
                    case "--hit":
                        a.hit = parseBool(require(args, ++i, flag));
                        break;
                    case "--precision":
                        a.precision = parseBool(require(args, ++i, flag));
                        break;
                    case "--board":
                        a.largeBoard = "large".equals(require(args, ++i, flag));
                        break;
                    case "--players":
                        a.players = Integer.parseInt(require(args, ++i, flag));
                        break;
                    case "--rolls":
                        a.rolls = parseRolls(require(args, ++i, flag));
                        break;
                    case "--games":
                        a.games = Math.max(1, Integer.parseInt(require(args, ++i, flag)));
                        break;
                    case "--help":
                    case "-h":
                        a.help = true;
                        break;
                    default:
                        System.err.println("Unknown option: " + flag);
                        a.help = true;
                }
            }
            return a;
        }

        private static String require(String[] args, int i, String flag) {
            if (i >= args.length) {
                System.err.println("Missing value for " + flag);
                System.exit(1);
            }
            return args[i];
        }

        private static boolean parseBool(String v) {
            if ("on".equalsIgnoreCase(v) || "true".equalsIgnoreCase(v) || "1".equals(v)) return true;
            if ("off".equalsIgnoreCase(v) || "false".equalsIgnoreCase(v) || "0".equals(v)) return false;
            System.err.println("Expected on/off but got: " + v);
            System.exit(1);
            return false;
        }

        private static int[] parseRolls(String v) {
            String[] parts = v.split(",");
            int[] rolls = new int[parts.length];
            for (int i = 0; i < parts.length; i++) {
                rolls[i] = Integer.parseInt(parts[i].trim());
            }
            return rolls;
        }
    }
}
