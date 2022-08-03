package pageObjects.homePages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import pageObjects.BasePage;

public class PopularItemsPage extends BasePage {
    protected PopularItemsPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(className = "alert-info")
    @CacheLookup
    private WebElement noProductsAlert;

    public boolean isNoProductsAlertDisplayed(){

        return super.isElementDisplayed(noProductsAlert);
    }
}
