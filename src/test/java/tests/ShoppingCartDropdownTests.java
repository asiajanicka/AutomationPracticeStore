package tests;

import drivers.DriverFactory;
import io.qameta.allure.Allure;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pageObjects.ProductPage;
import pageObjects.cartPages.CartBlockProductPage;
import pageObjects.homePages.BestSellerProductPage;
import pageObjects.homePages.HomePage;

import java.math.BigDecimal;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.awaitility.Awaitility.await;

public class ShoppingCartDropdownTests extends BaseTest {

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
    @DisplayName("Cart is empty if no product added")
    public void shouldSeeEmptyCart() {
        String expectedCartProductLabel = appProperties.getCartEmptyLabel();
        SoftAssertions softly = new SoftAssertions();
//        check label on cart bar
        softly.assertThat(home.getCart().getLabel())
                .withFailMessage("Displayed label is different then \"%s\"", expectedCartProductLabel)
                .isEqualTo(expectedCartProductLabel);
//        check if cart is collapsed when empty
        softly.assertThat(home.getCart().hoverOnCart().isCartContainerDisplayed())
                .withFailMessage("Cart is expanded when no products in cart").isFalse();
        Allure.step(String.format("Assert if cart is collapsed when hovered on and if cart label shows \"%s\"",
                expectedCartProductLabel));
        softly.assertAll();
    }

    @Test
    @DisplayName("Add product from Best Sellers to cart")
    public void shouldSeeOneProductFromBestSellersInCart() {
        String expectedCartProductLabel = appProperties.getCartOneProductLabel();
        String productName = testData.getProductNames()[0];
//        add product to cart
        BestSellerProductPage bestSeller = home.goToBestSellers().getProduct(productName);
        bestSeller.hover().addToCart().closeWindow();

        SoftAssertions softly = new SoftAssertions();
//        check label on cart bar
        softly.assertThat(home.getCart().getLabel())
                .withFailMessage("Displayed label is different then \"%s\"", expectedCartProductLabel)
                .isEqualTo(expectedCartProductLabel);
//        check quantity on cart bar
        softly.assertThat(home.getCart().getQuantity())
                .withFailMessage("Quantity on cart label is different then 1").isEqualTo(1);
//        expand cart
        home.getCart().expandCart();
        softly.assertThat(home.getCart().isCartContainerDisplayed())
                .withFailMessage("The cart is not expanded").isTrue();
//        find product in cart
        CartBlockProductPage productInCart = home.getCart().findProductInCart(productName);
//        assert product properties in cart
        softly.assertThat(productInCart.getFullName())
                .withFailMessage("Product full name in cart is different then %s", productName)
                .isEqualTo(productName);
        softly.assertThat(productInCart.getQuantity())
                .withFailMessage("Quantity of product in the cart is different then 1").isEqualTo(1);
        softly.assertThat(productInCart.getPrice())
                .withFailMessage("Price of product in the cart is different then original best seller's price")
                .isEqualTo(bestSeller.getPrice());
        softly.assertThat(productInCart.areAttributesDisplayed())
                .withFailMessage("Product's attributes (size, color) are not displayed").isTrue();
        softly.assertThat(productInCart.hasImageDisplayed())
                .withFailMessage("Product's image is not displayed").isTrue();
//        assert shipping and total cost currency
        softly.assertThat(home.getCart().getShippingCost())
                .withFailMessage("Shipping cost displayed in cart has different currency then %s",
                        appProperties.getCurrency())
                .startsWith(appProperties.getCurrency());
        softly.assertThat(home.getCart().getTotalCost())
                .withFailMessage("Total cost displayed in cart has different currency then %s",
                        appProperties.getCurrency()).startsWith(appProperties.getCurrency());
//        assert total price as sum of product price and shipping cost
        BigDecimal expectedTotalCost = bestSeller.getPriceValue().add(home.getCart().getShippingCostValue());
        softly.assertThat(home.getCart().getTotalCostValue())
                .withFailMessage("Total cost does not equal to sum of product's price and " +
                        "shipping cost").isEqualTo(expectedTotalCost);

        Allure.step(String.format("Assert if cart label displays \"1 %s\"", expectedCartProductLabel));
        Allure.step("Assert if cart expands after hover on");
        Allure.step(String.format("Assert if product in cart: " +
                "1) is %s " +
                "2) has quantity :1 " +
                "3) has the same price as in Best Sellers " +
                "4) has image displayed " +
                "5) has attributes displayed", productName));
        Allure.step(String.format("Assert if shipping and total costs are in %s", appProperties.getCurrency()));
        Allure.step("Assert if total cost is a sum of product price and shipping costs");
        softly.assertAll();
    }

    @Test
    @DisplayName("Add the same product from Best Sellers to cart three times")
    public void shouldSeeTheSameProductFromBestSellersAddedThreeTimesInCart() {
        String expectedCartProductLabel = appProperties.getCartProductsLabel();
        String productName = testData.getProductNames()[0];
        int productQuantity = 3;
//        add product three times to cart
        BestSellerProductPage bestSeller = home.goToBestSellers().getProduct(productName);
        for (int i = 0; i < productQuantity; i++) {
            home.goToBestSellers().getProduct(productName).hover().addToCart().closeWindow();
        }

        SoftAssertions softly = new SoftAssertions();
//        check label on cart bar
        softly.assertThat(home.getCart().getLabel())
                .withFailMessage("Displayed label is different then \"%s\"", expectedCartProductLabel)
                .isEqualTo(expectedCartProductLabel);
//        check quantity on cart bar
        softly.assertThat(home.getCart().getQuantity())
                .withFailMessage("Quantity on cart label is different then %d", productQuantity)
                .isEqualTo(productQuantity);
//        check if there is only one kind of product in cart
        int cartListSize = home.getCart().expandCart().size();
        softly.assertThat(cartListSize)
                .withFailMessage("There are %d entries in cart product list, instead of 1", cartListSize)
                .isEqualTo(1);

        CartBlockProductPage productInCart = home.getCart().findProductInCart(productName);
//        assert product properties in cart
        softly.assertThat(productInCart.getFullName())
                .withFailMessage("Product full name in cart is different then %s", productName)
                .isEqualTo(productName);
        softly.assertThat(productInCart.getQuantity())
                .withFailMessage("Quantity of product in the cart is different then %d", productQuantity)
                .isEqualTo(productQuantity);
        softly.assertThat(productInCart.getPriceValue())
                .withFailMessage("Total product price differs from original price multiplied by %d",
                        productQuantity)
                .isEqualTo(bestSeller.getPriceValue().multiply(new BigDecimal(productQuantity)));

//        assert if total cost is product price multiplied by 3 plus shipping cost
        BigDecimal expectedTotalCost = bestSeller.getPriceValue()
                .multiply(new BigDecimal(productQuantity))
                .add(home.getCart().getShippingCostValue());
        softly.assertThat(home.getCart().getTotalCostValue())
                .withFailMessage("Total cost displayed in cart differs from sum of shipping cost and" +
                        "total product price").isEqualTo(expectedTotalCost);

        Allure.step(String.format("Assert if cart label displays \"%d %s\"",
                productQuantity,
                expectedCartProductLabel));
        Allure.step("Assert if there is one product on cart list");
        Allure.step(String.format("Assert if product in cart: " +
                        "1) is %s " +
                        "2) has quantity :%d " +
                        "3) has original price multiplied by %d ",
                productName, productQuantity, productQuantity));
        Allure.step("Assert if total cost is a sum of total product price and shipping costs");
        softly.assertAll();
    }

    @Test
    @DisplayName("Add three different products from Best Sellers to cart")
    public void shouldSeeThreeDifferentProductsFromBestSellersInCart() {
        String expectedCartProductLabel = appProperties.getCartProductsLabel();
        String product_1_name = testData.getProductNames()[0];
        String product_2_name = testData.getProductNames()[1];
        String product_3_name = testData.getProductNames()[2];
//        add first product to cart
        BestSellerProductPage bestSeller_1 = home.goToBestSellers().getProduct(product_1_name);
        bestSeller_1.hover().addToCart().closeWindow();
//        add second product to cart
        BestSellerProductPage bestSeller_2 = home.goToBestSellers().getProduct(product_2_name);
        bestSeller_2.hover().addToCart().closeWindow();
//        add third product to cart
        BestSellerProductPage bestSeller_3 = home.goToBestSellers().getProduct(product_3_name);
        bestSeller_3.hover().addToCart().closeWindow();

        SoftAssertions softly = new SoftAssertions();

//        check label on cart bar
        softly.assertThat(home.getCart().getLabel())
                .withFailMessage("Displayed label is different then \"%s\"", expectedCartProductLabel)
                .isEqualTo(expectedCartProductLabel);
//        check quantity on cart bar
        softly.assertThat(home.getCart().getQuantity())
                .withFailMessage("Quantity on cart label is different then 3").isEqualTo(3);
        int cartListSize = home.getCart().expandCart().size();
//        check if there are three items in cart
        softly.assertThat(cartListSize)
                .withFailMessage("There are %d entries in cart product list, instead of 3", cartListSize)
                .isEqualTo(3);
//        check if first, second and third products are in cart
        softly.assertThat(home.getCart().getProducts())
                .withFailMessage("Some of products %s, %s, %s are not displayed in the cart",
                        product_1_name, product_2_name, product_3_name)
                .extracting(CartBlockProductPage::getFullName,
                        CartBlockProductPage::getPrice,
                        CartBlockProductPage::getQuantity)
                .contains(tuple(product_1_name, bestSeller_1.getPrice(), 1),
                        tuple(product_2_name, bestSeller_2.getPrice(), 1),
                        tuple(product_3_name, bestSeller_3.getPrice(), 1));
//        check if total price is a sum of all product prices plus shipping cost
        BigDecimal expectedTotalCost = bestSeller_1.getPriceValue()
                .add(bestSeller_2.getPriceValue())
                .add(bestSeller_3.getPriceValue())
                .add(home.getCart().getShippingCostValue());
        assertThat(home.getCart().getTotalCostValue())
                .withFailMessage("Total cost in cart is different then expected")
                .isEqualTo(expectedTotalCost);

        Allure.step(String.format("Assert if cart label displays \"3 %s\"",
                expectedCartProductLabel));
        Allure.step("Assert if cart list has three entries");
        Allure.step(String.format("Assert if following products are in cart %s, %s, %s",
                product_1_name, product_2_name, product_3_name));
        Allure.step("Assert if total cost in cart is sum of separate product prices and shipping cost");
        softly.assertAll();
    }

    @Test
    @DisplayName("Display part of product name in cart if name is too long")
    public void shouldSeePartOfProductNameInCartIfNameTooLong() {
        String productNameLong = testData.getProductNames()[1];
        home.goToBestSellers().getProduct(productNameLong).hover().addToCart().closeWindow();
        String actualProductNameInCart = home.getCart().findProductInCart(productNameLong).getName();

        assertThat(actualProductNameInCart)
                .withFailMessage("Product name in Best Sellers is the same as in cart")
                .isNotEqualTo(productNameLong);
        assertThat(actualProductNameInCart)
                .withFailMessage("Product name in the cart does not end with \"...\"")
                .endsWith("...");
        assertThat(productNameLong)
                .withFailMessage("Product name in Best Sellers does not start with beginning of name " +
                        "displayed in cart")
                .startsWith(actualProductNameInCart.replaceAll(".", ""));

        Allure.step(String.format("Assert if product name in cart is \"%s\" for product %s",
                actualProductNameInCart,
                productNameLong));
    }

    @Test
    @DisplayName("Remove all products from cart")
    public void shouldRemoveProductsFromCartAndCollapse() {
//        add first product to cart
        String product_1_name = testData.getProductNames()[1];
        BestSellerProductPage product_1 = home.goToBestSellers().getProduct(product_1_name);
        BigDecimal product_1_price = product_1.getPriceValue();
        product_1.hover().addToCart().closeWindow();

//        add second product to cart
        String product_2_name = testData.getProductNames()[2];
        BestSellerProductPage product_2 = home.goToBestSellers().getProduct(product_2_name);
        BigDecimal product_2_price = product_2.getPriceValue();
        product_2.hover().addToCart().closeWindow();

//        check if cart expands
        assertThat(home.getCart().hoverOnCart().isCartContainerDisplayed()).
                withFailMessage("Cart is not expanded when hover on after adding second product to cart")
                .isTrue();
        Allure.step("Assert if cart is expanded");

//        check if cart quantity label is 2
        assertThat(home.getCart().getQuantity())
                .withFailMessage("Quantity on cart label is different then 2").isEqualTo(2);
        Allure.step("Assert if cart label displays 2 products");

//        remove first product from cart
        home.getCart().findProductInCart(product_1_name).removeProduct();

//        check if cart remains expanded
        assertThat(home.getCart().isCartContainerDisplayed()).
                withFailMessage("Cart is not expanded after removing first product from cart")
                .isTrue();
        Allure.step("Assert if cart is still expanded");

//        check if product list in cart decreased to 1
        await().untilAsserted(() -> assertThat(home.getCart().getProducts().size())
                .withFailMessage("There are 2 entries in cart product list, instead of 1")
                .isEqualTo(1));
        Allure.step("Assert if number of entries in cart product list decreased by 1");

//        check if cart quantity label is 1
        assertThat(home.getCart().getQuantity())
                .withFailMessage("Quantity on cart label is different then 1").isEqualTo(1);
        Allure.step("Assert if cart label displays 1 product");

//        check if total cost has decreased by cost of first product
        BigDecimal expectedTotalCost = product_2_price.add(home.getCart().getShippingCostValue());
        assertThat(home.getCart().getTotalCostValue())
                .withFailMessage("Total cost in cart is different then sum of product %s price " +
                        "and shipping cost", product_2_name)
                .isEqualTo(expectedTotalCost);
        Allure.step(String.format("Assert if total cost decreased by price of %s", product_1_name));

//        remove second product from cart
        home.getCart().findProductInCart(product_2_name).removeProduct();
        assertThat(home.getCart().isCartContainerCollapsed()).
                withFailMessage("Cart is not collapsed")
                .isTrue();
        Allure.step("Assert if cart is collapsed");

//        check if product list's size in cart is 0
        await().untilAsserted(() -> assertThat(home.getCart().getProducts().size())
                .withFailMessage("There is 1 entry in cart product list, but cart should be empty")
                .isEqualTo(0));
        Allure.step("Assert if cart product list is empty");

//        check if cart quantity label says empty
        String expectedCartProductLabel = appProperties.getCartEmptyLabel();
        assertThat(home.getCart().getLabel())
                .withFailMessage("Displayed label is different then \"%s\"", expectedCartProductLabel)
                .isEqualTo(expectedCartProductLabel);
        Allure.step(String.format("Assert if cart label changed to %s", expectedCartProductLabel));
    }

    @Test
    @DisplayName("Go to product page after click on product name in cart")
    public void shouldGoToProductPageAfterClickOnProductNameInCart() {
        String productName = testData.getProductNames()[1];
        home.goToBestSellers().getProduct(productName).hover().addToCart().closeWindow();
        ProductPage productPage = home.getCart().findProductInCart(productName).clickOnName();
        assertThat(productPage.getName())
                .withFailMessage("Redirected to page with different product then %s", productName)
                .isEqualTo(productName);
        Allure.step(String.format("Assert if redirected to product page of %s", productName));
    }

    @Test
    @DisplayName("Go to product page after click on product image in cart")
    public void shouldGoToProductPageAfterClickOnProductImgInCart() {
        String productName = testData.getProductNames()[2];
        home.goToBestSellers().getProduct(productName).hover().addToCart().closeWindow();
        ProductPage productPage = home.getCart().findProductInCart(productName).clickOnImg();
        assertThat(productPage.getName())
                .withFailMessage("Redirected to page with different product then %s", productName)
                .isEqualTo(productName);
        Allure.step(String.format("Assert if redirected to product page of %s", productName));
    }

    @Test
    @DisplayName("Go to product page after click on product attributes in cart")
    public void shouldGoToProductPageAfterClickOnProductAttributesInCart() {
        String productName = testData.getProductNames()[1];
        home.goToBestSellers().getProduct(productName).hover().addToCart().closeWindow();
        ProductPage productPage = home.getCart().findProductInCart(productName).clickOnAttributes();
        assertThat(productPage.getName())
                .withFailMessage("Redirected to page with different product then %s", productName)
                .isEqualTo(productName);
        Allure.step(String.format("Assert if redirected to product page of %s", productName));
    }

    @Test
    @DisplayName("Check out to cart page")
    public void shouldGoToCartPageAfterCheckOut() {
        String productName = testData.getProductNames()[2];
        home.goToBestSellers().getProduct(productName).hover().addToCart().closeWindow();
        String cartTitle = home.getCart().hoverOnCart().checkOut().getCartTitle();
        String expectedCartPageTitle = appProperties.getCartPageTitle();

        assertThat(cartTitle.toLowerCase())
                .withFailMessage("Not redirected to cart page with title \"%s\"",
                        expectedCartPageTitle)
                .contains(expectedCartPageTitle.toLowerCase());
        Allure.step(String.format("Assert if redirected to page with title", expectedCartPageTitle));
    }

    @Test
    @DisplayName("Go to cart page when click on drop down cart bar")
    public void shouldGoToCartPageAfterClickOnCartBar() {
        String productName = testData.getProductNames()[2];
        home.goToBestSellers().getProduct(productName).hover().addToCart().closeWindow();
        String cartTitle = home.getCart().hoverOnCart().clickOnCartBar().getCartTitle();
        String expectedCartPageTitle = appProperties.getCartPageTitle();

        assertThat(cartTitle.toLowerCase())
                .withFailMessage("Not redirected to cart page with title \"%s\"",
                        expectedCartPageTitle)
                .contains(expectedCartPageTitle.toLowerCase());
        Allure.step(String.format("Assert if redirected to page with title", expectedCartPageTitle));
    }
}
