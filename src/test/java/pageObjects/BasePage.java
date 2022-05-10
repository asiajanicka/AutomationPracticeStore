package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class BasePage {

    protected WebDriver driver;
    protected WebDriverWait baseWait;

    protected BasePage(WebDriver driver){
        PageFactory.initElements(driver, this);
        baseWait = new WebDriverWait(driver, Duration.ofSeconds(5));
        this.driver = driver;
    }

    protected boolean isElementDisplayed(WebElement element) {
        boolean isDisplayed = false;
        try {

            baseWait.until(ExpectedConditions.visibilityOf(element));
            isDisplayed = element.isDisplayed();
        } catch (Exception e) {
        }
        return isDisplayed;
    }
}
