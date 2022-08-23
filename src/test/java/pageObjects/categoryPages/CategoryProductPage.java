package pageObjects.categoryPages;

import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import pageObjects.base.ProductBasePage;

import java.util.List;

public class CategoryProductPage extends ProductBasePage {

    private List<WebElement> colors;
    @Getter
    private String availability;

    public CategoryProductPage(WebDriver driver,
                               WebElement container,
                               WebElement img,
                               WebElement name,
                               String price,
                               String oldPrice,
                               String pricePercentReduction,
                               List<WebElement> colors,
                               String availability) {
        super(driver, container, img, name, price, oldPrice, pricePercentReduction);
        this.colors = colors;
        this.availability = availability;
    }

    public CategoryProductPage(WebDriver driver, WebElement product){
        super(driver, product);
        this.colors = product.findElements(By.cssSelector("a[id^='color_']"));
        this.availability = product.findElement(By.cssSelector(".available-now")).getText();
    }
}
