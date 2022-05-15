package tests;

import fileLoaders.AppPropertiesReader;
import fileLoaders.ConfigReader;
import fileLoaders.TestDataReader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.WebDriver;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BaseTest {

    protected WebDriver driver;
    protected ConfigReader configuration;
    protected AppPropertiesReader appProperties;
    protected TestDataReader testData;

    @BeforeAll
    public void initialSetup() {
        testData = new TestDataReader();
        configuration = new ConfigReader();
        appProperties = new AppPropertiesReader();
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }
}
