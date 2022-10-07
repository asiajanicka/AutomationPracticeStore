package pageObjects.homePages;

import io.qameta.allure.Step;
import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import pageObjects.base.BasePage;
import pageObjects.ProductPage;
import pageObjects.ProductQuickViewPage;
import pageObjects.base.Product;
import pageObjects.base.ProductBasePage;

public class BestSellerHoveredProductPage extends BasePage {

    protected BestSellerHoveredProductPage(WebDriver driver) {
        super(driver);
        this.oldPrice = checkOldPrice();
        this.pricePercentageReduction = checkPriceReduction();
    }

    @FindBy(css = "#blockbestsellers .hovered")
    @CacheLookup
    private WebElement hoveredProduct;
    @FindBy(css = "#blockbestsellers .hovered .quick-view")
    @CacheLookup
    private WebElement quickViewBtn;
    @FindBy(css = "#blockbestsellers .hovered .left-block .price")
    @CacheLookup
    private WebElement price;
    @Getter
    private String oldPrice;
    @Getter
    private String pricePercentageReduction;
    @FindBy(css = "#blockbestsellers .hovered .content_price")
    @CacheLookup
    private WebElement availability;
    @FindBy(css = "#blockbestsellers .hovered .right-block a[title='Add to cart']")
    @CacheLookup
    private WebElement addToCartBtn;
    @FindBy(css = "#blockbestsellers .hovered .right-block a[title='View']")
    @CacheLookup
    private WebElement moreBtn;


    private String checkOldPrice() {
        try {
            return hoveredProduct.findElement(By.cssSelector(".left-block .old-price")).getText().strip();
        } catch (NoSuchElementException e) {
            return "";
        }
    }

    private String checkPriceReduction() {
        try {
            return hoveredProduct.findElement(By.cssSelector(".left-block .price-percent-reduction")).getText().strip();
        } catch (NoSuchElementException e) {
            return "";
        }
    }

    @Step("Click \"Quick view\"")
    public ProductQuickViewPage quickView() {
        quickViewBtn.click();
        return new ProductQuickViewPage(driver);
    }

    public String getPrice() {
        return price.getText().strip();
    }

    public String getAvailability(String currency) {
        String temp = String.format("\\%s", currency);
        return availability.getText()
                .replaceAll(temp, "")
                .replaceAll("[0-9.%-]", "")
                .strip();
    }

    @Step("Add product to cart")
    public LayerCartPage addToCart() {
        addToCartBtn.click();
        return new LayerCartPage(driver);
    }

    @Step("Click \"More\"")
    public ProductPage viewMore() {
        moreBtn.click();
        return new ProductPage(driver);
    }
}
