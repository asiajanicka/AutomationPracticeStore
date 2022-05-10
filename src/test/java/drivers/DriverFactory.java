package drivers;

import fileLoaders.ConfigManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class DriverFactory {

    public WebDriver create(ConfigManager configuration) {
        switch (Browser.valueOf(configuration.getBrowser())) {
            case CHROME: {
                return new ChromeDriver();
            }
            case FIREFOX:
                return new FirefoxDriver();
            default:
                throw new IllegalArgumentException("Provided browser doesn't exist");
        }
    }
}
