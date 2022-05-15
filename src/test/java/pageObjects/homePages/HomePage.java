package pageObjects.homePages;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import pageObjects.BasePage;

@Getter
public class HomePage extends BasePage {

    private SliderPage slider;

    public HomePage(WebDriver driver) {
        super(driver);
        slider = new SliderPage(driver);
    }


}
