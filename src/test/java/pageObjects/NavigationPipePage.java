package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pageObjects.base.BasePage;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class NavigationPipePage extends BasePage {

    public NavigationPipePage(WebDriver driver) {
        super(driver);
    }

    @FindBy(className = "breadcrumb")
    WebElement navigationPipe;

    public List<String> getElements(){
        String navigationText = navigationPipe.getText();
        if(navigationText.startsWith(">")){
            navigationText = navigationText.replaceFirst(">","");
        }
        return Arrays.stream(navigationText.split(">")).map(el->el.trim()).collect(Collectors.toList());
    }
}
