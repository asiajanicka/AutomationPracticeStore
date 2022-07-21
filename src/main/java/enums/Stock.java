package enums;

public enum Stock {
    IN_STOCK("In Stock"),
    OUT_OF_STOCK("Out of Stock"),
    IN_STOCK_1("In stock");

    private String value;

    Stock(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
