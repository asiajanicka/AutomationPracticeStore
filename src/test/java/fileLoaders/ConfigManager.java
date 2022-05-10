package fileLoaders;

public class ConfigManager extends PropertiesLoader{

    public ConfigManager(){
        super("config.properties");
    }

    public String getBaseUrl() {
        return super.properties.getProperty("baseUrl");
    }
    public String getBrowser() {return super.properties.getProperty("browser");}
}
