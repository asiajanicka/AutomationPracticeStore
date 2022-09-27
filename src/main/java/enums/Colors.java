package enums;

public enum Colors {
    PINK("Pink"),
    BLACK("Black"),
    WHITE("White"),
    BLUE("Blue"),
    YELLOW("Yellow");

    Colors(String displayName) {
        this.displayName = displayName;
    }

    private final String displayName;

    public String toString(){
        return displayName;
    }

}
