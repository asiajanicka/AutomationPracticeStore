package pageObjects.cartPages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pageObjects.BasePage;

public class CartBlockProductPage extends BasePage {

    private WebElement img;
    private WebElement quantity;
    private WebElement name;
    private WebElement attributes;
    private WebElement price;
    private WebElement removeCross;

    public CartBlockProductPage(WebDriver driver, WebElement element) {
        super(driver);
        this.driver=driver;
        this.img = element.findElement(By.cssSelector("img"));
        this.name = element.findElement(By.className("cart_block_product_name"));
        this.quantity = element.findElement(By.className("quantity"));
        this.attributes = element.findElement(By.className("product-atributes"));
        this.price = element.findElement(By.className("price"));
        this.removeCross = element.findElement(By.className("ajax_cart_block_remove_link"));
    }

    public String getFullName(){
        return name.getAttribute("title");
    }

    public int getQuantity(){
    return Integer.valueOf(quantity.getText().strip());
    }

    public void removeProduct(){
        removeCross.click();
        baseWait.until(ExpectedConditions.invisibilityOf(this.name));
    }
}
