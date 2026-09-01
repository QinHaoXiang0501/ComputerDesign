package frustration.game;

import frustration.model.PlayerColor;
import frustration.board.BoardType;

/**
 * Singleton holding the currently active variant combination for a simulation:
 * which board is used, whether the HIT and/or exact-landing rules are enabled,
 * how many dice are rolled, and (optionally) a fixed dice sequence for testing.
 *
 * There is exactly one instance for the whole run, obtained via
 * {@link #getInstance()}. The {@link GameFactory} reads it to assemble a
 * {@link Game}; nothing else needs to know how the variants are stored.
 */
public final class GameConfig {

    private static final GameConfig INSTANCE = new GameConfig();

    private BoardType boardType = BoardType.STANDARD;
    private int diceCount = 2;           // 1 or 2 dice
    private boolean hitEnabled = false;
    private boolean exactLanding = false;
    private int[] diceSequence = null;   // fixed rolls for testing (null = random)
    private PlayerColor[] players = { PlayerColor.RED, PlayerColor.BLUE };

    /** Private constructor enforces the singleton. */
    private GameConfig() {}

    /** @return the single shared configuration instance. */
    public static GameConfig getInstance() {
        return INSTANCE;
    }

    // ---- fluent configuration (each returns the singleton for chaining) ----

    public GameConfig useBoard(BoardType type)     { this.boardType = type; return this; }
    public GameConfig useDiceCount(int count)      { this.diceCount = count; return this; }
    public GameConfig useSingleDice()              { return useDiceCount(1); } // Variation 3
    public GameConfig useTwoDice()                 { return useDiceCount(2); }
    public GameConfig enableHit()                  { this.hitEnabled = true; return this; }
    public GameConfig disableHit()                 { this.hitEnabled = false; return this; }
    public GameConfig enableHitBackToHome()        { return enableHit(); } // alias for Variation 2
    public GameConfig enableExactLanding()         { this.exactLanding = true; return this; }
    public GameConfig disableExactLanding()        { this.exactLanding = false; return this; }
    public GameConfig enablePreciseLanding()       { return enableExactLanding(); } // alias for Variation 1
    public GameConfig useFixedDice(int... rolls)   { this.diceSequence = rolls; return this; }
    public GameConfig useRandomDice()              { this.diceSequence = null; return this; }
    public GameConfig usePlayers(PlayerColor... c) { this.players = c; return this; }

    /** Reset every option back to the basic game. */
    public GameConfig reset() {
        boardType = BoardType.STANDARD;
        diceCount = 2;
        hitEnabled = false;
        exactLanding = false;
        diceSequence = null;
        players = new PlayerColor[] { PlayerColor.RED, PlayerColor.BLUE };
        return this;
    }

    // ---- read access, used by the GameFactory ----

    public BoardType boardType()    { return boardType; }
    public int diceCount()          { return diceCount; }
    public boolean hitEnabled()     { return hitEnabled; }
    public boolean exactLanding()   { return exactLanding; }
    public int[] diceSequence()     { return diceSequence; }
    public PlayerColor[] players()  { return players; }
}
