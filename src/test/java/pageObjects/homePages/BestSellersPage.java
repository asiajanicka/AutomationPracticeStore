package pageObjects.homePages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import pageObjects.BasePage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BestSellersPage extends BasePage {

    WebDriver driver;
    protected BestSellersPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
    }

    @FindBy(css = "#blockbestsellers .ajax_block_product")
    @CacheLookup
    private List<WebElement> bestSellers;

    @Step("Get products in Best Sellers")
    public List<BestSellerProductPage> getProducts(){
        List products = new ArrayList<BestSellerProductPage>();
        for(WebElement product: bestSellers){
            WebElement container = product.findElement(By.className("product-container"));
            WebElement img = product.findElement(By.className("product_img_link"));
            WebElement name = product.findElement(By.className("product-name"));
            String price = product.findElement(By.cssSelector(".right-block .price")).getText();
            String oldPrice = checkOldPrice(product);
            String pricePercentReduction = checkPriceReduction(product);
            BestSellerProductPage bestSeller =  new BestSellerProductPage(driver,
                    container, img, name, price, oldPrice, pricePercentReduction);
            products.add(bestSeller);
        }
        return products;
    }

    private static String checkOldPrice(WebElement product) {
        try {
            return product.findElement(By.cssSelector(".right-block .old-price")).getText();
        } catch (NoSuchElementException e) {
            return "";
        }
    }

    private static String checkPriceReduction(WebElement product) {
        try {
            return product.findElement(By.cssSelector(".right-block .price-percent-reduction")).getText();
        } catch (NoSuchElementException e) {
            return "";
        }
    }

    @Step("Get discounted products in Best Sellers")
    public List<BestSellerProductPage> getDiscountedProducts(){
        return getProducts()
                .stream()
                .filter(p->!p.getOldPrice().isEmpty())
                .collect(Collectors.toList());
    }

    @Step("Get products with price percent reduction in Best Sellers")
    public List<BestSellerProductPage> getDiscountedProductsWithPricePercentReduction(){
        return getProducts()
                .stream()
                .filter(p->!p.getPricePercentReduction().isEmpty())
                .collect(Collectors.toList());
    }

    @Step("Get product {0}")
    public BestSellerProductPage getProduct(String name){
        return getProducts()
                .stream()
                .filter(p->p.getName().equals(name))
                .findFirst()
                .orElseThrow(()->new IllegalArgumentException("There is no product with given name"));
    }

}
