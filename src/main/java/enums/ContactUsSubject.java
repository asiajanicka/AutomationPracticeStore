package enums;

public enum ContactUsSubject {

    CHOOSE("-- Choose --"),
    CUSTOMER_SERVICE("Customer service"),
    WEBMASTER("Webmaster");

    private String value;

    ContactUsSubject(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
