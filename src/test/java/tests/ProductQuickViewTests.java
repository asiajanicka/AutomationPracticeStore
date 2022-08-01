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
import pageObjects.homePages.LayerCartPage;

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
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));
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

    @ParameterizedTest(name = "Add {arguments} items of product to cart")
    @ValueSource(ints = {2, 10})
    @DisplayName("Add given quantity of product to cart")
    public void shouldAddAFewItemsOfProductToCart(int amount) {
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
    @DisplayName("Add max available quantity of product to cart")
    public void shouldAddMaxQuantityOfProductToCart() {
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

    @ParameterizedTest(name = "{displayName}. Quantity: {arguments}")
    @ValueSource(strings = {"-5", "-1", "0", "-0.8", "-0,8", "-0.3", "-0,3"})
    @DisplayName("Do not add product with wanted quantity lower then 1 to cart")
    public void shouldDisplayErrorBoxWhenWantedQuantityLessThanOne(String amount) {
        home.goToBestSellers().getProducts().get(0).hover().quickView().enterWantedQuantity(amount).addToCart();
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
    @DisplayName("Round to floor int if factional quantity of product (1;oo) is to be added to cart")
    public void shouldRoundToFloorIntForFractionalQuantityOfProductToCart() {
        BestSellerProductPage product = home.goToBestSellers().getProducts().get(0);
        String expectedName = product.getName();
        int expectedAmount = 15;
        product.hover().quickView().enterWantedQuantity(15.9f).addToCart().closeWindow();
        List<CartBlockProductPage> productsInCart = home.getCart().scrollToCart().expandCart();
        assertThat(productsInCart)
                .withFailMessage("%d items of product %s are not in the cart", expectedAmount, expectedName)
                .extracting(p -> p.getName(), p -> p.getQuantity())
                .contains(tuple(expectedName, expectedAmount));
        Allure.step(String.format("Assert if %d items of '%s' are in cart", expectedAmount, expectedName));
    }

    @ParameterizedTest(name = "{displayName} : {arguments}")
    @DisplayName("Do not add invalid quantity of product to cart")
    @ValueSource(strings = {"cat", "ABC_+"})
    public void shouldDisplayErrorBoxIfInvalidQuantityToBeAddedToCart(String invalidAmount) {
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
    @DisplayName("Don't add more items of product to cart in one go than available quantity")
    public void shouldNotAddMoreItemsThenAvailableToCart() {
        BestSellerProductPage product = home.goToBestSellers().getProducts().get(0);
        ProductQuickViewPage productInQuickView = product.hover().quickView();
        int maxAvailableAmount = productInQuickView.getQuantityAvailable();
        productInQuickView.enterWantedQuantity(maxAvailableAmount + 1).addToCart();
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
        int quantityInFirstPart = maxAvailableAmount - 1;
        int quantityInSecondPart = 2;
        productInQuickView.enterWantedQuantity(quantityInFirstPart).addToCart().closeWindow();
        home.goToBestSellers().getProducts().get(0).hover()
                .quickView().enterWantedQuantity(quantityInSecondPart).addToCart();
        assertThat(home.isErrorBoxDisplayed())
                .withFailMessage("Error box is not displayed")
                .isTrue();
        Allure.step("Assert if error box is displayed");
        home.closeErrorBox();
        int actualQuantity = home.getCart().scrollToCart().findProductInCartByName(productName).getQuantity();
        assertThat(actualQuantity)
                .withFailMessage("There are %d items of product '%s', instead of %d",
                        actualQuantity, productName, quantityInFirstPart)
                .isEqualTo(quantityInFirstPart);
        Allure.step("Assert if cart has amount of products added only in first go");
    }

    @Test
    @DisplayName("Increase wanted quantity by one with plus btn")
    public void shouldIncreaseWantedQuantityByOne() {
        BestSellerProductPage product = home.goToBestSellers().getProducts().get(0);
        String productName = product.getName();
        product.hover().quickView().increaseWantedQuantityByOne().addToCart().closeWindow();
        int actualQuantity = home.getCart().scrollToCart().findProductInCartByName(productName).getQuantity();
        assertThat(actualQuantity)
                .withFailMessage("There are %d items of '%s' in cart instead of 2",
                        actualQuantity, productName)
                .isEqualTo(2);
        Allure.step(String.format("Assert if there are 2 items of product '%s' in the cart", productName));
    }

    @ParameterizedTest(name = "{displayName}. Quantity: {arguments}")
    @DisplayName("Increase wanted quantity to 1 with plus btn when entered quantity lower than 1")
    @ValueSource(strings = {"-5", "-1", "0", "-0.8", "-0,8", "-0.3", "-0,3"})
    public void shouldIncreaseWantedQuantityToOneWithPlus(String amount) {
        BestSellerProductPage product = home.goToBestSellers().getProducts().get(0);
        String productName = product.getName();
        LayerCartPage layerCartPage = product.hover().quickView().enterWantedQuantity(amount).increaseWantedQuantityByOne().addToCart();
        assertThat(home.isErrorBoxDisplayed())
                .withFailMessage("Error box is displayed")
                .isFalse();
        Allure.step("Assert if error box is not displayed");
        layerCartPage.closeWindow();
        assertThat(home.getCart().scrollToCart().getProducts())
                .withFailMessage("Cart is empty. Was product added to the cart?")
                .isNotEmpty();
        Allure.step("Assert if cart is not empty");
        int actualQuantity = home.getCart().findProductInCartByName(productName).getQuantity();
        assertThat(actualQuantity)
                .withFailMessage("There are %d items of '%s' in cart instead of 2",
                        actualQuantity, productName)
                .isEqualTo(1);
        Allure.step(String.format("Assert if there is one item of product '%s' in the cart", productName));
    }

    @Test
    @DisplayName("Increase wanted quantity to max available quantity with plus btn")
    public void shouldIncreaseQuantityToMaxValueWithPlus() {
        BestSellerProductPage product = home.goToBestSellers().getProducts().get(0);
        String productName = product.getName();
        ProductQuickViewPage productInQuickView = product.hover().quickView();
        int availableQuantity = productInQuickView.getQuantityAvailable();
        verifyQuantityInCartAfterPlusBtn(productInQuickView, availableQuantity - 1, availableQuantity);
        Allure.step("Assert if max available quantity of product is in cart");
    }

    @Test
    @DisplayName("Do not increase wanted quantity over max available amount with plus btn")
    public void shouldNotIncreaseQuantityWithPlusIfMaxAvailableEntered() {
        BestSellerProductPage product = home.goToBestSellers().getProducts().get(0);
        ProductQuickViewPage productInQuickView = product.hover().quickView();
        int availableQuantity = productInQuickView.getQuantityAvailable();
        verifyQuantityInCartAfterPlusBtn(productInQuickView, availableQuantity, availableQuantity);
        Allure.step("Assert if max available quantity of product is in cart");
    }

    @ParameterizedTest(name = "{displayName}. Quantity: {arguments}")
    @DisplayName("Round to ceiling int with plus btn if factional quantity of product (1;oo) wanted")
    @ValueSource(strings = {"1,8", "1.9", "1,3", "1.1"})
    public void shouldRoundToCeilingWithPlusIfFractionalQuantityWanted(String amount) {
        BestSellerProductPage product = home.goToBestSellers().getProducts().get(0);
        ProductQuickViewPage productInQuickView = product.hover().quickView();
        verifyQuantityInCartAfterPlusBtn(productInQuickView, amount, 2);
        Allure.step("Assert if there are 2 items of product in cart");
    }

    @Test
    @DisplayName("Set max available quantity with plus btn when entered quantity exceeds max available amount")
    public void shouldSetMaxAvailableQuantityWhenWantedQuantityExceedsMaxWithPlus() {
        BestSellerProductPage product = home.goToBestSellers().getProducts().get(0);
        ProductQuickViewPage productInQuickView = product.hover().quickView();
        int availableQuantity = productInQuickView.getQuantityAvailable();
        verifyQuantityInCartAfterPlusBtn(productInQuickView, availableQuantity + 3, availableQuantity);
        Allure.step("Assert if max available quantity of product is in cart");
    }

    @ParameterizedTest(name = "{displayName}: {arguments}")
    @DisplayName("Set max available quantity with plus btn when entered quantity is invalid")
    @ValueSource(strings = {"cat", "ABC_+"})
    public void shouldSetMaxAvailableQuantityWhenWantedQuantityIsInvalidWithPlus(String invalidAmount) {
        BestSellerProductPage product = home.goToBestSellers().getProducts().get(0);
        ProductQuickViewPage productInQuickView = product.hover().quickView();
        int availableQuantity = productInQuickView.getQuantityAvailable();
        verifyQuantityInCartAfterPlusBtn(productInQuickView, invalidAmount, availableQuantity);
        Allure.step("Assert if max available quantity of product is in cart");
    }

    private void verifyQuantityInCartAfterPlusBtn(ProductQuickViewPage productInQuickView,
                                                  String wantedQuantity,
                                                  int expectedQuantity) {
        String productName = productInQuickView.getName();
        productInQuickView.enterWantedQuantity(wantedQuantity)
                .increaseWantedQuantityByOne()
                .addToCart()
                .closeWindow();
        int actualQuantity = home.getCart().scrollToCart().findProductInCartByName(productName).getQuantity();
        assertThat(actualQuantity)
                .withFailMessage("There are %d items of '%s', instead of %d",
                        actualQuantity, productName, expectedQuantity)
                .isEqualTo(expectedQuantity);
    }

    private void verifyQuantityInCartAfterPlusBtn(ProductQuickViewPage productInQuickView,
                                                  int wantedQuantity,
                                                  int expectedQuantity) {
        verifyQuantityInCartAfterPlusBtn(productInQuickView, String.valueOf(wantedQuantity), expectedQuantity);
    }

    @Test
    @DisplayName("Don't decrease quantity below 1 with minus btn")
    public void shouldNotDecreaseQuantityBelowOne() {
        BestSellerProductPage product = home.goToBestSellers().getProducts().get(0);
        String productName = product.getName();
        product.hover().quickView().decreaseWantedQuantityByOne().addToCart().closeWindow();
        int actualQuantity = home.getCart().scrollToCart().findProductInCartByName(productName).getQuantity();
        assertThat(actualQuantity)
                .withFailMessage("There are %d items of '%s' in cart, instead of 1", actualQuantity, product)
                .isEqualTo(1);
        Allure.step("Assert if one item of product is in the cart");
    }

    @ParameterizedTest(name = "{displayName}. Quantity: {arguments}")
    @DisplayName("Set wanted quantity to 1 with minus btn if entered quantity is less or equal to 2")
    @ValueSource(strings = {"-5", "-0.8", "-0.3", "0", "0.6", "1.8"})
    public void shouldSetQuantityToOne(String amount) {
        BestSellerProductPage product = home.goToBestSellers().getProducts().get(0);
        String productName = product.getName();
        product.hover().quickView().enterWantedQuantity(amount).decreaseWantedQuantityByOne().addToCart().closeWindow();
        int actualQuantity = home.getCart().scrollToCart().findProductInCartByName(productName).getQuantity();
        assertThat(actualQuantity)
                .withFailMessage("There are %d items of '%s' in cart, instead of 1",
                        actualQuantity, productName)
                .isEqualTo(1);
        Allure.step("Assert if one item of product is in the cart");
    }

    @Test
    @DisplayName("Decrease wanted quantity by one with minus btn if entered quantity is in (2, max value>")
    public void shouldDecreaseQuantityByOne() {
        ProductQuickViewPage productInQuickView = home.goToBestSellers().getProducts().get(0).hover().quickView();
        String productName = productInQuickView.getName();
        int availableQuantity = productInQuickView.getQuantityAvailable();
        productInQuickView.enterWantedQuantity(availableQuantity).decreaseWantedQuantityByOne().addToCart().closeWindow();
        int actualQuantity = home.getCart().scrollToCart().findProductInCartByName(productName).getQuantity();
        int expectedQuantityInCart = availableQuantity - 1;
        assertThat(actualQuantity)
                .withFailMessage("There are %d items of '%s' in cart, instead of %d",
                        actualQuantity, productName, expectedQuantityInCart)
                .isEqualTo(expectedQuantityInCart);
        Allure.step("Assert if quantity of product in the cart is equal to max available quantity decrease by one");
    }

    @Test
    @DisplayName("Decrease wanted quantity to max available value if entered quantity exceeds max available value")
    public void shouldDecreaseQuantityToMaxAvailableValue() {
        ProductQuickViewPage productInQuickView = home.goToBestSellers().getProducts().get(0).hover().quickView();
        String productName = productInQuickView.getName();
        int availableQuantity = productInQuickView.getQuantityAvailable();
        LayerCartPage layerCart = productInQuickView.enterWantedQuantity(availableQuantity + 20).decreaseWantedQuantityByOne().addToCart();
        assertThat(home.isErrorBoxDisplayed())
                .withFailMessage("Error box is displayed")
                .isFalse();
        Allure.step("Assert if error box is not displayed");
        layerCart.closeWindow();
        int actualQuantity = home.getCart().scrollToCart().findProductInCartByName(productName).getQuantity();
        assertThat(actualQuantity)
                .withFailMessage("There are %d items of '%s' in cart, instead of %d",
                        actualQuantity, productName, availableQuantity)
                .isEqualTo(availableQuantity);
        Allure.step("Assert if quantity of product in the cart is equal to max available quantity");
    }

    @ParameterizedTest(name = "{displayName}: {arguments}")
    @DisplayName("Set wanted quantity to one with minus btn if entered quantity has invalid value")
    @ValueSource(strings = {"KO_T", "-1Abc!"})
    public void shouldSetWantedQuantityToOneIfEnteredQuantityIsInvalid(String amount) {
        ProductQuickViewPage productInQuickView = home.goToBestSellers().getProducts().get(0).hover().quickView();
        String productName = productInQuickView.getName();
        LayerCartPage layerCart = productInQuickView.enterWantedQuantity(amount).decreaseWantedQuantityByOne().addToCart();
        assertThat(home.isErrorBoxDisplayed())
                .withFailMessage("Error box is displayed")
                .isFalse();
        Allure.step("Assert if error box is not displayed");
        layerCart.closeWindow();
        int actualQuantity = home.getCart().scrollToCart().findProductInCartByName(productName).getQuantity();
        assertThat(actualQuantity)
                .withFailMessage("There are %d items of '%s' in cart, instead of %d",
                        actualQuantity, productName, 1)
                .isEqualTo(1);
        Allure.step("Assert if quantity of product in the cart is equal to 1");
    }

    @Test
    @DisplayName("Add product in different size to cart")
    public void shouldAddProductInDifferentSizeToCart() {
        ProductQuickViewPage productInQuickView = home.goToBestSellers().getProducts().get(0).hover().quickView();
        String productName = productInQuickView.getName();
        String expectedSize = "M";
        productInQuickView.setSize(expectedSize).increaseWantedQuantityByOne().addToCart().closeWindow();
        CartBlockProductPage productInCart = home.getCart().scrollToCart().findProductInCartByName(productName);
        int actualQuantity = productInCart.getQuantity();
        assertThat(actualQuantity)
                .withFailMessage("Quantity of product '%s' in cart is %d instead of 2",
                        productName, actualQuantity)
                .isEqualTo(2);
        Allure.step("Assert if there are 2 items of product in cart");
        String attributes = productInCart.getAttributes();
        assertThat(attributes)
                .withFailMessage("Product '%s' in cart is '%s' instead of '%s'",
                        productName, attributes, expectedSize).startsWith(expectedSize + ",");
        Allure.step(String.format("Assert if size of product is %s", expectedSize));
    }

    @Test
    @DisplayName("Add product in different color to cart")
    public void shouldAddProductInDifferentColorToCart() {
        ProductQuickViewPage productInQuickView = home.goToBestSellers().getProducts().get(0).hover().quickView();
        String productName = productInQuickView.getName();
        String expectedColor = productInQuickView.getAvailableColors().get(1);
        int expectedQuantity = 5;
        productInQuickView.enterWantedQuantity(expectedQuantity).setColor(1).addToCart().closeWindow();
        CartBlockProductPage productInCart = home.getCart().scrollToCart().findProductInCartByName(productName);
        int actualQuantity = productInCart.getQuantity();
        assertThat(actualQuantity)
                .withFailMessage("Quantity of product '%s' in cart is %d instead of %d",
                        productName, actualQuantity, expectedQuantity)
                .isEqualTo(expectedQuantity);
        Allure.step(String.format("Assert if there are %d items of product in cart", expectedQuantity));
        String attributes = productInCart.getAttributes();
        assertThat(attributes).withFailMessage("Product '%s' in cart is '%s' instead of '%s'",
                        productName, actualQuantity, expectedQuantity)
                .endsWith(expectedColor);
        Allure.step(String.format("Assert if product in cart %s color", expectedColor));
    }

    @Test
    @DisplayName("Add max quantities of product in two different colors and sizes")
    public void shouldAddProductInVariousColorsAndSizesToCart() {
        // add product in size S in given color and max available quantity
        ProductQuickViewPage productInQuickView = home.goToBestSellers().getProducts().get(0).hover().quickView();
        String productName = productInQuickView.getName();
        String expectedSize_S = "S";
        ProductQuickViewPage productInSizeS = productInQuickView.setSize(expectedSize_S);
        String expectedColor_S = productInSizeS.getAvailableColors().get(1);
        productInSizeS.setColor(1);
        int expectedQuantity_S = productInSizeS.getQuantityAvailable();
        productInSizeS.enterWantedQuantity(expectedQuantity_S).addToCart().closeWindow();

        // add product in size L in given color and max available quantity
        productInQuickView = home.goToBestSellers().getProducts().get(0).hover().quickView();
        String expectedSize_L = "L";
        ProductQuickViewPage productInSizeL = productInQuickView.setSize(expectedSize_L);
        String expectedColor_L = productInSizeL.getAvailableColors().get(0);
        productInSizeL.setColor(0);
        int expectedQuantity_L = productInSizeL.getQuantityAvailable();
        productInSizeL.enterWantedQuantity(expectedQuantity_L).addToCart().closeWindow();

        List<CartBlockProductPage> productsInCart = home.getCart().scrollToCart().findProductsInCartByName(productName);
        int actualQuantity_S = productsInCart.get(0).getQuantity();
        String attributes_S = productsInCart.get(0).getAttributes();
        // assert quantity, color, size for first product added
        assertThat(actualQuantity_S)
                .withFailMessage("Quantity of product '%s' in cart is %d instead of %d",
                        productName, actualQuantity_S, expectedQuantity_S)
                .isEqualTo(expectedQuantity_S);
        Allure.step(String.format("Assert if there are %d items of product '%s %s' in cart"
                ,productName, attributes_S, expectedQuantity_S));
        assertThat(attributes_S).withFailMessage("Product '%s' in cart is '%s' instead of '%s'",
                        productName, attributes_S, expectedColor_S)
                .endsWith(expectedColor_S);
        Allure.step(String.format("Assert if product '%s' in cart has %s color", productName, expectedColor_S));
        assertThat(attributes_S)
                .withFailMessage("Product '%s' in cart is '%s' instead of '%s'",
                        productName, attributes_S, expectedSize_S)
                .startsWith(expectedSize_S + ",");
        Allure.step(String.format("Assert if size of product '%s' is %s",productName, expectedSize_S));

        int actualQuantityForProductInSizeL = productsInCart.get(1).getQuantity();
        String attributes_L = productsInCart.get(1).getAttributes();
        assertThat(actualQuantityForProductInSizeL)
                .withFailMessage("Quantity of product '%s' in cart is %d instead of %d",
                        productName, actualQuantityForProductInSizeL, expectedQuantity_L)
                .isEqualTo(expectedQuantity_L);
        Allure.step(String.format("Assert if there are %d items of product '%s %s' in cart",
                productName, attributes_L, expectedQuantity_L));
        assertThat(attributes_L).withFailMessage("Product '%s' in cart is '%s' instead of '%s'",
                        productName, attributes_L, expectedColor_L)
                .endsWith(expectedColor_L);
        Allure.step(String.format("Assert if product '%s' in cart %s color", productName, expectedColor_L));
        assertThat(attributes_L)
                .withFailMessage("Product '%s' in cart is '%s' instead of '%s'",
                        productName, attributes_L, expectedSize_L)
                .startsWith(expectedSize_L + ",");
        Allure.step(String.format("Assert if size of product '%s' is %s", productName, expectedSize_L));
    }
}
