package pageObjects.cartPages;

import io.qameta.allure.Step;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import pageObjects.BasePage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ShoppingCartDropDownPage extends BasePage {
    public ShoppingCartDropDownPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(css = ".shopping_cart a")
    private WebElement cart;
    @FindBy(className = "cart_block_list")
    private WebElement cartContainer;
    @FindBy(css = ".cart_block_list .products dt")
    private List<WebElement> products;
    @FindBy(className = "cart_block_total")
    private WebElement totalCost;

    @Step("Scroll to cart")
    public ShoppingCartDropDownPage scrollToCart(){
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView();", cart);
        return this;
    }

    @Step("Expand dropdown cart")
    public List<CartBlockProductPage> expandCart(){
        return scrollToCart().hoverOnCart().getProducts();
    }

    @Step("Hover on cart")
    public ShoppingCartDropDownPage hoverOnCart() {
        Actions action = new Actions(driver);
        action.moveToElement(cart).perform();
        isElementDisplayed(totalCost);
        return this;
    }

    @Step("Get products")
    public List<CartBlockProductPage> getProducts(){
        List<CartBlockProductPage> list = new ArrayList<>();
        for(WebElement element: products){
            list.add(new CartBlockProductPage(driver, element));
        }
        return list;
    }

    @Step("Find product {0} in cart")
    public CartBlockProductPage findProductInCart(String fullName, String price){
        List<CartBlockProductPage> products = expandCart().stream().filter(el -> (el.getFullName().equals(fullName))
        ).collect(Collectors.toList());
        if(products.size()==1) return products.get(0);
        if(products.isEmpty()) return null;
        else throw new IllegalStateException("There are more then one product with given name in cart");
    }

}
