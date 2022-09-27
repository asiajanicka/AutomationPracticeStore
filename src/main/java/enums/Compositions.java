package enums;

public enum Compositions {
    COTTON("Cotton"),
    VISCOSE("Viscose");
    private final String displayName;

    Compositions(String s) {
        displayName = s;
    }

    public String toString(){
        return displayName;
    }
}
