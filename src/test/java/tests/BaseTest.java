package tests;

import fileLoaders.AppProperties;
import fileLoaders.ConfigManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.WebDriver;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BaseTest {

    protected WebDriver driver;
    protected ConfigManager configuration;
    protected AppProperties appProperties;

    @BeforeAll
    public void initialSetup() {
        configuration = new ConfigManager();
        appProperties = new AppProperties();
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }
}
