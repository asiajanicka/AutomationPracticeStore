package enums;

public enum Styles {
    COLORFUL_DRESS("Colorful dress"),
    CASUAL("Casual"),
    DRESSY("Dressy"),
    GIRLY("Girly");
    private final String displayName;

    Styles(final String s) {
        this.displayName = s;
    }

    @Override
    public String toString() {
        return  displayName;
    }
}
