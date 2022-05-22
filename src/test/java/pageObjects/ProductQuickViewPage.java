package pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ProductQuickViewPage extends BasePage {
    public ProductQuickViewPage(WebDriver driver) {
        super(driver);
        driver.switchTo().frame(1);
    }

    @FindBy(css = "#product .pb-center-column [itemprop='name']")
    private WebElement name;

    public String getName() {
        return name.getText();
    }
}
