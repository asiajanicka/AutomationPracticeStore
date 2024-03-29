package pageObjects.homePages;

import io.qameta.allure.Step;
import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pageObjects.base.BasePage;
import pageObjects.cartPages.ShoppingCartDropDownPage;
import pageObjects.topMenuPages.TopMenuPage;

@Getter
public class HomePage extends BasePage {

    private final SliderPage slider;
    private final BestSellersPage bestSellers;
    private final ShoppingCartDropDownPage cart;

    private final TopMenuPage topMenu;

    @FindBy(css = "#home-page-tabs .blockbestsellers")
    @CacheLookup
    private WebElement bestSellersTab;
    @FindBy(css = "#home-page-tabs .homefeatured")
    @CacheLookup
    private WebElement popularItems;
    @FindBy(css = "#home-page-tabs .active >a")
    private WebElement activeTab;
    @FindBy(css = ".tab-content .active")
    private WebElement activeTabContent;
    @FindBy(className = "fancybox-error")
    @Getter
    private WebElement errorBox;
    @FindBy(className = "fancybox-close")
    private WebElement closeErrorBoxCross;

    public HomePage(WebDriver driver) {
        super(driver);
        slider = new SliderPage(driver);
        bestSellers = new BestSellersPage(driver);
        cart = new ShoppingCartDropDownPage(driver);
        topMenu =  new TopMenuPage(driver);
    }

    @Step("Go to Best Sellers")
    public BestSellersPage goToBestSellers(){
        if (!getActiveTabName().equals(bestSellersTab.getText().strip())) {
            bestSellersTab.click();
        }
        return new BestSellersPage(driver);
    }

    @Step("Go to Popular Items")
    public PopularItemsPage goToPopularItems(){
        popularItems.click();
        return new PopularItemsPage(driver);
    }

    public String getActiveTabName(){
        return activeTab.getText().strip();
    }

    public String getActiveTabContentId(){
        return activeTabContent.getAttribute("id");
    }

    @Step("Get Popular Items")
    public PopularItemsPage getPopularItems(){
        return new PopularItemsPage(driver);
    }

    public boolean isErrorBoxDisplayed(){
        return isElementDisplayed(errorBox);
    }

    public HomePage closeErrorBox(){
        baseWait.until(ExpectedConditions.visibilityOf(closeErrorBoxCross));
        closeErrorBoxCross.click();
        return this;
    }
}
