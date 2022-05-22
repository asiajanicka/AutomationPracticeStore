package pageObjects.homePages;

import io.qameta.allure.Step;
import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pageObjects.BasePage;
import pageObjects.cartPages.ShoppingCartDropDownPage;

@Getter
public class HomePage extends BasePage {

    private SliderPage slider;
    private BestSellersPage bestSellers;
    private ShoppingCartDropDownPage cart;

    @FindBy(css = "#home-page-tabs .blockbestsellers")
    private WebElement bestSellersTab;
    @FindBy(css = "#home-page-tabs .homefeatured")
    private WebElement popularItems;
    @FindBy(css = "#home-page-tabs .active >a")
    private WebElement activeTab;
    @FindBy(css = ".tab-content .active")
    private WebElement activeTabContent;


    public HomePage(WebDriver driver) {
        super(driver);
        slider = new SliderPage(driver);
        bestSellers = new BestSellersPage(driver);
        cart = new ShoppingCartDropDownPage(driver);
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

}
