package pageObjects.categoryPages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pageObjects.base.ProductHoveredBasePage;

import java.util.List;

public class CategoryProductHoveredPage extends ProductHoveredBasePage {

    @FindBy(css = "li[class*='ajax_block_product'][class*='hovered'] a[id*='color']")
    private List<WebElement> colors;
    @FindBy(css = "li[class*='ajax_block_product'][class*='hovered'] .available-now")
    private WebElement availabilityLow;

    protected CategoryProductHoveredPage(WebDriver driver) {
        super(driver);
    }

    public String getAvailabilityLow(){
        return availabilityLow.getText();
    }

}
