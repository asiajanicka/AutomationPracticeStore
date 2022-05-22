package tests;

import drivers.DriverFactory;
import io.qameta.allure.Allure;
import io.qameta.allure.Feature;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pageObjects.ProductPage;
import pageObjects.ProductQuickViewPage;
import pageObjects.homePages.BestSellerHoveredProductPage;
import pageObjects.homePages.BestSellerProductPage;
import pageObjects.homePages.HomePage;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import static enums.Stock.IN_STOCK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

@Feature("Best Sellers & Popular Items")
public class HomeTabsTests extends BaseTest {

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
    @DisplayName("Display Popular Items after page launch")
    public void shouldSeePopularItemsContentAfterPageLaunch() {
        String expectedActiveTab = appProperties.getPopularItemsTabLabel();
        assertThat(home.getActiveTabName())
                .withFailMessage(String.format("Name of active tab should be: %s but is: %s",
                        expectedActiveTab,
                        home.getActiveTabName()))
                .isEqualTo(expectedActiveTab);
        Allure.step(String.format("Assert if name of active tab is \"%s\"", expectedActiveTab));

        String expectedActiveTabContentId = appProperties.getPopularItemsTabContentId();
        assertThat(home.getActiveTabContentId())
                .withFailMessage("Active tab content id should be: %s but is: %s",
                        expectedActiveTabContentId,
                        home.getActiveTabContentId())
                .isEqualTo(expectedActiveTabContentId);
        Allure.step(String.format("Assert if active tab content id is \"%s\"", expectedActiveTabContentId));
    }

    @Test
    @DisplayName("Switch content between tabs")
    public void shouldSwitchBetweenTabs() {
        home.goToBestSellers();

        String expectedActiveTab = appProperties.getBestSellersTabLabel();
        assertThat(home.getActiveTabName())
                .withFailMessage(String.format("Name of active tab should be: %s but is: %s",
                        expectedActiveTab,
                        home.getActiveTabName()))
                .isEqualTo(expectedActiveTab);
        Allure.step(String.format("Assert if name of active tab is \"%s\"", expectedActiveTab));

        String expectedActiveTabContentId = appProperties.getBestSellersTabContentId();
        assertThat(home.getActiveTabContentId())
                .withFailMessage("Active tab content id should be: %s but is: %s",
                        expectedActiveTabContentId,
                        home.getActiveTabContentId())
                .isEqualTo(expectedActiveTabContentId);
        Allure.step(String.format("Assert if active tab content id is \"%s\"", expectedActiveTabContentId));

        home.goToPopularItems();

        expectedActiveTab = appProperties.getPopularItemsTabLabel();
        assertThat(home.getActiveTabName())
                .withFailMessage(String.format("Name of active tab should be: %s but is: %s",
                        expectedActiveTab,
                        home.getActiveTabName()))
                .isEqualTo(expectedActiveTab);
        Allure.step(String.format("Assert if name of active tab is \"%s\"", expectedActiveTab));

        expectedActiveTabContentId = appProperties.getPopularItemsTabContentId();
        assertThat(home.getActiveTabContentId())
                .withFailMessage("Active tab content id should be: %s but is: %s",
                        expectedActiveTabContentId,
                        home.getActiveTabContentId())
                .isEqualTo(expectedActiveTabContentId);
        Allure.step(String.format("Assert if active tab content id is \"%s\"", expectedActiveTabContentId));
    }

    @Test
    @DisplayName("Display alert if no content in Popular Items is available")
    public void shouldSeeNoProductsAlertInPopularItem() {
        assertThat(home.getPopularItems().isNoProductsAlertDisplayed())
                .withFailMessage("Alert informing that there are not products in Popular Items" +
                        " is not displayed")
                .isTrue();
        Allure.step("Assert if alert about no products is displayed");
    }

    @Test
    @DisplayName("Display given number of products with necessary info")
    public void shouldSeeAmountOfProductsInBestSellers() {
        SoftAssertions softly = new SoftAssertions();

        List<BestSellerProductPage> products = home.goToBestSellers().getProducts();
        int expectedNumberOfProducts = testData.getProductNames().length;
        int actualNumberOfProducts = products.size();
        softly.assertThat(actualNumberOfProducts)
                .withFailMessage("There should be %d products displayed in BestSellers, but there are" +
                        "%d instead", expectedNumberOfProducts, actualNumberOfProducts)
                .isEqualTo(expectedNumberOfProducts);
        softly.assertThat(products)
                .withFailMessage("Some of products have empty name")
                .noneSatisfy(p -> assertThat(p.getName()).isEmpty());
        softly.assertThat(products)
                .withFailMessage("Some of products have empty hyperlink for name")
                .noneSatisfy(p -> assertThat(p.hasNameHyperLink()).isFalse());
        softly.assertThat(products)
                .withFailMessage("Some of products have empty image")
                .noneSatisfy(p -> assertThat(p.getImgStr()).isEmpty());
        softly.assertThat(products)
                .withFailMessage("Some of products have empty hyperlink for image")
                .noneSatisfy(p -> assertThat(p.hasImgHyperLink()).isFalse());
        softly.assertThat(products)
                .withFailMessage("Some of products have empty price")
                .noneSatisfy(p -> assertThat(p.getPrice()).isEmpty());
        softly.assertThat(products)
                .withFailMessage("Some of products have non positive price")
                .allSatisfy(p -> assertThat(p.getPriceValue().compareTo(BigDecimal.ZERO)).isPositive());
        softly.assertThat(products)
                .withFailMessage("Some of products have price in other currency then %s",
                        appProperties.getCurrency())
                .allSatisfy(p -> assertThat(p.getPrice()).startsWith(appProperties.getCurrency()));
        Allure.step(String.format("Assert if: there are %d products displayed in Best Sellers. " +
                        "Products: " +
                        "1) don't have empty name " +
                        "2) have hyperlink for name " +
                        "3) don't have empty image " +
                        "4) have hyperlink for image " +
                        "5) don't have empty price " +
                        "6) have price with positive value " +
                        "7) have price in %s",
                expectedNumberOfProducts,
                appProperties.getCurrency()));
        softly.assertAll();
    }

    @Test
    @DisplayName("Display given number of discounted products with old price and price percent reduction")
    public void shouldSeeDiscountedProducts() {
        SoftAssertions softly = new SoftAssertions();

        List<BestSellerProductPage> discountedProducts = home.goToBestSellers().getDiscountedProducts();
        int expectedNumberOfDiscountedProducts = testData.getDiscountedProductNames().length;
        int actualNumberOfDiscountedProducts = discountedProducts.size();
        List<BestSellerProductPage> productsWithPriceReduction = home.goToBestSellers()
                .getDiscountedProductsWithPricePercentReduction();

        softly.assertThat(actualNumberOfDiscountedProducts)
                .withFailMessage("There should be %d discounted products displayed in BestSellers," +
                                " but there are %d instead",
                        expectedNumberOfDiscountedProducts,
                        actualNumberOfDiscountedProducts)
                .isEqualTo(expectedNumberOfDiscountedProducts);
        softly.assertThat(discountedProducts)
                .withFailMessage("Some of discounted products have non positive old price")
                .allSatisfy(p -> assertThat(p.getOldPriceValue().compareTo(BigDecimal.ZERO)).isPositive());
        softly.assertThat(discountedProducts)
                .withFailMessage("Some of discounted products have old price with currency" +
                                " different then: %s",
                        appProperties.getCurrency())
                .allSatisfy(p -> assertThat(p.getOldPrice()).startsWith(appProperties.getCurrency()));
        softly.assertThat(discountedProducts)
                .withFailMessage("Some of discounted products have old price higher than current price")
                .allSatisfy(p -> assertThat(p.getOldPriceValue().compareTo(p.getPriceValue())).isPositive());
        softly.assertThat(discountedProducts)
                .as("Some of discounted products don't have price reduction")
                .containsAll(productsWithPriceReduction);
        softly.assertThat(productsWithPriceReduction)
                .withFailMessage("Some of discounted products don't have price reduction staring with '-'")
                .allSatisfy(p -> assertThat(p.getPricePercentReduction()).startsWith("-"));
        softly.assertThat(productsWithPriceReduction)
                .withFailMessage("Some of discounted products don't have price reduction ending with '%'")
                .allSatisfy(p -> assertThat(p.getPricePercentReduction()).endsWith("%"));
        Allure.step(String.format("Assert if: there are %d discounted products displayed in Best Sellers. " +
                        "Discounted products: " +
                        "1) have positive old price " +
                        "2) have old price in %s " +
                        "3) have current price lower than old price " +
                        "4) have percent price reduction starting with minus and ending with percent sign",
                expectedNumberOfDiscountedProducts,
                appProperties.getCurrency()));
        softly.assertAll();
    }

    @Test
    @DisplayName("Go to product page after click on product img")
    public void shouldGoToProductPageAfterClickOnProductImg() throws InterruptedException {
        String expectedName = testData.getProductNames()[0];
        ProductPage productPage = home.goToBestSellers().getProduct(expectedName).clickOnImg();

        assertThat(productPage.getName())
                .withFailMessage("After click on product image user was redirected to another page " +
                        "which does not correspond to %s", expectedName)
                .isEqualTo(expectedName);
        Allure.step(String.format("Assert if after click on product image user is redirected to page with title \"%s\"",
                expectedName));
    }

    @Test
    @DisplayName("Go to product page after click on product name")
    public void shouldGoToProductPageAfterClickOnProductName() {
        String expectedName = testData.getProductNames()[0];
        ProductPage productPage = home.goToBestSellers().getProduct(expectedName).clickOnName();

        assertThat(productPage.getName())
                .withFailMessage("After click on product name user was redirected to another page " +
                        "which does not correspond to %s", expectedName)
                .isEqualTo(expectedName);
        Allure.step(String.format("Assert if after click on product name user is redirected to page with title \"%s\"",
                expectedName));
    }

    @Test
    @DisplayName("Display price and availability of product after hover")
    public void shouldSeeAvailabilityAndPriceAfterHoverOnProduct() {
        String productName = testData.getProductNames()[1];
        BestSellerProductPage product = home.goToBestSellers().getProduct(productName);
        String expectedPrice = product.getPrice();
        BestSellerHoveredProductPage productHovered = product.hover();
        String actualPrice = productHovered.getPrice();
        String availability = productHovered.getAvailability(appProperties.getCurrency());

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(availability)
                .withFailMessage("Availability of product %s is %s where it should be %s",
                        product.getName(), availability, IN_STOCK.getValue())
                .isEqualTo(IN_STOCK.getValue());
        softly.assertThat(actualPrice)
                .withFailMessage("Price displayed on hovered product is different" +
                        " than original price of product")
                .isEqualTo(expectedPrice);
        Allure.step("Assert if after hover on product: " +
                "1) availability is displayed " +
                "2) price is the same as original one");
        softly.assertAll();
    }

    @Test
    @DisplayName("Display old price and price percent reduction of discounted product after hover")
    public void shouldSeeOldPriceAndReductionAfterHoverOnProduct() {
        String discountedProductName = testData.getDiscountedProductNames()[0];
        BestSellerProductPage discountedProduct = home.goToBestSellers().getProduct(discountedProductName);
        String expectedOldPrice = discountedProduct.getOldPrice();
        String expectedPriceReduction = discountedProduct.getPricePercentReduction();
        BestSellerHoveredProductPage productHovered = discountedProduct.hover();
        String actualOldPrice = productHovered.getOldPrice();
        String actualPriceReduction = productHovered.getPricePercentageReduction();

        SoftAssertions softly = new SoftAssertions();
        assertThat(actualOldPrice)
                .withFailMessage("Old price displayed after hover on product %s is different than" +
                        "original old price", discountedProduct.getName())
                .isEqualTo(expectedOldPrice);
        assertThat(actualPriceReduction)
                .withFailMessage("Price reduction displayed after hover on product %s is different than " +
                        "original price reduction")
                .isEqualTo(expectedPriceReduction);
        Allure.step("Assert if after hover on discounted product: " +
                "1) old price is the same as original " +
                "2) price percent reduction is the same as original");
        softly.assertAll();
    }

    @Test
    @DisplayName("Go to product page after click on \"More\"")
    public void shouldGoToProductPageAfterClickOnMoreBtn() {
        String expectedName = testData.getProductNames()[0];
        ProductPage productPage = home.goToBestSellers().getProduct(expectedName).hover().viewMore();

        assertThat(productPage.getName())
                .withFailMessage("After click on \"More\" user was redirected to another page " +
                        "which does not correspond to %s", expectedName)
                .isEqualTo(expectedName);
        Allure.step(String.format("Assert if after click on \"More\" user is redirected to page with title \"%s\""
                , expectedName));
    }

    @Test
    @DisplayName("Quick view product")
    public void shouldSeeQuickViewPageAfterClickOnQuickViewBtn() {
        String expectedName = testData.getProductNames()[0];
        ProductQuickViewPage quickView = home.goToBestSellers().getProduct(expectedName).hover().quickView();

        assertThat(quickView.getName())
                .withFailMessage("Quick view shows info about different product then \"%s\"", expectedName)
                .isEqualTo(expectedName);
        Allure.step(String.format("Assert if quick view shows info about product %s", expectedName));
    }

    @Test
    @DisplayName("Add product to cart")
    public void shouldAddProductToCart() {
        String expectedName = testData.getProductNames()[0];
        home.goToBestSellers().getProduct(expectedName).hover().addToCart().closeWindow();

        assertThat(home.getCart().expandCart())
                .extracting(p -> p.getFullName())
                .withFailMessage("Product %s is not in the cart", expectedName)
                .contains(expectedName);
        Allure.step(String.format("Assert if product %s is in the cart", expectedName));
    }

    @Test
    @DisplayName("Add the same product to cart three times")
    public void shouldAddProductToCartThreeTimes() {
        String expectedName = testData.getProductNames()[0];
        int numberOfItems = 3;
        for (int i = 0; i < numberOfItems; i++) {
            home.goToBestSellers().getProduct(expectedName).hover().addToCart().closeWindow();
        }

        assertThat(home.getCart().expandCart())
                .extracting(p -> p.getFullName(), p -> p.getQuantity())
                .withFailMessage("Quantity of product %s in cart is different than %d",
                        expectedName, numberOfItems)
                .contains(tuple(expectedName, numberOfItems));
        Allure.step(String.format("Assert if product %s was added %d times to cart", expectedName, numberOfItems));
    }

    @Test
    @DisplayName("Add three different products to cart")
    public void shouldAddThreeDifferentProductsToCart() {
        String product_1_name = testData.getProductNames()[0];
        home.goToBestSellers().getProduct(product_1_name).hover().addToCart().closeWindow();
        String product_2_name = testData.getProductNames()[1];
        home.goToBestSellers().getProduct(product_2_name).hover().addToCart().closeWindow();
        String product_3_name = testData.getProductNames()[2];
        home.goToBestSellers().getProduct(product_3_name).hover().addToCart().closeWindow();

        assertThat(home.getCart().expandCart())
                .extracting(p -> p.getFullName(), p -> p.getQuantity())
                .withFailMessage("Some products of %s, %s, %s were not added to cart",
                        product_1_name, product_2_name, product_2_name)
                .contains(tuple(product_1_name, 1),
                        tuple(product_2_name, 1),
                        tuple(product_3_name, 1));
        Allure.step(String.format("Assert if products: %s, %s, %s are in cart",
                product_1_name, product_2_name, product_2_name));
    }
}