package pageObjects.categoryPages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pageObjects.base.BasePage;

public class CategoriesLeftBlock extends BasePage {
    protected CategoriesLeftBlock(WebDriver driver) {
        super(driver);
    }

    @FindBy(id = "categories_block_left")
    private WebElement categoriesBlock;
    @FindBy(css = "#categories_block_left .title_block")
    private WebElement categoriesBlockTitle;

    public String getCategoriesBlockTitle(){
        return categoriesBlockTitle.getText().strip();
    }

    public boolean isCategoriesBlockDisplayed(){
        return isElementDisplayed(categoriesBlock);
    }
}
