package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class ProductPage extends BasePage{
    public ProductPage(WebDriver driver) {
        super(driver);
        baseWait.until(ExpectedConditions.visibilityOf(name));
    }

    @FindBy(css = ".pb-center-column [itemprop='name']")
    @CacheLookup
    private WebElement name;

    public String getName(){
        return name.getText().strip();
    }
}
