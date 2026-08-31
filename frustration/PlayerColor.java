package frustration;

/**
 * The colours a player may have, together with their display name.
 * Keeping this as an enum (rather than a String) makes player identity type-safe
 * and centralises the mapping colour -> name in one place.
 */
public enum PlayerColor {
    RED("Red"),
    BLUE("Blue"),
    GREEN("Green"),
    YELLOW("Yellow");

    private final String displayName;

    PlayerColor(String displayName) {
        this.displayName = displayName;
    }

    /** The human-readable name used in output, e.g. "Red". */
    public String displayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
