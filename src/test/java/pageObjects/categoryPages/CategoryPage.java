package pageObjects.categoryPages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pageObjects.base.BasePage;

import java.util.ArrayList;
import java.util.List;

public class CategoryPage extends BasePage {

    public CategoryPage(WebDriver driver) {
        super(driver);
        baseWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".page-heading")));
    }

    @FindBy(css = ".page-heading>.cat-name")
    WebElement categoryName;
    @FindBy(css = ".heading-counter")
    WebElement productCounter;

    @FindBy(css = ".ajax_block_product")
    private List<WebElement> products;

    public String getCategoryName(){
        return categoryName.getText().strip();
    }

    public List<CategoryProductPage> getProducts(){
        List productsParsed = new ArrayList<CategoryProductPage>();
        for(WebElement product: products)
            productsParsed.add(new CategoryProductPage(driver, product));
        return productsParsed;
    }

    public int getNumberOfProductsFromProductCounter(){
        return Integer.parseInt(productCounter.getText().replaceAll("\\D",""));
    }
}
