package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ProductPage extends BasePage{
    public ProductPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(css = ".pb-center-column [itemprop='name']")
    private WebElement name;

    public String getName(){
        return name.getText().strip();
    }
}
