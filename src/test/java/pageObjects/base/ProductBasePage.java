package pageObjects.base;

import io.qameta.allure.Step;
import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import pageObjects.base.BasePage;
import pageObjects.homePages.BestSellerProductPage;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class ProductBasePage extends BasePage {
    protected WebElement container;
    protected WebElement img;
    protected WebElement name;
    @Getter
    protected String price;
    @Getter
    protected String oldPrice;
    @Getter
    protected String pricePercentReduction;

    public ProductBasePage(WebDriver driver,
                           WebElement container,
                           WebElement img,
                           WebElement name,
                           String price,
                           String oldPrice,
                           String pricePercentReduction
    ) {
        super(driver);

        this.container = container;
        this.img = img;
        this.name = name;
        this.price = price;
        this.oldPrice = oldPrice;
        this.pricePercentReduction = pricePercentReduction;
    }

    public ProductBasePage (WebDriver driver, WebElement product){
        super(driver);
        this.container = product.findElement(By.className("product-container"));
        this.img = product.findElement(By.className("product_img_link"));
        this.name = product.findElement(By.className("product-name"));
        this.price = product.findElement(By.cssSelector(".right-block .price")).getText();
        this.oldPrice = checkOldPrice(product);
        this.pricePercentReduction = checkPriceReduction(product);
    }

    private static String checkOldPrice(WebElement product) {
        try {
            return product.findElement(By.cssSelector(".right-block .old-price")).getText();
        } catch (NoSuchElementException e) {
            return "";
        }
    }

    private static String checkPriceReduction(WebElement product) {
        try {
            return product.findElement(By.cssSelector(".right-block .price-percent-reduction")).getText();
        } catch (NoSuchElementException e) {
            return "";
        }
    }

//    @Step("Hover on product")
//    public BestSellerHoveredProductPage getProductOnHover() {
//        JavascriptExecutor js = (JavascriptExecutor) driver;
//        js.executeScript("arguments[0].scrollIntoView();", container);
//        Actions action = new Actions(driver);
//        action.moveByOffset(1,1);
//        action.moveToElement(container).perform();
//        return new BestSellerHoveredProductPage(driver);
//    }

    public String getImgStr() {
        return img.getAttribute("src");
    }

    public boolean hasImgHyperLink(){
        return hasHyperLink(img);
    }

    @Step("Click on image")
    public pageObjects.ProductPage clickOnImg() {
        // trying to click on img directly will click on "Quick view" button that is in the middle of img
        // for this reason click on img must be removed by offset
        Actions action = new Actions(driver);
        int width = img.getSize().getWidth();
        action.moveToElement(img).moveByOffset((width/2)-2,10).click().perform();
        return new pageObjects.ProductPage(driver);
    }

    public String getName() {
        return name.getText().strip();
    }

    public boolean hasNameHyperLink(){
        return hasHyperLink(name);
    }

    @Step("Click on name")
    public pageObjects.ProductPage clickOnName() {
        name.click();
        return new pageObjects.ProductPage(driver);
    }

    public BigDecimal getPriceValue() {
        String price = getPrice().replaceAll("[^\\d.,]", "");
        return new BigDecimal(price);
    }

    public BigDecimal getOldPriceValue() {
        if(!oldPrice.isEmpty()) {
            String price = getOldPrice().replaceAll("[^\\d.,]", "");
            return new BigDecimal(price);
        } else throw new IllegalArgumentException("This product is not discounted so it doesn't have an old price");
    }

    public boolean hasHyperLink(WebElement element){
        boolean isLinkPresent;
        try{
            isLinkPresent = !element.getAttribute("href").isEmpty();
        } catch (Exception e){
            isLinkPresent = false;
        }
        return isLinkPresent;
    }

    @Override
    public String toString() {
        return "{" +
                "name=" + name.getText() +
                ", price=" + price +
                ", old price=" + oldPrice +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BestSellerProductPage)) return false;
        BestSellerProductPage that = (BestSellerProductPage) o;
        return Objects.equals(getName(), that.getName()) && Objects.equals(getPrice(), that.getPrice());
    }
}
