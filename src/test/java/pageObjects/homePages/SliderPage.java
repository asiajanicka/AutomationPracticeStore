package pageObjects.homePages;

import io.qameta.allure.Step;
import lombok.Getter;
import org.openqa.selenium.*;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import pageObjects.BasePage;

import java.util.List;

public class SliderPage extends BasePage {

    protected SliderPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(className = "bx-viewport")
    private WebElement viewPort;
    @Getter
    @FindBy(xpath = "//*[@id='homeslider']//li[@class='homeslider-container']")
    @CacheLookup
    private List<WebElement> elements;
    @FindBy(className = "bx-prev")
    @CacheLookup
    private WebElement prevBtn;
    @FindBy(className = "bx-next")
    @CacheLookup
    private WebElement nextBtn;
    private final By headingOfSlideInViewPortSelector = By.cssSelector(".homeslider-description h2");
    private final By descriptionOfSlideInViewPortSelector = By.cssSelector(".homeslider-description p");
    private final By imgOfSlideInViewPortSelector = By.cssSelector("a img");

    public int getNumberOfSlides() {
        return elements.size();
    }

    public WebElement getCurrentSlide() {
        return elements.stream()
                .filter(this::isElementInViewPort)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No slide in viewport"));
    }

    public String getCurrentHeading() {
        return getCurrentSlide().findElement(headingOfSlideInViewPortSelector)
                .getText();
    }

    public String getCurrentDescription() {
        return getCurrentSlide().findElement(descriptionOfSlideInViewPortSelector)
                .getText();
    }

    public String getCurrentImg() {
        return getCurrentSlide()
                .findElement(imgOfSlideInViewPortSelector).getAttribute("src");
    }

    public boolean hasCurrentSlideHyperLink() {
        boolean hasLink;
        try {
            hasLink = !getCurrentSlide().findElement(By.tagName("a")).getAttribute("href").isEmpty();
        } catch (Exception e) {
            hasLink = false;
        }
        return hasLink;
    }

    public boolean isShopNowBtnClickable() {
        return getCurrentSlide().findElement(By.className("btn")).isEnabled();
    }

    @Step("Go to next slide")
    public void goToNextSlide() {
        nextBtn.click();
    }

    @Step("Go to previous slide")
    public SliderPage goToPreviousSlide() {
        prevBtn.click();
        return this;
    }

    public boolean isElementInViewPort(WebElement element) {
        Dimension elementSize = element.getSize();
        Point elementLocation = element.getLocation();
        Dimension viewPortSize = viewPort.getSize();
        Point viewPortLocation = viewPort.getLocation();

        return (elementLocation.getX() >= viewPortLocation.getX()) &&
                (elementLocation.getY() >= viewPortLocation.getY()) &&
                (elementSize.getWidth() + elementLocation.getX() <= viewPortSize.getWidth() + viewPortLocation.getX()) &&
                (elementSize.getHeight() + elementLocation.getY() <= viewPortSize.getHeight() + viewPortLocation.getY());
    }

    public boolean isElementInViewPort(int i) {
        return isElementInViewPort(elements.get(i));
    }
}
