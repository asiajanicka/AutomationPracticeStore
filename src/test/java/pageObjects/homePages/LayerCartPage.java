package pageObjects.homePages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pageObjects.BasePage;

public class LayerCartPage extends BasePage {

    public LayerCartPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(css = "continue")
    private WebElement continueShoppingBtn;

    @FindBy(css = ".layer_cart_product .cross")
    private WebElement crossButton;

    public boolean isCrossDisplayed(){
        return isElementDisplayed(crossButton);
    }

    @Step("Close layer cart window with X")
    public void closeWindow(){
        isCrossDisplayed();
        crossButton.click();
    }
}
