package tests;

import TestHelpers.TestStatus;
import drivers.DriverFactory;
import fileLoaders.AppPropertiesReader;
import fileLoaders.ConfigReader;
import fileLoaders.TestDataReader;
import io.qameta.allure.Attachment;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.time.Duration;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BaseTest {

    protected WebDriver driver;
    protected ConfigReader configuration;
    protected AppPropertiesReader appProperties;
    protected TestDataReader testData;
    @RegisterExtension
    protected TestStatus status = new TestStatus();

    @BeforeEach
    public void testSetup() {
        DriverFactory driverFactory = new DriverFactory();
        driver = driverFactory.create(configuration);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(7));
        driver.manage().window().maximize();
    }

    @BeforeAll
    public void initialSetup() {
        testData = new TestDataReader();
        configuration = new ConfigReader();
        appProperties = new AppPropertiesReader();
    }
    @AfterEach
    public void tearDown() {
        if(status.isFailed){
            takeScreenshot();
        }
        driver.quit();
    }

//    @Attachment
//    protected String takeScreenshot(TestInfo info) throws IOException {
//        driver.switchTo().defaultContent();
//        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
//        LocalDateTime timeNow = LocalDateTime.now();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH-mm-ss");
////        String path = System.getProperty("user.dir") + "\\screenshots\\" + info.getDisplayName() + " " + formatter.format(timeNow) + ".png";
//        FileUtils.copyFile(screenshot, new File(path));
//        return path;
//    }


    @Attachment(value = "Page screenshot", type = "image/png")
    protected byte[] takeScreenshot() {
        driver.switchTo().defaultContent();
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

}
