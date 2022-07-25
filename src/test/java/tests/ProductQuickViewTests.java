package tests;

import drivers.DriverFactory;
import io.qameta.allure.Allure;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import pageObjects.ProductQuickViewPage;
import pageObjects.cartPages.CartBlockProductPage;
import pageObjects.homePages.BestSellerProductPage;
import pageObjects.homePages.HomePage;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static enums.Stock.IN_STOCK_1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

public class ProductQuickViewTests extends BaseTest {

    private HomePage home;

    @BeforeEach
    public void testSetup() {
        DriverFactory driverFactory = new DriverFactory();
        driver = driverFactory.create(configuration);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(5));
        driver.navigate().to(configuration.getBaseUrl());
        driver.manage().window().maximize();

        home = new HomePage(driver);
    }

    @Test
    @DisplayName("Display basic info about product")
    public void shouldSeeBasicInfoAboutProduct() {
        BestSellerProductPage product = home.goToBestSellers().getProducts().get(0);
        String expectedName = product.getName();
        String expectedPrice = product.getPrice();
        ProductQuickViewPage productInQuickView = product.hover().quickView();
        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(productInQuickView.getName())
                .withFailMessage("Product name in quick view is different then name in best sellers")
                .isEqualTo(expectedName);
        soft.assertThat(productInQuickView.getPrice())
                .withFailMessage("Product price in quick view is different then price in best sellers")
                .isEqualTo(expectedPrice);
        soft.assertThat(productInQuickView.getReferenceNo())
                .withFailMessage("Reference number of product is empty")
                .isNotEmpty();
        soft.assertThat(productInQuickView.getCondition())
                .withFailMessage("Condition of product is empty")
                .isNotEmpty();
        soft.assertThat(productInQuickView.getDesc())
                .withFailMessage("Description of product is empty")
                .isNotEmpty();
        soft.assertThat(productInQuickView.getBigPic().isDisplayed())
                .withFailMessage("Big picture of product is not displayed")
                .isTrue();
        soft.assertThat(productInQuickView.getNumberOfThumbImages())
                .withFailMessage("There are less than 2 thumb images displayed")
                .isGreaterThan(1);
        soft.assertThat(productInQuickView.getQuantityAvailable())
                .withFailMessage("Number of available items is not positive")
                .isPositive();
        soft.assertThat(productInQuickView.getAvailability())
                .withFailMessage("Incorrect availability is displayed for in stock product")
                .isEqualTo(IN_STOCK_1.getValue());
        soft.assertAll();
        Allure.step("Assert if product has big image displayed, has reference number, condition and description not empty. " +
                "Assert if quick view product name and price are the same as in Best Sellers tab. " +
                "Assert if quick view product has at least 2 thumb images, positive quantity available and is in stock.");
    }

    @Test
    @DisplayName("Display price reduction and old price for discounted product")
    public void shouldDisplayOldPriceAndPriceReductionForDiscountedProduct() {
        BestSellerProductPage discountedProduct = home.goToBestSellers().getDiscountedProducts().get(0);
        String expectedPriceReduction = discountedProduct.getPricePercentReduction();
        String expectedOldPrice = discountedProduct.getOldPrice();
        ProductQuickViewPage productInQuickView = discountedProduct.hover().quickView();
        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(productInQuickView.getOldPrice())
                .withFailMessage("Old price of discounted product in quick view is different then in Best Sellers")
                .isEqualTo(expectedOldPrice);
        soft.assertThat(productInQuickView.getOldPrice())
                .withFailMessage("Old price of discounted product in quick view is different then in Best Sellers")
                .isEqualTo(expectedOldPrice);
        soft.assertThat(productInQuickView.getPriceReduction())
                .withFailMessage("Price reduction of discounted product in quick view is different then in Best Sellers")
                .isEqualTo(expectedPriceReduction);
        soft.assertAll();
        Allure.step("Assert if discounted product has the same old price and price reduction as in Best Sellers");
    }

    @Test
    @DisplayName("Go to product page after click on big image")
    public void shouldGoToProductPage() {
        BestSellerProductPage product = home.goToBestSellers().getProducts().get(0);
        String expectedName = product.getName();
        product.hover().quickView().clickOnBigImage();
        String title = driver.getTitle();
        assertThat(title)
                .withFailMessage("Product page title does not contain expected product name '%s'", expectedName)
                .contains(expectedName);
        Allure.step(String.format("Assert if product page title contains product name '%s'", expectedName));
    }

    @Test
    @DisplayName("Display product on Facebook")
    public void shouldDisplayProductOnFacebook() {
        home.goToBestSellers().getProducts().get(0).hover().quickView().shareOnFaceBook();
        Set<String> windowHandles = driver.getWindowHandles();
        List<String> windowTitles = new ArrayList<>();
        for (String winId : windowHandles) {
            windowTitles.add(driver.switchTo().window(winId).getTitle());
        }
        assertThat(windowTitles)
                .withFailMessage("Assert if page title contains word 'Facebook'")
                .anySatisfy(title -> assertThat(title.contains("Facebook")));
        Allure.step("Assert if Facebook opens after click on Facebook share button");
    }

    @Test
    @DisplayName("Display product on Tweeter")
    public void shouldDisplayProductOnTweeter() {
        home.goToBestSellers().getProducts().get(0).hover().quickView().shareOnTweeter();
        Set<String> windowHandles = driver.getWindowHandles();
        List<String> windowTitles = new ArrayList<>();
        for (String winId : windowHandles) {
            windowTitles.add(driver.switchTo().window(winId).getTitle());
        }
        assertThat(windowTitles)
                .withFailMessage("Assert if page title contains word 'Tweeter'")
                .anySatisfy(title -> assertThat(title.contains("Tweeter")));
        Allure.step("Assert if Tweeter opens after click on Tweeter share button");
    }

    @Test
    @DisplayName("Display product on Google Plus")
    public void shouldDisplayProductOnGoogle() {
        home.goToBestSellers().getProducts().get(0).hover().quickView().shareOnGooglePlus();
        Set<String> windowHandles = driver.getWindowHandles();
        List<String> windowTitles = new ArrayList<>();
        for (String winId : windowHandles) {
            windowTitles.add(driver.switchTo().window(winId).getTitle());
        }
        assertThat(windowTitles)
                .withFailMessage("Assert if page title contains word 'Google'")
                .anySatisfy(title -> assertThat(title.contains("Google")));
        Allure.step("Assert if Google Login page opens after click on Google Plus share button");
    }

    @Test
    @DisplayName("Display product on Pinterest")
    public void shouldDisplayProductOnPinterest() {
        home.goToBestSellers().getProducts().get(0).hover().quickView().shareOnPinterest();
        Set<String> windowHandles = driver.getWindowHandles();
        List<String> windowTitles = new ArrayList<>();
        for (String winId : windowHandles) {
            windowTitles.add(driver.switchTo().window(winId).getTitle());
        }
        assertThat(windowTitles)
                .withFailMessage("Assert if page title contains word 'Pinterest'")
                .anySatisfy(title -> assertThat(title.contains("Pinterest")));
        Allure.step("Assert if Pinterest opens after click on Pinterest share button");
    }

    @Test
    @DisplayName("Add single product to cart")
    public void shouldAddSingleProductToCart() {
        BestSellerProductPage product = home.goToBestSellers().getProducts().get(0);
        String expectedName = product.getName();
        product.hover().quickView().addToCart().closeWindow();
        List<CartBlockProductPage> productsInCart = home.getCart().scrollToCart().expandCart();
        assertThat(productsInCart)
                .withFailMessage("One item of product '%s' is not in cart", expectedName)
                .extracting(p -> p.getName(), p -> p.getQuantity())
                .contains(tuple(expectedName, 1));
        Allure.step("Assert if one item of product is in cart");
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 10})
    @DisplayName("Add given number of items of product to cart")
    public void shouldAddAFewProductsToCart(int amount) {
        BestSellerProductPage product = home.goToBestSellers().getProducts().get(0);
        String expectedName = product.getName();
        product.hover().quickView().enterWantedQuantity(amount).addToCart().closeWindow();
        List<CartBlockProductPage> productsInCart = home.getCart().scrollToCart().expandCart();
        assertThat(productsInCart)
                .withFailMessage("%d items of '%s' are not in cart", amount, expectedName)
                .extracting(p -> p.getName(), p -> p.getQuantity())
                .contains(tuple(expectedName, amount));
        Allure.step(String.format("Assert if %d items of product '%s' are in cart", amount, expectedName));
    }

    @Test
    @DisplayName("Add max available amount of product to cart")
    public void shouldAddMaxAmountOfProductToCart() {
        BestSellerProductPage product = home.goToBestSellers().getProducts().get(0);
        String expectedName = product.getName();
        ProductQuickViewPage productInQuickView = product.hover().quickView();
        int maxAvailableAmount = productInQuickView.getQuantityAvailable();
        productInQuickView.enterWantedQuantity(maxAvailableAmount).addToCart().closeWindow();
        List<CartBlockProductPage> productsInCart = home.getCart().scrollToCart().expandCart();
        assertThat(productsInCart)
                .withFailMessage("Max available amount - %d - of '%s' are not in cart",
                        maxAvailableAmount, expectedName)
                .extracting(p -> p.getName(), p -> p.getQuantity())
                .contains(tuple(expectedName, maxAvailableAmount));
        Allure.step(String.format("Assert if max available amount - that is: %d - of product '%s' is in cart",
                maxAvailableAmount, expectedName));
    }

    @Test
    @DisplayName("Don't add 0 items to cart")
    public void shouldNotAddZeroItemsOfProductToCart() {
        home.goToBestSellers().getProducts().get(0).hover().quickView().enterWantedQuantity(0).addToCart();
        assertThat(home.isErrorBoxDisplayed())
                .withFailMessage("Error box is not displayed")
                .isTrue();
        Allure.step("Assert if error box is displayed");
        home.closeErrorBox();
        assertThat(home.getCart().scrollToCart().getProducts())
                .withFailMessage("Cart is not empty")
                .isEmpty();
        Allure.step("Assert if cart is empty");
    }

    @Test
    @DisplayName("Don't add fractional amount (0;1) of product to cart")
    public void shouldNotAddFractionalAmountOfProductToCart() {
        BestSellerProductPage product = home.goToBestSellers().getProducts().get(0);
        product.hover().quickView().enterWantedQuantity(0.4f).addToCart();
        assertThat(home.isErrorBoxDisplayed())
                .withFailMessage("Error box is not displayed")
                .isTrue();
        Allure.step("Assert if error box is displayed");
        home.closeErrorBox();
        assertThat(home.getCart().scrollToCart().getProducts())
                .withFailMessage("Cart is not empty")
                .isEmpty();
        Allure.step("Assert if cart is empty");
    }

    @Test
    @DisplayName("Round to floor int if factional amount of product (1;oo) is to be added to cart")
    public void shouldRoundToFloorIntForFractionalAmountOfProductToCart() {
        BestSellerProductPage product = home.goToBestSellers().getProducts().get(0);
        String expectedName = product.getName();
        int expectedAmount = 15;
        product.hover().quickView().enterWantedQuantity(15.9f).addToCart().closeWindow();
        List<CartBlockProductPage> productsInCart = home.getCart().scrollToCart().expandCart();
        assertThat(productsInCart)
                .withFailMessage("%d items of product %s are not in the cart",expectedAmount, expectedName)
                .extracting(p -> p.getName(), p -> p.getQuantity())
                .contains(tuple(expectedName, expectedAmount));
        Allure.step(String.format("Assert if %d items of '%s' are in cart", expectedAmount,expectedName));
    }

    @Test
    @DisplayName("Don't add negative amount of product to cart")
    public void shouldNotAddNegativeAmountOfProductToCart() {
        home.goToBestSellers().getProducts().get(0)
                .hover().quickView().enterWantedQuantity(-12).addToCart();
        assertThat(home.isErrorBoxDisplayed())
                .withFailMessage("Error box is not displayed")
                .isTrue();
        Allure.step("Assert if error box is displayed");
        home.closeErrorBox();
        assertThat(home.getCart().scrollToCart().getProducts())
                .withFailMessage("Cart is not empty")
                .isEmpty();
        Allure.step("Assert if cart is empty");
    }

    @ParameterizedTest
    @DisplayName("Don't add string amount value of product to cart")
    @ValueSource(strings = {"cat", "ABC_+"})
    public void shouldNotAddStringAmountValueOfProductToCart(String invalidAmount) {
        home.goToBestSellers().getProducts().get(0)
                .hover().quickView().enterWantedQuantity(invalidAmount).addToCart();
        assertThat(home.isErrorBoxDisplayed())
                .withFailMessage("Error box is not displayed")
                .isTrue();
        Allure.step("Assert if error box is displayed");
        home.closeErrorBox();
        assertThat(home.getCart().scrollToCart().getProducts())
                .withFailMessage("Cart is not empty")
                .isEmpty();
        Allure.step("Assert if cart is empty");
    }

    @Test
    @DisplayName("Don't add more items of product in one go than available to cart")
    public void shouldNotAddMoreItemsThenAvailableToCart() {
        BestSellerProductPage product = home.goToBestSellers().getProducts().get(0);
        ProductQuickViewPage productInQuickView = product.hover().quickView();
        int maxAvailableAmount = productInQuickView.getQuantityAvailable();
        productInQuickView.enterWantedQuantity(maxAvailableAmount+1).addToCart();
        assertThat(home.isErrorBoxDisplayed())
                .withFailMessage("Error box is not displayed")
                .isTrue();
        Allure.step("Assert if error box is displayed");
        home.closeErrorBox();
        assertThat(home.getCart().scrollToCart().getProducts())
                .withFailMessage("Cart is not empty")
                .isEmpty();
        Allure.step("Assert if cart is empty");
    }

    @Test
    @DisplayName("Don't add more items of product than totally available to cart")
    public void shouldNotAddMoreItemsOfProductToCartThanTotallyAvailable() {
        BestSellerProductPage product = home.goToBestSellers().getProducts().get(0);
        String productName = product.getName();
        ProductQuickViewPage productInQuickView = product.hover().quickView();
        int maxAvailableAmount = productInQuickView.getQuantityAvailable();
        int quantityInFirstPart = maxAvailableAmount-1;
        int quantityInSecondPart = 2;
        productInQuickView.enterWantedQuantity(quantityInFirstPart).addToCart().closeWindow();
        home.goToBestSellers().getProducts().get(0).hover()
                .quickView().enterWantedQuantity(quantityInSecondPart).addToCart();
        assertThat(home.isErrorBoxDisplayed())
                .withFailMessage("Error box is not displayed")
                .isTrue();
        Allure.step("Assert if error box is displayed");
        home.closeErrorBox();
        int actualQuantity = home.getCart().scrollToCart().findProductInCart(productName).getQuantity();
        assertThat(actualQuantity)
                .withFailMessage("There are %d items of product '%s', instead of %d",
                        actualQuantity, productName, quantityInFirstPart)
                .isEqualTo(quantityInFirstPart);
        Allure.step("Assert if cart has amount of products added only in first go");
    }

//    @Test
//    @DisplayName("Add product in all available colors to cart")
//    public void should() {
//
//    }

//    @Test
//    @DisplayName("")
//    public void should(){
//
//    }
//
//    @Test
//    @DisplayName("")
//    public void should(){
//
//    }
}
