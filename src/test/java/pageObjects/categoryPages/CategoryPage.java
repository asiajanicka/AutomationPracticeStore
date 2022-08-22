package pageObjects.categoryPages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pageObjects.BasePage;

public class CategoryPage extends BasePage {

    public CategoryPage(WebDriver driver) {
        super(driver);
        baseWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".page-heading")));
    }

    @FindBy(css = ".page-heading>.cat-name")
    WebElement categoryName;

    public String getCategoryName(){
        return categoryName.getText().strip();
    }


}
