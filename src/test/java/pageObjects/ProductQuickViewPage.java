package pageObjects;

import io.qameta.allure.Step;
import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageObjects.homePages.HomePage;
import pageObjects.homePages.LayerCartPage;

import java.time.Duration;
import java.util.List;

public class ProductQuickViewPage extends BasePage {
    public ProductQuickViewPage(WebDriver driver) {
        super(driver);
        loadQuickView();
        driver.switchTo().frame(1);
    }

    private By overlayLoaderLocator = By.id("fancybox-loading");
    @FindBy(css = "#product .pb-center-column [itemprop='name']")
    @CacheLookup
    private WebElement name;
    @FindBy(id = "bigpic")
    @Getter
    private WebElement bigPic;
    @FindBy(css = "li[id^='thumbnail_']")
    @CacheLookup
    @Getter
    private List<WebElement> thumbPics;
    @FindBy(css = "p[id='product_reference'] span")
    @CacheLookup
    private WebElement referenceNo;
    @FindBy(css = "p[id='product_condition'] span")
    @CacheLookup
    private WebElement condition;
    @FindBy(id = "short_description_content")
    @CacheLookup
    private WebElement desc;
    @FindBy(id = "quantityAvailable")
    @CacheLookup
    private WebElement quantityAvailable;
    @FindBy(id = "availability_value")
    @CacheLookup
    private WebElement availability;
    @FindBy(css = "button[data-type='twitter']")
    @CacheLookup
    private WebElement tweetBtn;
    @FindBy(css = "button[data-type='facebook']")
    @CacheLookup
    private WebElement facebookBtn;
    @FindBy(css = "button[data-type='google-plus']")
    @CacheLookup
    private WebElement googlePlusBtn;
    @FindBy(css = "button[data-type='pinterest']")
    @CacheLookup
    private WebElement pinterestBtn;
    @FindBy(id = "our_price_display")
    @CacheLookup
    private WebElement price;
    @FindBy(id = "reduction_percent_display")
    @CacheLookup
    private WebElement priceReduction;
    @FindBy(id = "old_price")
    @CacheLookup
    private WebElement oldPrice;
    @FindBy(id = "quantity_wanted")
    private WebElement quantityWanted;
    @FindBy(className = "icon-minus")
    @CacheLookup
    private WebElement increaseQuantityBtn;
    @FindBy(className = "icon-plus")
    @CacheLookup
    private WebElement decreaseQuantityBtn;
    @FindBy(id = "group_1")
    private WebElement sizeDropDown;
    @FindBy(css = "a[id^='color_']")
    private List<WebElement> colors;
    @FindBy(css = "#color_to_pick_list .selected a")
    private WebElement selectedColor;
    @FindBy(id = "add_to_cart")
    @CacheLookup
    private WebElement addToCartBtn;
    @FindBy(id = "fancybox-close")
    @CacheLookup
    private WebElement closeFancyBoxBtn;
    @FindBy(className = "fancybox-error")
    @Getter
    private WebElement errorBox;

    public ProductQuickViewPage loadQuickView(){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.presenceOfElementLocated(overlayLoaderLocator));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(overlayLoaderLocator));
        return this;
    }

    public String getName() {
        return name.getText();
    }

    @Step("Click on big image")
    public ProductPage clickOnBigImage(){
        bigPic.click();
        return new ProductPage(driver);
    }
    public int getNumberOfThumbImages(){
        return thumbPics.size();
    }
    public String getReferenceNo() {
        return referenceNo.getText();
    }
    public String getCondition(){
        return condition.getText();
    }
    public String getDesc(){
        return desc.getText();
    }
    public int getQuantityAvailable(){
        return Integer.parseInt(quantityAvailable.getText().strip());
    }
    public String getAvailability(){
        return availability.getText().strip();
    }
    @Step("Share on Tweeter")
    public void shareOnTweeter(){
     tweetBtn.click();
    }
    @Step("Share on Facebook")
    public void shareOnFaceBook(){
        facebookBtn.click();
    }
    @Step("Share on Google Plus")
    public void shareOnGooglePlus(){
        googlePlusBtn.click();
    }
    @Step("Share on Pinterest")
    public void shareOnPinterest(){
        pinterestBtn.click();
    }
    public String getPrice(){
        return price.getText().strip();
    }
    public String getOldPrice(){
        return oldPrice.getText().strip();
    }
    public String getPriceReduction(){return priceReduction.getText().strip();}
    public int getQuantityWanted(){
       return Integer.parseInt(quantityWanted.getText().strip());
    }
    @Step("Increase wanted quantity with + btn")
    public ProductQuickViewPage increaseWantedQuantityByOne(){
        increaseQuantityBtn.click();
        return this;
    }
    @Step("Decrease wanted quantity with + btn")
    public ProductQuickViewPage decreaseWantedQuantityByOne(){
        decreaseQuantityBtn.click();
        return this;
    }
    @Step("Enter wanted quantity")
    public ProductQuickViewPage enterWantedQuantity(int quantity){
        quantityWanted.clear();
        quantityWanted.sendKeys(String.valueOf(quantity));
        return this;
    }

    @Step("Enter wanted quantity")
    public ProductQuickViewPage enterWantedQuantity(float quantity){
        quantityWanted.clear();
        quantityWanted.sendKeys(String.valueOf(quantity));
        return this;
    }

    @Step("Enter wanted quantity")
    public ProductQuickViewPage enterWantedQuantity(String quantity){
        quantityWanted.clear();
        quantityWanted.sendKeys(quantity);
        return this;
    }

    @Step("Set size")
    public ProductQuickViewPage setSize(String size){
        Select select = new Select(sizeDropDown);
        select.selectByVisibleText(size);
        return this;
    }
    @Step("Set color of product by number")
    public ProductQuickViewPage setColor(int number){
        String color = colors.get(number).getAttribute("name");
        colors.get(number).click();
        return this;
    }
    public String getSelectedColor(){
       return selectedColor.getAttribute("name").strip();
    }
    @Step("Add product to cart")
    public LayerCartPage addToCart(){
        addToCartBtn.click();
        driver.switchTo().defaultContent();
        return new LayerCartPage(driver);
    }
    @Step("Close quick view")
    public HomePage close(){
        closeFancyBoxBtn.click();
        driver.switchTo().defaultContent();
        return new HomePage(driver);
    }
}
