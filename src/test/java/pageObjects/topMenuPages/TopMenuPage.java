package pageObjects.topMenuPages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pageObjects.BasePage;
import pageObjects.categoryPages.CategoryPage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class TopMenuPage extends BasePage {
    public TopMenuPage(WebDriver driver) {
        super(driver);
    }
    @FindBy(id = "block_top_menu")
    @CacheLookup
    private WebElement topMenu;
    @FindBy(css = "#block_top_menu>ul>li[class='sfHover'] .submenu-container" )
    private WebElement submenuContainer;
    @FindBy(css = "#block_top_menu .sfHover li a[href]")
    private List<WebElement> expandedMenuCategories;
   private String expandedSubcategoriesLocator = "./ul/li[not(contains(@class,'category-thumbnail'))]";

    @FindBy(css = "#block_top_menu>ul>li:nth-of-type(1)")
    @CacheLookup
    private WebElement women;
    @FindBy(css = "#block_top_menu>ul>li:nth-of-type(2)")
    @CacheLookup
    private WebElement dresses;
    @FindBy(css = "#block_top_menu>ul>li:nth-of-type(3)")
    @CacheLookup
    private WebElement tShirts;
    @FindBy(css = "#block_top_menu>ul>li:nth-of-type(4)")
    @CacheLookup
    private WebElement blog;

    @Step("Scroll to top menu")
    public TopMenuPage scrollToMenu() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView();", topMenu);
        return this;
    }

    @Step("Hover on WOMEN menu")
    public TopMenuPage hoverOnWomenMenu(){
        return hoverOnExpandableMenuElement(women);
    }

    @Step("Hover on DRESSES Menu")
    public TopMenuPage hoverOnDressesMenu(){
        return hoverOnExpandableMenuElement(dresses);
    }

    private TopMenuPage hoverOnExpandableMenuElement(WebElement element){
        Actions action = new Actions(driver);
        action.moveToElement(element).build().perform();
        baseWait.until(ExpectedConditions.visibilityOf(submenuContainer));
        return this;
    }

    public List<String> getCategoriesNames(){
        return expandedMenuCategories
                .stream()
                .map(el->el.getText())
                .collect(Collectors.toList());
    }

    @Step("Go to Women category page")
    public CategoryPage goToWomenPage() {
        women.click();
        return new CategoryPage(driver);
    }

    @Step("Go to Dresses category page")
    public CategoryPage goToDressesPage() {
        dresses.click();
        return new CategoryPage(driver);
    }

    @Step("Go to T-shirts category page")
    public CategoryPage goToTShirtsPage() {
        tShirts.click();
        return new CategoryPage(driver);
    }

    @Step("Go to BLOG page")
    public void goToBlogPage() {
        blog.click();
    }

    @Step("Find subcategory '{0}' in Women menu")
    public MenuCategoryPage getWomenSubcategory(String name) {
        return getWomenSubcategories()
                .stream()
                .filter(cat -> cat.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("There is no such a category"));
    }

    @Step("Get Women subcategories")
    public List<MenuCategoryPage> getWomenSubcategories() {
        return getSubcategories(women);
    }

    @Step("Get Dresses subcategories")
    public List<MenuCategoryPage> getDressesSubcategories() {
        return getSubcategories(dresses);
    }

    private List<MenuCategoryPage> getSubcategories(WebElement menuEl) {
        List<MenuCategoryPage> subcategories = new ArrayList<>();
        List<WebElement> elements = menuEl.findElements(By.xpath(expandedSubcategoriesLocator));
        for (WebElement el : elements) {
            MenuCategoryPage categoryPage = new MenuCategoryPage(driver, el);
            subcategories.add(categoryPage);
        }
        return subcategories;
    }
}
