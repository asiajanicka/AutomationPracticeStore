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
        baseWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.driver = driver;
    }

    protected boolean isElementDisplayed(WebElement element) {
        boolean isDisplayed = false;
        try {
            baseWait.until(ExpectedConditions.visibilityOf(element));
            isDisplayed = true;
        } catch (Exception e) {
        }
        return isDisplayed;
    }

    protected boolean isElementRemoved(WebElement element) {
        boolean isRemoved = false;
        try {
            baseWait.until(ExpectedConditions.invisibilityOf(element));
            isRemoved = !element.isDisplayed();
        } catch (Exception e) {

        }
        return isRemoved;
    }
}
