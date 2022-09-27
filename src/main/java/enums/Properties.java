package enums;

public enum Properties {
    SHORT_SLEEVE("Short Sleeve"),
    COLORFUL_DRESS("Colorful Dress"),
    MAXI_DRESS("Maxi Dress");
    String displayName;

    Properties(String s) {
        displayName = s;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
