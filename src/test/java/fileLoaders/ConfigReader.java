package fileLoaders;

import lombok.Getter;

@Getter
public class ConfigReader extends PropertiesReader {

    private String baseUrl;
    private String browser;

    public ConfigReader(){
        super("config.properties");
    }

    void loadData(){
        baseUrl = properties.getProperty("baseUrl");
        browser = properties.getProperty("browser");
    }
}
