package enums;

public enum SortProductsOptions {
    PRICE_ASC("price:asc"),
    PRICE_DESC("price:desc"),
    NAME_ASC("name:asc"),
    NAME_DESC("name:desc"),
    QUANTITY_DESC("quantity:desc"),
    REFERENCE_ASC("reference:asc"),
    REFERENCE_DESC("reference:desc");

    private String value;

    SortProductsOptions(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
