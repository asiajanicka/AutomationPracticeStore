package pageObjects.homePages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pageObjects.base.BasePage;

public class LayerCartPage extends BasePage {

    public LayerCartPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(css = "continue")
    @CacheLookup
    private WebElement continueShoppingBtn;

    @FindBy(css = ".layer_cart_product .cross")
    @CacheLookup
    private WebElement crossButton;

    @Step("Close layer cart window with X")
    public void closeWindow(){
        baseWait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fancybox-overlay")));
        baseWait.until(ExpectedConditions.elementToBeClickable(crossButton));
        crossButton.click();
    }
}
