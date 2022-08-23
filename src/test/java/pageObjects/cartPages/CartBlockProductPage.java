package pageObjects.cartPages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pageObjects.base.BasePage;
import pageObjects.ProductPage;

import java.math.BigDecimal;

public class CartBlockProductPage extends BasePage {

    private WebElement img;
    private WebElement quantity;
    private WebElement name;
    private WebElement attributes;
    private WebElement price;
    private WebElement removeCross;

    public CartBlockProductPage(WebDriver driver, WebElement element) {
        super(driver);
        this.driver = driver;
        this.img = element.findElement(By.cssSelector("img"));
        this.name = element.findElement(By.className("cart_block_product_name"));
        this.quantity = element.findElement(By.className("quantity"));
        this.attributes = element.findElement(By.cssSelector(".product-atributes>a"));
        this.price = element.findElement(By.className("price"));
        this.removeCross = element.findElement(By.className("ajax_cart_block_remove_link"));
    }

    public boolean hasImageDisplayed() {
        return isElementDisplayed(img);
    }

    @Step("Click on product image")
    public ProductPage clickOnImg() {
        img.click();
        return new ProductPage(driver);
    }

    public String getName() {
        return name.getText().strip();
    }

    @Step("Click on product name")
    public ProductPage clickOnName() {
        name.click();
        return new ProductPage(driver);
    }

    public String getFullName() {
        return name.getAttribute("title");
    }

    public int getQuantity() {
        return Integer.valueOf(quantity.getText().strip());
    }

    public boolean areAttributesDisplayed() {
        return isElementDisplayed(attributes);
    }

    public String getAttributes() {
        return attributes.getText().strip();
    }

    @Step("Click on product attributes")
    public ProductPage clickOnAttributes() {
        attributes.click();
        return new ProductPage(driver);
    }

    public String getPrice() {
        return price.getText().strip();
    }

    public BigDecimal getPriceValue() {
        String price = getPrice().replaceAll("[^0-9.,]", "");
        return new BigDecimal(price);
    }

    @Step("Remove product from cart")
    public void removeProduct() {
        removeCross.click();
        baseWait.until(ExpectedConditions.invisibilityOf(this.name));
    }

}
