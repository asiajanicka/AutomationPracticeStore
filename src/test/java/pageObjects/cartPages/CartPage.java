package pageObjects.cartPages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import pageObjects.BasePage;

public class CartPage extends BasePage {
    public CartPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(id = "cart_title")
    @CacheLookup
    private WebElement cartTitle;

    public String getCartTitle() {
        return cartTitle.getText().strip();
    }
}
