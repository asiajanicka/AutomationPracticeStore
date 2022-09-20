package pageObjects.topMenuPages;

import io.qameta.allure.Step;
import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import pageObjects.base.BasePage;
import pageObjects.categoryPages.CategoryPage;

import java.util.ArrayList;
import java.util.List;

public class MenuCategoryPage extends BasePage {

    private WebElement category;
    private String subcategoryLocator = "./ul/li";
    @Getter
    private List<MenuCategoryPage> subCategories;

    public MenuCategoryPage(WebDriver driver, WebElement element){
        super(driver);
        category = element.findElement(By.cssSelector("a"));
        List<WebElement> elements = element.findElements(By.xpath(subcategoryLocator));
        subCategories = new ArrayList<>();
        for (WebElement el: elements){
            subCategories.add(new MenuCategoryPage(driver, el));
        }
    }

    public String getName(){
        return category.getText().strip();
    }

    @Step("Find subcategory '{0}'")
    public MenuCategoryPage getSubcategory(String name){
        return subCategories
                .stream()
                .filter(sub ->sub.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(()->new IllegalArgumentException("There is no such category"));
    }

    @Step("Go to page of subcategory")
    public CategoryPage goToPage(){
        category.click();
        return new CategoryPage(driver);
    }

    @Override
    public String toString() {
        return "MenuCategoryPage{" +
                "category=" + getName() +
                '}';
    }
}
