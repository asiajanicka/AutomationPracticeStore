package tests;

import io.qameta.allure.Allure;
import io.qameta.allure.Feature;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageObjects.ProductPage;
import pageObjects.ProductQuickViewPage;
import pageObjects.cartPages.CartBlockProductPage;
import pageObjects.homePages.BestSellerHoveredProductPage;
import pageObjects.homePages.BestSellerProductPage;
import pageObjects.homePages.HomePage;
import pageObjects.homePages.LayerCartPage;

import java.time.Duration;
import java.util.List;
import java.util.Set;

import static enums.Stock.IN_STOCK_1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

@Feature("Quick View of Best Seller")
public class ProductQuickViewTests extends BaseTest {

    private HomePage home;

    @BeforeEach
    public void testSetup() {
        super.testSetup();
        driver.navigate().to(configuration.getBaseUrl());
        home = new HomePage(driver);
    }

    @Test
    @DisplayName("Display basic info about product")
    public void shouldSeeBasicInfoAboutProduct() {
        String expectedName = testData.getProductNames()[0];
        BestSellerProductPage product = home.goToBestSellers().getProduct(expectedName);
        String expectedPrice = product.getPrice();
        ProductQuickViewPage productInQuickView = product.getProductOnHover().quickView();
        String actualName = productInQuickView.getName();
        String actualPrice = productInQuickView.getPrice();
        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(actualName)
                .withFailMessage("Product name in quick view is different then name in Best Sellers. " +
                        "Actual name: '%s'. Expected name: '%s'.", actualName, expectedName)
                .isEqualTo(expectedName);
        soft.assertThat(actualPrice)
                .withFailMessage("Product price in quick view is different then price in Best Sellers. " +
                        "Actual price: '%s'. Expected price: '%s'.", actualPrice, expectedPrice)
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
        String expectedName = testData.getDiscountedProductNames()[0];
        BestSellerProductPage discountedProduct = home.goToBestSellers().getProduct(expectedName);
        String expectedPriceReduction = discountedProduct.getPricePercentReduction();
        String expectedOldPrice = discountedProduct.getOldPrice();
        ProductQuickViewPage productInQuickView = discountedProduct.getProductOnHover().quickView();
        String actualOldPrice = productInQuickView.getOldPrice();
        String actualPriceReduction = productInQuickView.getPriceReduction();
        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(actualOldPrice)
                .withFailMessage("Old price of discounted product in quick view is different then in Best Sellers. " +
                        "Actual old price: '%s'. Expected old price: '%s'.", actualOldPrice, expectedOldPrice)
                .isEqualTo(expectedOldPrice);
        soft.assertThat(actualPriceReduction)
                .withFailMessage("Price reduction of discounted product in quick view is different then in Best Sellers. " +
                        "Actual reduction is: '%s'. Expected reduction is: '%s'.", actualPriceReduction, expectedPriceReduction)
                .isEqualTo(expectedPriceReduction);
        soft.assertAll();
        Allure.step("Assert if discounted product has the same old price and price reduction as in Best Sellers");
    }

    @Test
    @DisplayName("Go to product page after click on big image")
    public void shouldGoToProductPage() {
        String expectedName = testData.getProductNames()[0];
        ProductPage productPage = home.goToBestSellers().getProduct(expectedName).getProductOnHover().quickView()
                .clickOnBigImage();
        assertThat(productPage.getName())
                .withFailMessage("After click on product big image user was " +
                        "redirected to another page " +
                        "which does not correspond to %s", expectedName)
                .isEqualTo(expectedName);
        Allure.step(String.format("Assert if product page contains product name '%s'", expectedName));
    }

//    private boolean isPageCurrentLoaded() {
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//        Boolean complete = wait.until(
//                webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
//        return complete;
//    }

    private void checkIfSocialMediaPageOpenAfterClickOnShare(String socialMediaName){
        String expectedName = testData.getProductNames()[0];
        BestSellerHoveredProductPage productOnHover = home.goToBestSellers()
                .getProduct(expectedName)
                .getProductOnHover();
        String parentWindowHandle = driver.getWindowHandle();
        switch (socialMediaName){
            case"Facebook":{
                productOnHover.quickView().shareOnFaceBook();
                break;
            }
            case "Pinterest":{
                productOnHover.quickView().shareOnPinterest();
                break;
            }
            case "Twitter":{
                productOnHover.quickView().shareOnTwitter();
                break;
            }
            case "Google":{
                productOnHover.quickView().shareOnGooglePlus();
                break;
            }
            default: throw new IllegalArgumentException("Unknown social media name");
        }

        Set<String> windowHandles = driver.getWindowHandles();
        windowHandles.remove(parentWindowHandle);
        String windowHandleOfSocialMediaPage = windowHandles.iterator().next();
        driver.switchTo().window(windowHandleOfSocialMediaPage);
        isPageCurrentLoaded();
        String titleOFSocialMediaPage = driver.getTitle();
        assertThat(titleOFSocialMediaPage)
                .withFailMessage("Newly opened page title doesn't contain word '%s'. " +
                        "Title of opened page is '%s'", socialMediaName, titleOFSocialMediaPage)
                .contains(socialMediaName);
        Allure.step(String.format("Assert if %s page opens after click on share button", socialMediaName));
    }

    @Test
    @DisplayName("Go to Facebook after click on 'share on Facebook'")
    public void shouldDisplayProductOnFacebook() {
        checkIfSocialMediaPageOpenAfterClickOnShare("Facebook");
    }

    @Test
    @DisplayName("Go to Twitter after click on 'share on Twitter'")
    public void shouldDisplayProductOnTweeter() {
        checkIfSocialMediaPageOpenAfterClickOnShare("Twitter");
    }

    @Test
    @DisplayName("Go to Google Plus after click on 'share on Google'")
    public void shouldDisplayProductOnGoogle() {
        checkIfSocialMediaPageOpenAfterClickOnShare("Google");
    }

    @Test
    @DisplayName("Go to Pinterest after click on 'share on Pinterest'")
    public void shouldDisplayProductOnPinterest() {
        checkIfSocialMediaPageOpenAfterClickOnShare("Pinterest");
    }

    @Test
    @DisplayName("Add single product to cart")
    public void shouldAddSingleProductToCart() {
        String expectedName = testData.getProductNames()[0];
        home.goToBestSellers().getProduct(expectedName).getProductOnHover().quickView()
                .addToCart()
                .closeWindow();
        List<CartBlockProductPage> productsInCart = home.getCart().scrollToCart().expandCart();
        assertThat(productsInCart)
                .withFailMessage("One item of product '%s' was not add to cart", expectedName)
                .extracting(CartBlockProductPage::getName, CartBlockProductPage::getQuantity)
                .contains(tuple(expectedName, 1));
        Allure.step("Assert if one item of product is in cart");
    }

    @ParameterizedTest(name = "Add {arguments} items of product to cart")
    @ValueSource(ints = {2, 10})
    @DisplayName("Add given quantity of product to cart")
    public void shouldAddAFewItemsOfProductToCart(int amount) {
        String expectedName = testData.getProductNames()[0];
        home.goToBestSellers().getProduct(expectedName)
                .getProductOnHover().quickView()
                .enterWantedQuantity(amount)
                .addToCart()
                .closeWindow();
        List<CartBlockProductPage> productsInCart = home.getCart().scrollToCart().expandCart();
        assertThat(productsInCart)
                .withFailMessage("%d items of '%s' are not in cart", amount, expectedName)
                .extracting(CartBlockProductPage::getName, CartBlockProductPage::getQuantity)
                .contains(tuple(expectedName, amount));
        Allure.step(String.format("Assert if %d items of product '%s' are in cart", amount, expectedName));
    }

    @Test
    @DisplayName("Add max available quantity of product to cart")
    public void shouldAddMaxQuantityOfProductToCart() {
        String expectedName = testData.getProductNames()[0];
        ProductQuickViewPage productInQuickView = home.goToBestSellers().getProduct(expectedName).getProductOnHover().quickView();
        int maxAvailableAmount = productInQuickView.getQuantityAvailable();
        productInQuickView.enterWantedQuantity(maxAvailableAmount).addToCart().closeWindow();
        List<CartBlockProductPage> productsInCart = home.getCart().scrollToCart().expandCart();
        assertThat(productsInCart)
                .withFailMessage("Max available amount - %d - of '%s' is not in cart",
                        maxAvailableAmount, expectedName)
                .extracting(CartBlockProductPage::getName, CartBlockProductPage::getQuantity)
                .contains(tuple(expectedName, maxAvailableAmount));
        Allure.step(String.format("Assert if max available amount - that is: %d - of product '%s' is in cart",
                maxAvailableAmount, expectedName));
    }

    @ParameterizedTest(name = "{displayName}. Quantity: {arguments}")
    @ValueSource(strings = {"-5", "-1", "0", "-0.8", "-0,8", "-0.3", "-0,3"})
    @DisplayName("Do not add product with wanted quantity lower then 1 to cart")
    public void shouldDisplayErrorBoxWhenWantedQuantityLessThanOne(String amount) {
        String expectedName = testData.getProductNames()[0];
        home.goToBestSellers().getProduct(expectedName).getProductOnHover().quickView()
                .enterWantedQuantity(amount)
                .addToCart();
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
        String expectedName = testData.getProductNames()[0];
        int expectedAmount = 15;
        home.goToBestSellers().getProduct(expectedName).getProductOnHover().quickView()
                .enterWantedQuantity(15.9f)
                .addToCart()
                .closeWindow();
        List<CartBlockProductPage> productsInCart = home.getCart().scrollToCart().expandCart();
        assertThat(productsInCart)
                .withFailMessage("%d items of product %s are not in the cart", expectedAmount, expectedName)
                .extracting(CartBlockProductPage::getName, CartBlockProductPage::getQuantity)
                .contains(tuple(expectedName, expectedAmount));
        Allure.step(String.format("Assert if %d items of '%s' are in cart", expectedAmount, expectedName));
    }

    @ParameterizedTest(name = "{displayName}: {arguments}")
    @DisplayName("Do not add invalid quantity of product to cart")
    @ValueSource(strings = {"cat", "ABC_+"})
    public void shouldDisplayErrorBoxIfInvalidQuantityToBeAddedToCart(String invalidAmount) {
        String expectedName = testData.getProductNames()[0];
        home.goToBestSellers().getProduct(expectedName).getProductOnHover().quickView()
                .enterWantedQuantity(invalidAmount)
                .addToCart();
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
        String expectedName = testData.getProductNames()[0];
        ProductQuickViewPage productInQuickView = home.goToBestSellers().getProduct(expectedName).getProductOnHover().quickView();
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
        String productName = testData.getProductNames()[0];
        ProductQuickViewPage productInQuickView = home.goToBestSellers().getProduct(productName).getProductOnHover().quickView();
        int maxAvailableAmount = productInQuickView.getQuantityAvailable();
        int quantityInFirstPart = maxAvailableAmount - 1;
        int quantityInSecondPart = 2;
        productInQuickView.enterWantedQuantity(quantityInFirstPart).addToCart().closeWindow();
        home.goToBestSellers().getProduct(productName).getProductOnHover()
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
        String productName = testData.getProductNames()[0];
        home.goToBestSellers().getProduct(productName).getProductOnHover().quickView()
                .increaseWantedQuantityByOne().
                addToCart()
                .closeWindow();
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
        String productName = testData.getProductNames()[0];
        LayerCartPage layerCartPage = home.goToBestSellers().getProduct(productName).getProductOnHover().quickView()
                .enterWantedQuantity(amount)
                .increaseWantedQuantityByOne()
                .addToCart();
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
        String productName = testData.getProductNames()[0];
        ProductQuickViewPage productInQuickView = home.goToBestSellers().getProduct(productName).getProductOnHover().quickView();
        int availableQuantity = productInQuickView.getQuantityAvailable();
        verifyQuantityInCartAfterPlusBtn(productInQuickView, availableQuantity - 1, availableQuantity);
        Allure.step("Assert if max available quantity of product is in cart");
    }

    @Test
    @DisplayName("Do not increase wanted quantity over max available amount with plus btn")
    public void shouldNotIncreaseQuantityWithPlusIfMaxAvailableEntered() {
        String productName = testData.getProductNames()[0];
        ProductQuickViewPage productInQuickView = home.goToBestSellers().getProduct(productName).getProductOnHover().quickView();
        int availableQuantity = productInQuickView.getQuantityAvailable();
        verifyQuantityInCartAfterPlusBtn(productInQuickView, availableQuantity, availableQuantity);
        Allure.step("Assert if max available quantity of product is in cart");
    }

    @ParameterizedTest(name = "{displayName}. Quantity: {arguments}")
    @DisplayName("Round to ceiling int with plus btn if factional quantity of product (1;oo) wanted")
    @ValueSource(strings = {"1,8", "1.9", "1,3", "1.1"})
    public void shouldRoundToCeilingWithPlusIfFractionalQuantityWanted(String amount) {
        String productName = testData.getProductNames()[0];
        ProductQuickViewPage productInQuickView = home.goToBestSellers().getProduct(productName).getProductOnHover().quickView();
        verifyQuantityInCartAfterPlusBtn(productInQuickView, amount, 2);
        Allure.step("Assert if there are 2 items of product in cart");
    }

    @Test
    @DisplayName("Set max available quantity with plus btn when entered quantity exceeds max available amount")
    public void shouldSetMaxAvailableQuantityWhenWantedQuantityExceedsMaxWithPlus() {
        String productName = testData.getProductNames()[0];
        ProductQuickViewPage productInQuickView = home.goToBestSellers().getProduct(productName).getProductOnHover().quickView();
        int availableQuantity = productInQuickView.getQuantityAvailable();
        verifyQuantityInCartAfterPlusBtn(productInQuickView, availableQuantity + 3, availableQuantity);
        Allure.step("Assert if max available quantity of product is in cart");
    }

    @ParameterizedTest(name = "{displayName}: {arguments}")
    @DisplayName("Set max available quantity with plus btn when entered quantity is invalid")
    @ValueSource(strings = {"cat", "ABC_+"})
    public void shouldSetMaxAvailableQuantityWhenWantedQuantityIsInvalidWithPlus(String invalidAmount) {
        String productName = testData.getProductNames()[0];
        ProductQuickViewPage productInQuickView = home.goToBestSellers().getProduct(productName).getProductOnHover().quickView();
        int availableQuantity = productInQuickView.getQuantityAvailable();
        verifyQuantityInCartAfterPlusBtn(productInQuickView, invalidAmount, availableQuantity);
        Allure.step("Assert if max available quantity of product is in cart");
    }

    @Test
    @DisplayName("Don't decrease quantity below 1 with minus btn")
    public void shouldNotDecreaseQuantityBelowOne() {
        String productName = testData.getProductNames()[0];
        home.goToBestSellers().getProduct(productName).getProductOnHover().quickView()
                .decreaseWantedQuantityByOne()
                .addToCart()
                .closeWindow();
        int actualQuantity = home.getCart().scrollToCart().findProductInCartByName(productName).getQuantity();
        assertThat(actualQuantity)
                .withFailMessage("There are %d items of '%s' in cart, instead of 1",
                        actualQuantity, productName)
                .isEqualTo(1);
        Allure.step("Assert if one item of product is in the cart");
    }

    @ParameterizedTest(name = "{displayName}. Quantity: {arguments}")
    @DisplayName("Set wanted quantity to 1 with minus btn if entered quantity is less or equal to 2")
    @ValueSource(strings = {"-5", "-0.8", "-0.3", "0", "0.6", "1.8"})
    public void shouldSetQuantityToOne(String amount) {
        String productName = testData.getProductNames()[0];
        home.goToBestSellers().getProduct(productName).getProductOnHover().quickView()
                .enterWantedQuantity(amount)
                .decreaseWantedQuantityByOne()
                .addToCart()
                .closeWindow();
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
        String productName = testData.getProductNames()[0];
        ProductQuickViewPage productInQuickView = home.goToBestSellers().getProduct(productName).getProductOnHover().quickView();
        int availableQuantity = productInQuickView.getQuantityAvailable();
        productInQuickView
                .enterWantedQuantity(availableQuantity)
                .decreaseWantedQuantityByOne()
                .addToCart()
                .closeWindow();
        int actualQuantity = home.getCart().scrollToCart()
                .findProductInCartByName(productName)
                .getQuantity();
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
        String productName = testData.getProductNames()[0];
        ProductQuickViewPage productInQuickView = home.goToBestSellers().getProduct(productName).getProductOnHover().quickView();
        int availableQuantity = productInQuickView.getQuantityAvailable();
        LayerCartPage layerCart = productInQuickView
                .enterWantedQuantity(availableQuantity + 20)
                .decreaseWantedQuantityByOne()
                .addToCart();
        assertThat(home.isErrorBoxDisplayed())
                .withFailMessage("Error box is displayed")
                .isFalse();
        Allure.step("Assert if error box is not displayed");
        layerCart.closeWindow();
        int actualQuantity = home.getCart().scrollToCart()
                .findProductInCartByName(productName)
                .getQuantity();
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
        String productName = testData.getProductNames()[0];
        LayerCartPage layerCart = home.goToBestSellers().getProduct(productName).getProductOnHover().quickView()
                .enterWantedQuantity(amount)
                .decreaseWantedQuantityByOne()
                .addToCart();
        assertThat(home.isErrorBoxDisplayed())
                .withFailMessage("Error box is displayed")
                .isFalse();
        Allure.step("Assert if error box is not displayed");
        layerCart.closeWindow();
        int actualQuantity = home.getCart().scrollToCart()
                .findProductInCartByName(productName)
                .getQuantity();
        assertThat(actualQuantity)
                .withFailMessage("There are %d items of '%s' in cart, instead of %d",
                        actualQuantity, productName, 1)
                .isEqualTo(1);
        Allure.step("Assert if quantity of product in the cart is equal to 1");
    }

    @Test
    @DisplayName("Add product in different size to cart")
    public void shouldAddProductInDifferentSizeToCart() {
        String productName = testData.getProductNames()[0];
        ProductQuickViewPage productInQuickView = home.goToBestSellers().getProduct(productName).getProductOnHover().quickView();
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
        String productName = testData.getProductNames()[0];
        ProductQuickViewPage productInQuickView = home.goToBestSellers().getProduct(productName).getProductOnHover().quickView();
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
        String productName = testData.getProductNames()[0];
        // add product in size S in given color and max available quantity
        ProductQuickViewPage productInQuickView = home.goToBestSellers().getProduct(productName).getProductOnHover().quickView();
        String expectedSize_S = "S";
        ProductQuickViewPage productInSizeS = productInQuickView.setSize(expectedSize_S);
        String expectedColor_S = productInSizeS.getAvailableColors().get(1);
        productInSizeS.setColor(1);
        int expectedQuantity_S = productInSizeS.getQuantityAvailable();
        productInSizeS.enterWantedQuantity(expectedQuantity_S).addToCart().closeWindow();

        // add product in size L in given color and max available quantity
        productInQuickView = home.goToBestSellers().getProducts().get(0).getProductOnHover().quickView();
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
        Allure.step(String.format("Assert if there are %d items of product '%s %s' in cart",
                expectedQuantity_S, productName, attributes_S));
        assertThat(attributes_S).withFailMessage("Product '%s' in cart is '%s' instead of '%s'",
                        productName, attributes_S, expectedColor_S)
                .endsWith(expectedColor_S);
        Allure.step(String.format("Assert if product '%s' in cart has %s color", productName, expectedColor_S));
        assertThat(attributes_S)
                .withFailMessage("Product '%s' in cart is '%s' instead of '%s'",
                        productName, attributes_S, expectedSize_S)
                .startsWith(expectedSize_S + ",");
        Allure.step(String.format("Assert if size of product '%s' is %s", productName, expectedSize_S));

        int actualQuantityForProductInSizeL = productsInCart.get(1).getQuantity();
        String attributes_L = productsInCart.get(1).getAttributes();
        assertThat(actualQuantityForProductInSizeL)
                .withFailMessage("Quantity of product '%s' in cart is %d instead of %d",
                        productName, actualQuantityForProductInSizeL, expectedQuantity_L)
                .isEqualTo(expectedQuantity_L);
        Allure.step(String.format("Assert if there are %d items of product '%s %s' in cart",
                expectedQuantity_L, productName, attributes_L));
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
}
