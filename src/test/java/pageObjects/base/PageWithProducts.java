package pageObjects.base;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import pageObjects.ProductPage;
import pageObjects.categoryPages.CategoryProductPage;

import java.util.ArrayList;
import java.util.List;

public class PageWithProducts extends BasePage{
    protected PageWithProducts(WebDriver driver) {
        super(driver);
    }

    public ProductBasePage getBaseProduct( WebElement product){
            WebElement container = product.findElement(By.className("product-container"));
            WebElement img = product.findElement(By.className("product_img_link"));
            WebElement name = product.findElement(By.className("product-name"));
            String price = product.findElement(By.cssSelector(".right-block .price")).getText();
            String oldPrice = checkOldPrice(product);
            String pricePercentReduction = checkPriceReduction(product);
            List<WebElement> colors = product.findElements(By.cssSelector("a[id^='color_']"));
            String availability = product.findElement(By.className("available-now")).getText();
            return new ProductBasePage(driver,
                    container, img, name, price, oldPrice, pricePercentReduction);

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
}
