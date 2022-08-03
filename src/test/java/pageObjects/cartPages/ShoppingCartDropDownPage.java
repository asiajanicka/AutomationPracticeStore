package pageObjects.cartPages;

import io.qameta.allure.Step;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import pageObjects.BasePage;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ShoppingCartDropDownPage extends BasePage {
    public ShoppingCartDropDownPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(css = ".shopping_cart>a")
    @CacheLookup
    private WebElement cart;
    @FindBy(className = "cart_block_list")
    private WebElement cartContainer;
    @FindBy(className = "ajax_cart_quantity")
    private WebElement quantity;
    @FindBy(css = ".shopping_cart .ajax_cart_product_txt ")
    private WebElement productLabel;
    @FindBy(css = ".shopping_cart .ajax_cart_product_txt_s")
    private WebElement productsLabel;
    @FindBy(className = "ajax_cart_no_product")
    private WebElement emptyLabel;
    @FindBy(css = ".cart_block_list .products dt")
    private List<WebElement> products;
    @FindBy(className = "cart_block_shipping_cost")
    private WebElement shippingCost;
    @FindBy(className = "cart_block_total")
    private WebElement totalCost;
    @FindBy(id = "button_order_cart")
    @CacheLookup
    private WebElement checkOutBth;

    @Step("Scroll to cart")
    public ShoppingCartDropDownPage scrollToCart() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView();", cart);
        return this;
    }

    @Step("Expand dropdown cart")
    public List<CartBlockProductPage> expandCart() {
        return scrollToCart().hoverOnCart().getProducts();
    }

    @Step("Hover on cart")
    public ShoppingCartDropDownPage hoverOnCart() {
        Actions action = new Actions(driver);
        action.moveToElement(cart).perform();
        isElementDisplayed(totalCost);
        return this;
    }

    public List<CartBlockProductPage> getProducts() {
        List<CartBlockProductPage> list = new ArrayList<>();
        for (WebElement element : products) {
            list.add(new CartBlockProductPage(driver, element));
        }
        return list;
    }

    @Step("Find product {0} in cart")
    public CartBlockProductPage findProductInCartByName(String fullName) {
        List<CartBlockProductPage> products = expandCart().stream().filter(el -> (el.getFullName().equals(fullName))
        ).collect(Collectors.toList());
        if (products.size() == 1) return products.get(0);
        if (products.isEmpty()) return null;
        else throw new IllegalStateException("There are more then one product with given name in cart");
    }

    @Step("Find products {0} in cart")
    public List<CartBlockProductPage> findProductsInCartByName(String fullName) {
        return expandCart().stream().filter(el -> (el.getFullName().equals(fullName))
        ).collect(Collectors.toList());
    }

    public CartPage clickOnCartBar() {
        cart.click();
        isCartContainerCollapsed();
        return new CartPage(driver);
    }

    public boolean isCartContainerDisplayed() {
        return isElementDisplayed(totalCost);
    }

    public boolean isCartContainerCollapsed() {
        return isElementRemoved(cartContainer);
    }

    public String getLabel() {
        if (isElementDisplayed(emptyLabel)) return emptyLabel.getText().strip();
        else if (isElementDisplayed(productLabel)) return productLabel.getText().strip();
        else if (isElementDisplayed(productsLabel)) return productsLabel.getText().strip();
        else throw new IllegalStateException("None of defined labels is displayed");
    }

    public int getQuantity() {
        return Integer.parseInt(quantity.getText().strip());
    }

    public String getShippingCost() {
        return shippingCost.getText().strip();
    }

    public BigDecimal getShippingCostValue() {
        String cost = getShippingCost().replaceAll("[^\\d.,]", "");
        return new BigDecimal(cost);
    }

    public String getTotalCost() {
        return totalCost.getText().strip();
    }

    public BigDecimal getTotalCostValue() {
        String cost = getTotalCost().replaceAll("[^\\d.,]", "");
        return new BigDecimal(cost);
    }

    @Step("Click on checkout")
    public CartPage checkOut() {
        checkOutBth.click();
        isCartContainerCollapsed();
        return new CartPage(driver);
    }
}
