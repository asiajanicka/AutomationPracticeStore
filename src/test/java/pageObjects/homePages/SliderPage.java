package pageObjects.homePages;

import io.qameta.allure.Step;
import lombok.Getter;
import org.openqa.selenium.*;
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
    @FindBy(xpath = "//*[@id='homeslider']//li[@class='homeslider-container']" )
    private List<WebElement> elements;
    @FindBy(className = "bx-prev")
    private WebElement prevBtn;
    @FindBy(className = "bx-next")
    private WebElement nextBtn;

    public int getNumberOfSlides(){
        return elements.size();
    }

    public WebElement getCurrentSlide(){
        return elements.stream()
                .filter(e-> isElementInViewPort(e))
                .findFirst()
                .orElseThrow(()-> new IllegalStateException("No slide in viewport"));
    }

    public String getCurrentHeading(){
        return getCurrentSlide().findElement(By.className("homeslider-description"))
                .findElement(By.tagName("h2"))
                .getText();
    }

    public String getCurrentDescription(){
        return getCurrentSlide().findElement(By.className("homeslider-description"))
                .findElement(By.tagName("p"))
                .getText();
    }

    public String getCurrentImg(){
        return getCurrentSlide()
                .findElement(By.tagName("a")).findElement(By.tagName("img")).getAttribute("src");
    }

    public boolean hasCurrentSlideHyperLink(){
        boolean hasLink = false;
        try {
           hasLink = !getCurrentSlide().findElement(By.tagName("a")).getAttribute("href").isEmpty();
        } catch (Exception e){
        }
        return hasLink;
    }

    public boolean isShopNowBtnClickable(){
       return getCurrentSlide().findElement(By.className("btn")).isEnabled();
    }

    @Step("Go to next slide")
    public void goToNextSlide(){
        nextBtn.click();
    }

    @Step("Go to previous slide")
    public SliderPage goToPreviousSlide(){
        prevBtn.click();
        return this;
    }

    public boolean isElementInViewPort(WebElement element){
        Dimension elementSize = element.getSize();
        Point elementLocation = element.getLocation();
        Dimension viewPortSize = viewPort.getSize();
        Point viewPortLocation = viewPort.getLocation();

        return (elementLocation.getX() >= viewPortLocation.getX())  &&
                (elementLocation.getY() >= viewPortLocation.getY()) &&
                (elementSize.getWidth() + elementLocation.getX() <= viewPortSize.getWidth() + viewPortLocation.getX()) &&
                (elementSize.getHeight() + elementLocation.getY() <= viewPortSize.getHeight() + viewPortLocation.getY());
    }

    public boolean isElementInViewPort(int i){
        return isElementInViewPort(elements.get(i));
    }
}
