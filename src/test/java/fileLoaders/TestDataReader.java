package fileLoaders;

import lombok.Getter;
import utils.ContactUsMessage;

@Getter
public class TestDataReader extends PropertiesReader {

    private ContactUsMessage message;
    private String[] productNames;
    private String[] discountedProductNames;

    public TestDataReader() {
        super("testData.properties");
    }

    void loadData(){
        message = new ContactUsMessage(properties);
        productNames = properties.getProperty("product.name").split(",");
        discountedProductNames = properties.getProperty("product.discounted.name").split(",");
    }
}
