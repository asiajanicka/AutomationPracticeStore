package enums;

public enum Categories {
    WOMEN("Women"),
    DRESSES("Dresses"),
    TOPS("Tops"),
    BLOUSES("Blouses"),
    T_SHIRTS("T-shirts"),
    CASUAL_DRESSES("Casual Dresses"),
    EVENING_DRESSES("Evening Dresses"),
    SUMMER_DRESSES("Summer Dresses");;
    private final String displayName;

    Categories(final String s) {
        this.displayName = s;
    }

    @Override
    public String toString() {
        return  displayName;
    }
}
