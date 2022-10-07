package pageObjects.homePages;

import io.qameta.allure.Step;
import lombok.Getter;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import pageObjects.base.BasePage;
import pageObjects.ProductPage;

import java.math.BigDecimal;
import java.util.Objects;

public class BestSellerProductPage extends BasePage {

    WebElement container;
    WebElement img;
    WebElement name;
    @Getter
    String price;
    @Getter
    String oldPrice;
    @Getter
    String pricePercentReduction;

    public BestSellerProductPage(WebDriver driver,
                           WebElement container,
                           WebElement img,
                           WebElement name,
                           String price,
                           String oldPrice,
                           String pricePercentReduction
    ) {
        super(driver);
        this.driver = driver;
        this.container = container;
        this.img = img;
        this.name = name;
        this.price = price;
        this.oldPrice = oldPrice;
        this.pricePercentReduction = pricePercentReduction;
    }

    @Step("Hover on product")
    public BestSellerHoveredProductPage getProductOnHover() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", container);
        Actions action = new Actions(driver);
        action.moveByOffset(1,1);
        action.moveToElement(container).perform();
        return new BestSellerHoveredProductPage(driver);
    }

    public String getImgStr() {
        return img.getAttribute("src");
    }

    public boolean hasImgHyperLink(){
        return hasHyperLink(img);
    }

    @Step("Click on image")
    public ProductPage clickOnImg() {
        // trying to click on img directly will click on "Quick view" button that is in the middle of img
        // for this reason click on img must be removed by offset
        Actions action = new Actions(driver);
        int width = img.getSize().getWidth();
        action.moveToElement(img).moveByOffset((width/2)-2,10).click().perform();
        return new ProductPage(driver);
    }

    public String getName() {
        return name.getText().strip();
    }

    public boolean hasNameHyperLink(){
        return hasHyperLink(name);
    }

    @Step("Click on name")
    public ProductPage clickOnName() {
        name.click();
        return new ProductPage(driver);
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
        return "ProductInContentTab{" +
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
