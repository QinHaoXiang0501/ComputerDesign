package frustration;

/**
 * Simple Factory that assembles a fully configured {@link Game} from the singleton
 * {@link GameConfig}. It is the single place that maps configuration options onto
 * concrete strategy objects (board, dice, hit and winning), so the game engine
 * itself stays decoupled from how the pieces are wired together.
 */
public final class GameFactory {

    private GameFactory() {} // static utility class: no instances

    /**
     * Build a ready-to-play game from the given configuration.
     *
     * <pre>
     *   GameConfig config = GameConfig.getInstance();
     *   Game game = GameFactory.createGame(config);
     *   game.play();
     * </pre>
     */
    public static Game createGame(GameConfig config) {
        // 1. Board layout
        BoardConfiguration boardConfiguration =
                config.boardType() == BoardType.LARGE
                        ? new LargeBoardConfiguration()
                        : new StandardBoardConfiguration();

        // 2. Dice strategy (a fixed test sequence wins over random one/two dice)
        DiceRollingStrategy diceStrategy;
        if (config.diceSequence() != null) {
            diceStrategy = new FixedDiceRollingStrategy(config.diceSequence());
        } else if (config.diceCount() == 1) {
            diceStrategy = new SingleDieRollingStrategy();
        } else {
            diceStrategy = new TwoDiceRollingStrategy();
        }

        // 3. Hit strategy
        HitStrategy hitStrategy =
                config.hitEnabled() ? new SendHomeHitStrategy() : new NoHitStrategy();

        // 4. Winning strategy
        WinningStrategy winningStrategy =
                config.exactLanding() ? new ExactLandingWinningStrategy() : new OvershootWinningStrategy();

        // 5. Assemble and return
        return new Game(
                new Board(boardConfiguration, winningStrategy),
                new Dice(diceStrategy),
                hitStrategy,
                config.players());
    }
}
