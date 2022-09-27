package enums;

public enum Conditions {
    NEW("New");
    private final String displayName;

    Conditions(final String s) {
        this.displayName = s;
    }

    @Override
    public String toString() {
        return  displayName;
    }
}
