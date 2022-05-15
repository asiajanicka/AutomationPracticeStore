package fileLoaders;

import lombok.Getter;
import utils.ContactUsMessage;

@Getter
public class TestDataReader extends PropertiesReader {

    private ContactUsMessage message;

    public TestDataReader() {
        super("testData.properties");
    }

    void loadData(){
        message = new ContactUsMessage(properties);
    }
}
