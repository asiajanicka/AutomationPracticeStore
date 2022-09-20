package tests;

import enums.SortProductsOptions;
import enums.Stock;
import fileLoaders.ProductTestDataFromCSVReader;
import io.qameta.allure.Allure;
import io.qameta.allure.Feature;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import pageObjects.base.Product;
import pageObjects.base.ProductBasePage;
import pageObjects.categoryPages.CategoryPage;
import pageObjects.categoryPages.CategoryProductPage;
import pageObjects.homePages.HomePage;
import utils.SimpleProduct;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@Feature("Category Page")
public class CategoryPageTests extends BaseTest {

    private HomePage home;
    private ProductTestDataFromCSVReader testDataProducts;
    int numberOfProductsOnPage;

    @BeforeEach
    public void testSetup() {
        super.testSetup();
        driver.navigate().to(configuration.getBaseUrl());
        home = new HomePage(driver);
    }

    @BeforeAll
    public void initialSetup() {
        super.initialSetup();
        testDataProducts = new ProductTestDataFromCSVReader();
        numberOfProductsOnPage = 8;
    }

    @Test
    @DisplayName("CP1 Display all required info for category page (Women)")
    public void CP1_displayWomenCategoryInfo(){
        String category = "Women";
        List<Product> testDataProductsFiltered = testDataProducts
                .getProducts()
                .stream()
                .filter(product -> product.getCategory().equalsIgnoreCase(category))
                .distinct()
                .collect(Collectors.toList());
        List<String> testDataSubcategories = testDataProductsFiltered
                .stream()
                .map(p -> p.getSubcategory1stLevel().toUpperCase())
                .distinct()
                .collect(Collectors.toList());
        List<Integer> testDataProductsFiltered_Ids = testDataProductsFiltered
                .stream()
                .map(Product::getId)
                .distinct()
                .collect(Collectors.toList());

        CategoryPage categoryPage = home
                .getTopMenu()
                .goToWomenPage();
        List<Integer> productsIds = categoryPage
                .getProducts()
                .stream()
                .map(ProductBasePage::getId)
                .collect(Collectors.toList());


        SoftAssertions soft = new SoftAssertions();
//        category name
        soft.assertThat(categoryPage.getCategoryName())
                .as("Category name is different then expected")
                .isEqualToIgnoringCase(category);
        Allure.step(String.format("Assert category name is :'%s'", category));
//        category desc must not be empty
        soft.assertThat(categoryPage.getCategoryDesc())
                .withFailMessage("Category description is empty").isNotEmpty();
        Allure.step("Assert if category desc in not empty");
//        page header
        soft.assertThat(categoryPage.getPageHeader())
                .as("Category header is different then expected")
                .isEqualToIgnoringCase(category);
        Allure.step(String.format("Assert page header is :'%s'", category));
//        subcategories
        soft.assertThat(categoryPage.getSubcategoriesNames())
                .as("Subcategories on page are different then expected")
                .containsExactlyInAnyOrderElementsOf(testDataSubcategories);
        Allure.step(String.format("Assert if subcategories are: %s", testDataSubcategories));
//        header counter
        soft.assertThat(categoryPage.getNumberOfProductsFromProductCounter())
                .as("Product counter is different then number of products filtered from test data")
                .isEqualTo(testDataProductsFiltered.size());
        Allure.step("Assert if header product counter equals to number of products filtered from test data");
//       if pagination is correct
        verifyPagination(categoryPage,testDataProductsFiltered.size(), soft, numberOfProductsOnPage);
//        all products in category are displayed
        soft.assertThat(productsIds)
                .as(String.format("Products displayed in '%s' are different then those filtered from test data", category))
                .containsExactlyInAnyOrderElementsOf(testDataProductsFiltered_Ids);
        Allure.step("Assert is products displayed in category are the same as expected");
        soft.assertAll();
    }

    @Test
    @DisplayName("CP2 Display all required info for 1st level subcategory page (Women/Tops)")
    public void CP2_displayWomen1LevelSubcategoryInfo(){
        String category = "Women";
        String subcategory = "Tops";
        List<Product> testDataProductsFiltered = testDataProducts
                .getProducts()
                .stream()
                .filter(product -> product.getCategory().equalsIgnoreCase(category))
                .filter(p -> p.getSubcategory1stLevel().equalsIgnoreCase(subcategory))
                .distinct()
                .collect(Collectors.toList());
        List<String> testData2ndLevelSubcategories = testDataProductsFiltered
                .stream()
                .map(p -> p.getSubcategory2ndLevel().toUpperCase())
                .distinct()
                .collect(Collectors.toList());
        List<Integer> testDataProductsFiltered_Ids = testDataProductsFiltered
                .stream()
                .map(Product::getId)
                .distinct()
                .collect(Collectors.toList());

        CategoryPage categoryPage = home
                .getTopMenu()
                .hoverOnWomenMenu()
                .getWomenSubcategory(subcategory)
                .goToPage();
        List<Integer> productsIds = categoryPage
                .getProducts()
                .stream()
                .map(ProductBasePage::getId)
                .collect(Collectors.toList());

        SoftAssertions soft = new SoftAssertions();
//        category name
        soft.assertThat(categoryPage.getCategoryName())
                .as("Category name is different then expected subcategory")
                .isEqualToIgnoringCase(subcategory);
        Allure.step(String.format("Assert category name is :'%s'", subcategory));
//        category desc must not be empty
        soft.assertThat(categoryPage.getCategoryDesc())
                .withFailMessage("Category description is empty").isNotEmpty();
        Allure.step("Assert if category desc in not empty");
//        page header
        soft.assertThat(categoryPage.getPageHeader())
                .as("Category header is different then expected subcategory")
                .isEqualToIgnoringCase(subcategory);
        Allure.step(String.format("Assert page header is :'%s'", subcategory));
//        subcategories
        soft.assertThat(categoryPage.getSubcategoriesNames())
                .as("Subcategories on page are different then expected")
                .containsExactlyInAnyOrderElementsOf(testData2ndLevelSubcategories);
        Allure.step(String.format("Assert if subcategories are: %s", testData2ndLevelSubcategories));
//        header counter
        soft.assertThat(categoryPage.getNumberOfProductsFromProductCounter())
                .as("Product counter is different then number of products filtered from test data")
                .isEqualTo(testDataProductsFiltered.size());
        Allure.step("Assert if header product counter equals to number of products filtered from test data");
//       if pagination is correct
        verifyPagination(categoryPage,testDataProductsFiltered.size(), soft, numberOfProductsOnPage);
//        all products in category are displayed
        soft.assertThat(productsIds)
                .as(String.format("Products displayed in '%s' are different then those filtered from test data", category))
                .containsExactlyInAnyOrderElementsOf(testDataProductsFiltered_Ids);
        Allure.step("Assert is products displayed in category are the same as expected");
        soft.assertAll();
    }

    @Test
    @DisplayName("CP3 Display all required info for 2 level subcategory page (Women/Tops/T-shirts)")
    public void CP3_displayWomen2ndLevelSubcategoryInfo(){
        String category = "Women";
        String subcategory1 = "Tops";
        String subcategory2 = "T-shirts";
        List<Product> testDataProductsFiltered = testDataProducts
                .getProducts()
                .stream()
                .filter(product -> product.getCategory().equalsIgnoreCase(category))
                .filter(p -> p.getSubcategory1stLevel().equalsIgnoreCase(subcategory1))
                .filter(p->p.getSubcategory2ndLevel().equalsIgnoreCase(subcategory2))
                .distinct()
                .collect(Collectors.toList());
        List<Integer> testDataProductsFiltered_Ids = testDataProductsFiltered
                .stream()
                .map(Product::getId)
                .distinct()
                .collect(Collectors.toList());

        CategoryPage categoryPage = home
                .getTopMenu()
                .hoverOnWomenMenu()
                .getWomenSubcategory(subcategory1)
                .getSubcategory(subcategory2)
                .goToPage();
        List<Integer> productsIds = categoryPage
                .getProducts()
                .stream()
                .map(ProductBasePage::getId)
                .collect(Collectors.toList());

        SoftAssertions soft = new SoftAssertions();
//        category name
        soft.assertThat(categoryPage.getCategoryName())
                .as("Category name is different then expected subcategory")
                .isEqualToIgnoringCase(subcategory2);
        Allure.step(String.format("Assert category name is :'%s'", category));
//        category desc must not be empty
        soft.assertThat(categoryPage.getCategoryDesc())
                .withFailMessage("Category description is empty").isNotEmpty();
        Allure.step("Assert if category desc in not empty");
//        page header
        soft.assertThat(categoryPage.getPageHeader())
                .as("Category header is different then expected subcategory")
                .isEqualToIgnoringCase(subcategory2);
        Allure.step(String.format("Assert page header is :'%s'", subcategory1));
//        subcategories
        soft.assertThat(categoryPage.areSubcategoriesDisplayed())
                .as("Subcategories are displayed while they shouldn't be")
                .isFalse();
        Allure.step("Assert if subcategories block is not displayed");
//        header counter
        soft.assertThat(categoryPage.getNumberOfProductsFromProductCounter())
                .as("Product counter is different then number of products filtered from test data")
                .isEqualTo(testDataProductsFiltered.size());
        Allure.step("Assert if header product counter equals to number of products filtered from test data");
//       if pagination is correct
        verifyPagination(categoryPage,testDataProductsFiltered.size(), soft, numberOfProductsOnPage);
//        all products in category are displayed
        soft.assertThat(productsIds)
                .as(String.format("Products displayed in '%s' are different then those filtered from test data", category))
                .containsExactlyInAnyOrderElementsOf(testDataProductsFiltered_Ids);
        Allure.step("Assert is products displayed in category are the same as expected");
        soft.assertAll();
    }

    @Test
    @DisplayName("CP4 Display filters for products (Women)")
    public void CP4_displayFiltersForWomenProducts() {
        String category = "Women";
        List<Product> testDataProductsFiltered_Women = testDataProducts
                .getProducts()
                .stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
        CategoryPage categoryPage = home
                .getTopMenu()
                .goToWomenPage();

        SoftAssertions soft = new SoftAssertions();
        verifyCategoriesFilter(categoryPage, testDataProductsFiltered_Women, soft, 1);
        verifySizeFilter(categoryPage, testDataProductsFiltered_Women, soft);
        verifyColorFilter(categoryPage, testDataProductsFiltered_Women, soft);
        verifyPropertiesFilter(categoryPage, testDataProductsFiltered_Women, soft);
        verifyCompositionsFilter(categoryPage, testDataProductsFiltered_Women, soft);
        verifyStylesFilter(categoryPage, testDataProductsFiltered_Women, soft);
        verifyConditionFilter(categoryPage, testDataProductsFiltered_Women, soft);
        verifyInStockFilter(categoryPage, testDataProductsFiltered_Women, soft);
        verifyPriceRangeFilter(categoryPage, testDataProductsFiltered_Women, soft);
        soft.assertAll();
    }

    @Test
    @DisplayName("CP5 Display filters for products (Dresses)")
    public void CP5_displayFiltersForDressesProducts() {
        String category = "Dresses";
        List<Product> testDataProductsFiltered_Dresses = testDataProducts
                .getProducts()
                .stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
        CategoryPage categoryPage = home
                .getTopMenu()
                .goToDressesPage();

        SoftAssertions soft = new SoftAssertions();
        verifyCategoriesFilter(categoryPage, testDataProductsFiltered_Dresses, soft, 1);
        verifySizeFilter(categoryPage, testDataProductsFiltered_Dresses, soft);
        verifyColorFilter(categoryPage, testDataProductsFiltered_Dresses, soft);
        verifyPropertiesFilter(categoryPage, testDataProductsFiltered_Dresses, soft);
        verifyCompositionsFilter(categoryPage, testDataProductsFiltered_Dresses, soft);
        verifyStylesFilter(categoryPage, testDataProductsFiltered_Dresses, soft);
        verifyConditionFilter(categoryPage, testDataProductsFiltered_Dresses, soft);
        verifyInStockFilter(categoryPage, testDataProductsFiltered_Dresses, soft);
        verifyPriceRangeFilter(categoryPage, testDataProductsFiltered_Dresses, soft);
        soft.assertAll();
    }

    @Test
    @DisplayName("CP6 Display filters for products (T-shirts)")
    public void CP6_displayFiltersForTShirtsProducts() {
        String category = "T-shirts";
        List<Product> testDataProductsFiltered_Tshirts = testDataProducts
                .getProducts()
                .stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
        CategoryPage categoryPage = home
                .getTopMenu()
                .goToTShirtsPage();

        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(categoryPage.isCategoriesFilterDisplayed()).isFalse();
        verifySizeFilter(categoryPage, testDataProductsFiltered_Tshirts, soft);
        verifyColorFilter(categoryPage, testDataProductsFiltered_Tshirts, soft);
        verifyPropertiesFilter(categoryPage, testDataProductsFiltered_Tshirts, soft);
        verifyCompositionsFilter(categoryPage, testDataProductsFiltered_Tshirts, soft);
        verifyStylesFilter(categoryPage, testDataProductsFiltered_Tshirts, soft);
        verifyConditionFilter(categoryPage, testDataProductsFiltered_Tshirts, soft);
        verifyInStockFilter(categoryPage, testDataProductsFiltered_Tshirts, soft);
        verifyPriceRangeFilter(categoryPage, testDataProductsFiltered_Tshirts, soft);
        soft.assertAll();
    }

    @Test
    @DisplayName("CP7 Display filters for products in 1st level subcategory (Women/Tops)")
    public void CP7_displayFiltersFor1stLevelSubcategory() {
        String category = "Women";
        String subcategory = "Tops";
        List<Product> testDataProductsFiltered_Subcategory = testDataProducts
                .getProducts()
                .stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .filter(p -> p.getSubcategory1stLevel().equalsIgnoreCase(subcategory))
                .collect(Collectors.toList());
        CategoryPage categoryPage = home
                .getTopMenu()
                .hoverOnWomenMenu()
                .getWomenSubcategory(subcategory)
                .goToPage();

        SoftAssertions soft = new SoftAssertions();
        verifyCategoriesFilter(categoryPage, testDataProductsFiltered_Subcategory, soft, 2);
        verifySizeFilter(categoryPage, testDataProductsFiltered_Subcategory, soft);
        verifyColorFilter(categoryPage, testDataProductsFiltered_Subcategory, soft);
        verifyPropertiesFilter(categoryPage, testDataProductsFiltered_Subcategory, soft);
        verifyCompositionsFilter(categoryPage, testDataProductsFiltered_Subcategory, soft);
        verifyStylesFilter(categoryPage, testDataProductsFiltered_Subcategory, soft);
        verifyConditionFilter(categoryPage, testDataProductsFiltered_Subcategory, soft);
        verifyInStockFilter(categoryPage, testDataProductsFiltered_Subcategory, soft);
        verifyPriceRangeFilter(categoryPage, testDataProductsFiltered_Subcategory, soft);
        soft.assertAll();
    }

    @Test
    @DisplayName("CP8 Display filters for products in 2nd level subcategory (Women/Tops/Blouses)")
    public void CP8_displayFiltersFor2ndLevelSubcategory() {
        String category = "Women";
        String subcategory_1st = "Tops";
        String subcategory_2nd = "Blouses";
        List<Product> testDataProductsFiltered_Subcategory = testDataProducts
                .getProducts()
                .stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .filter(p -> p.getSubcategory1stLevel().equalsIgnoreCase(subcategory_1st))
                .filter(p -> p.getSubcategory2ndLevel().equalsIgnoreCase(subcategory_2nd))
                .collect(Collectors.toList());
        CategoryPage categoryPage = home
                .getTopMenu()
                .hoverOnWomenMenu()
                .getWomenSubcategory(subcategory_1st)
                .getSubcategory(subcategory_2nd)
                .goToPage();

        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(categoryPage.isCategoriesFilterDisplayed()).isFalse();
        verifySizeFilter(categoryPage, testDataProductsFiltered_Subcategory, soft);
        verifyColorFilter(categoryPage, testDataProductsFiltered_Subcategory, soft);
        verifyPropertiesFilter(categoryPage, testDataProductsFiltered_Subcategory, soft);
        verifyCompositionsFilter(categoryPage, testDataProductsFiltered_Subcategory, soft);
        verifyStylesFilter(categoryPage, testDataProductsFiltered_Subcategory, soft);
        verifyConditionFilter(categoryPage, testDataProductsFiltered_Subcategory, soft);
        verifyInStockFilter(categoryPage, testDataProductsFiltered_Subcategory, soft);
        verifyPriceRangeFilter(categoryPage, testDataProductsFiltered_Subcategory, soft);
        soft.assertAll();
    }

    @Test
    @DisplayName("CP9 Sort products by price: lowest first")
    public void CP_9sortProductsByPriceWithLowestFirst() {
        String category = "Women";
        List<Integer> testDataProductsSorted_Ids = testDataProducts
                .getProducts()
                .stream()
                .filter(p -> p.getCategory().equals(category))
                .distinct()
                .sorted(Comparator.comparing(Product::getPrice))
                .map(Product::getId)
                .collect(Collectors.toList());
        CategoryPage categoryPage = home
                .getTopMenu()
                .goToWomenPage()
                .sortProductsBy(SortProductsOptions.PRICE_ASC);
        List<Integer> productsIds = categoryPage
                .getProducts()
                .stream()
                .map(ProductBasePage::getId)
                .collect(Collectors.toList());

        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(productsIds)
                .as("Products ids are not sorted by product price")
                .containsExactlyElementsOf(testDataProductsSorted_Ids);
        Allure.step("Assert if products are sorted by product price");
        soft.assertThat(categoryPage.getNumberOfProductsFromProductCounter())
                .as("Product counter is different then expected")
                .isEqualTo(testDataProductsSorted_Ids.size());
        Allure.step("Assert if header product counter equals to number of products filtered from test data");
        verifyPagination(categoryPage,testDataProductsSorted_Ids.size(), soft, numberOfProductsOnPage);
        soft.assertAll();
    }

    @Test
    @DisplayName("CP10 Sort products by price: highest first")
    public void CP10_sortProductsByPriceWithHighestFirst() {
        String category = "Women";
        List<Integer> testDataProductsSorted_Ids = testDataProducts
                .getProducts()
                .stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .distinct()
                .sorted(Comparator.comparing(Product::getPrice).reversed())
                .map(Product::getId)
                .collect(Collectors.toList());
        CategoryPage categoryPage = home
                .getTopMenu()
                .goToWomenPage()
                .sortProductsBy(SortProductsOptions.PRICE_DESC);
        List<Integer> productsIds = categoryPage
                .getProducts()
                .stream()
                .map(ProductBasePage::getId)
                .collect(Collectors.toList());

        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(productsIds)
                .as("Products ids are not sorted by product price")
                .containsExactlyElementsOf(testDataProductsSorted_Ids);
        Allure.step("Assert if products are sorted by product price desc");
        soft.assertThat(categoryPage.getNumberOfProductsFromProductCounter())
                .as("Product counter is different then expected")
                .isEqualTo(testDataProductsSorted_Ids.size());
        Allure.step("Assert if header product counter equals to number of products filtered from test data");
        verifyPagination(categoryPage,testDataProductsSorted_Ids.size(), soft, numberOfProductsOnPage);
        soft.assertAll();
    }

    @Test
    @DisplayName("CP11 Sort products by product name: A to Z")
    public void CP11_sortProductsByName() {
        String category = "Women";
        List<Integer> testDataProductsSorted_Ids = testDataProducts
                .getProducts()
                .stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .distinct()
                .sorted(Comparator.comparing(Product::getName))
                .map(Product::getId)
                .collect(Collectors.toList());
        CategoryPage categoryPage = home
                .getTopMenu()
                .goToWomenPage()
                .sortProductsBy(SortProductsOptions.NAME_ASC);
        List<Integer> productsIds = categoryPage
                .getProducts()
                .stream()
                .map(ProductBasePage::getId)
                .collect(Collectors.toList());

        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(productsIds)
                .as("Products ids are not sorted by product name")
                .containsExactlyElementsOf(testDataProductsSorted_Ids);
        Allure.step("Assert if products are sorted by product name");
        soft.assertThat(categoryPage.getNumberOfProductsFromProductCounter())
                .as("Product counter is different then expected")
                .isEqualTo(testDataProductsSorted_Ids.size());
        Allure.step("Assert if header product counter equals to number of products filtered from test data");
        verifyPagination(categoryPage,testDataProductsSorted_Ids.size(), soft, numberOfProductsOnPage);
        soft.assertAll();
    }

    @Test
    @DisplayName("CP12 Sort products by product name: Z to A")
    public void CP12_sortProductsByNameDesc() {
        String category = "Women";
        List<Integer> testDataProductsSorted_Ids = testDataProducts
                .getProducts()
                .stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .distinct()
                .sorted(Comparator.comparing(Product::getName).reversed())
                .map(Product::getId)
                .collect(Collectors.toList());
        CategoryPage categoryPage = home
                .getTopMenu()
                .goToWomenPage()
                .sortProductsBy(SortProductsOptions.NAME_DESC);
        List<Integer> productsIds = categoryPage
                .getProducts()
                .stream()
                .map(ProductBasePage::getId)
                .collect(Collectors.toList());

        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(productsIds)
                .as("Products ids are not sorted by product name")
                .containsExactlyElementsOf(testDataProductsSorted_Ids);
        Allure.step("Assert if products are sorted by product name in reversed order");
        soft.assertThat(categoryPage.getNumberOfProductsFromProductCounter())
                .as("Product counter is different then expected")
                .isEqualTo(testDataProductsSorted_Ids.size());
        Allure.step("Assert if header product counter equals to number of products filtered from test data");
        verifyPagination(categoryPage,testDataProductsSorted_Ids.size(), soft, numberOfProductsOnPage);
        soft.assertAll();
    }
    @Test
    @DisplayName("CP13 Sort products by in stock availability")
    public void CP13_sortProductsThatAreInStock() {
        String category = "Women";
        List<Product> testDataProductsFiltered_InStock = testDataProducts
                .getProducts()
                .stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .filter(p -> p.getAvailability().equals(Stock.IN_STOCK))
                .collect(Collectors.toList());
        List<   Product> testDataProductsDistinct  = testDataProductsFiltered_InStock
                .stream()
                .distinct()
                .collect(Collectors.toList());
        List<SimpleProduct> testDataProductsFiltered_InStock_Short = new ArrayList<>();
        for (Product product : testDataProductsFiltered_InStock) {
            testDataProductsFiltered_InStock_Short.add(new SimpleProduct(product));
        }

        CategoryPage categoryPage = home
                .getTopMenu()
                .goToWomenPage()
                .sortProductsBy(SortProductsOptions.QUANTITY_DESC);

        List<SimpleProduct> womenProductsShort = new ArrayList<>();
        for (CategoryProductPage product : categoryPage.getProducts()) {
            int id = product.getId();
            String name = product.getName();
            BigDecimal price = product.getPriceValue();
            for (String color : product.getColors()){
                womenProductsShort.add(new SimpleProduct(id, name, price, color.toLowerCase()));
            }
        }
        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(womenProductsShort)
                .as("Displayed products that are in stock are different then expected")
                .containsExactlyInAnyOrderElementsOf(testDataProductsFiltered_InStock_Short);
        Allure.step("Assert if products displayed are in stock");
        soft.assertThat(categoryPage.getNumberOfProductsFromProductCounter())
                .as("Product counter is different then expected")
                .isEqualTo(testDataProductsDistinct.size());
        Allure.step("Assert if header product counter equals to number of products filtered from test data");
        verifyPagination(categoryPage,testDataProductsDistinct.size(), soft, numberOfProductsOnPage);
        soft.assertAll();
    }

    @Test
    @DisplayName("CP14 Sort products by reference: lowest first")
    public void CP14_sortProductsByReferenceWithLowestFirst(){
        String category = "Women";
        List<Integer> testDataProductsSorted_Ids = testDataProducts
                .getProducts()
                .stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .distinct()
                .sorted(Comparator.comparing(Product::getReference))
                .map(Product::getId)
                .collect(Collectors.toList());

        CategoryPage categoryPage = home
                .getTopMenu()
                .goToWomenPage()
                .sortProductsBy(SortProductsOptions.REFERENCE_ASC);

        List<Integer> productsIds = categoryPage
                .getProducts()
                .stream()
                .map(ProductBasePage::getId)
                .collect(Collectors.toList());

        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(productsIds)
                .as("Products ids are not sorted by product reference")
                .containsExactlyElementsOf(testDataProductsSorted_Ids);
        Allure.step("Assert if products are sorted by product reference");
        soft.assertThat(categoryPage.getNumberOfProductsFromProductCounter())
                .as("Product counter is different then expected")
                .isEqualTo(testDataProductsSorted_Ids.size());
        Allure.step("Assert if header product counter equals to number of products filtered from test data");
        verifyPagination(categoryPage,testDataProductsSorted_Ids.size(), soft, numberOfProductsOnPage);
        soft.assertAll();
    }

    @Test
    @DisplayName("CP15 Sort products by reference: highest first")
    public void CP15_sortProductsByReferenceWithHighestFirst(){
        String category = "Women";
        List<Integer> testDataProductsSorted_Ids = testDataProducts
                        .getProducts()
                        .stream()
                        .filter(p -> p.getCategory().equalsIgnoreCase(category))
                        .distinct()
                        .sorted(Comparator.comparing(Product::getReference).reversed())
                        .map(Product::getId)
                        .collect(Collectors.toList());

        CategoryPage categoryPage = home
                .getTopMenu()
                .goToWomenPage()
                .sortProductsBy(SortProductsOptions.REFERENCE_DESC);

        List<Integer> productsIds = categoryPage
                .getProducts()
                .stream()
                .map(ProductBasePage::getId)
                .collect(Collectors.toList());

        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(productsIds)
                .as("Products ids are not sorted by product reference")
                .containsExactlyElementsOf(testDataProductsSorted_Ids);
        Allure.step("Assert if products are sorted by product reference desc");
        soft.assertThat(categoryPage.getNumberOfProductsFromProductCounter())
                .as("Product counter is different then expected")
                .isEqualTo(testDataProductsSorted_Ids.size());
        Allure.step("Assert if header product counter equals to number of products filtered from test data");
        verifyPagination(categoryPage,testDataProductsSorted_Ids.size(), soft, numberOfProductsOnPage);
        soft.assertAll();
    }

    @Test
    @DisplayName("CP16 Filter products by single category")
    public void CP16_filterProductsBySingleCategoryInWomen(){
        String category = "Women";
        String subcategory = "Tops";
        List<Product> testDataProducts_Women = testDataProducts
                .getProducts()
                .stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
        List<Product> testDataProductsFiltered = testDataProducts_Women
                .stream()
                .filter(p -> p.getSubcategory1stLevel().equalsIgnoreCase(subcategory))
                .collect(Collectors.toList());
        List<Integer> testDataProductsFiltered_Ids = testDataProductsFiltered.stream()
                .map(Product::getId)
                .distinct()
                .collect(Collectors.toList());

        CategoryPage categoryPage = home
                .getTopMenu()
                .goToWomenPage()
                .selectCategoryFilterByName(subcategory);

        List<Integer> productsIds = categoryPage
                .getProducts()
                .stream()
                .map(ProductBasePage::getId)
                .collect(Collectors.toList());
        
        int actualValueOfProductCounter = categoryPage.getNumberOfProductsFromProductCounter();
        int expectedNumberOfProducts = testDataProductsFiltered_Ids.size();
        
        // if displayed products aren't filtered correctly then there is no sense to verify filters items
        assertThat(testDataProductsFiltered_Ids).containsExactlyInAnyOrderElementsOf(productsIds);
        Allure.step("Assert if products are filtered as expected");

        SoftAssertions soft = new SoftAssertions();
        // assert if filter is listed in enabled filters
        soft.assertThat(categoryPage.getEnabledFilters().keySet())
                .as(String.format("Filter for category '%s' is not displayed in enabled filters", subcategory))
                .contains(subcategory);
        Allure.step(String.format("Assert if filter for subcategory '%s' is displayed in enabled filters section", subcategory));
        // assert if header has info about categories filter
        soft.assertThat(categoryPage.getPageHeader())
                .usingComparator(String::compareToIgnoreCase)
                .as(String.format("Category '%s' should be displayed in page header", category))
                .contains(category)
                .contains("categories")
                .as(String.format("Category '%s' should be displayed in page header", subcategory))
                .contains(subcategory);
        Allure.step(String.format("Assert if page header contains '%s' and '%s'", category, subcategory));
        // assert header product counter
        soft.assertThat(actualValueOfProductCounter)
                .as("Value of product counter is different then expected")
                .isEqualTo(expectedNumberOfProducts);
        Allure.step("Assert if header product counter equals to number of products filtered from test data");
        // assert size filter items
        verifySizeFilter(categoryPage, testDataProductsFiltered, soft);
        // assert color filter items
        verifyColorFilter(categoryPage, testDataProductsFiltered, soft);
        // assert properties filter items
        verifyPropertiesFilter(categoryPage, testDataProductsFiltered, soft);
        // assert compositions filter items
        verifyCompositionsFilter(categoryPage, testDataProductsFiltered, soft);
        // assert styles filter items
        verifyStylesFilter(categoryPage, testDataProductsFiltered, soft);
        // assert availability filter items
        verifyInStockFilter(categoryPage, testDataProductsFiltered, soft);
        // assert condition filter items
        verifyConditionFilter(categoryPage, testDataProductsFiltered, soft);
        // assert price range filter settings
        verifyPriceRangeFilter(categoryPage, testDataProducts_Women, soft);
        // assert pagination
        verifyPagination(categoryPage,testDataProductsFiltered_Ids.size(), soft, numberOfProductsOnPage);
        soft.assertAll();
    }

    @Test
    @DisplayName("CP17 Filter products by single category and sort by product name: A to Z")
    public void CP17_filterProductsByCategoryAndSortByProductNameInWomen(){
        String category = "Women";
        String subcategory = "Tops";
        List<Product> testDataProducts_Women = testDataProducts
                .getProducts()
                .stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
        List<Product> testDataProductsFilteredSorted = testDataProducts_Women
                .stream()
                .filter(p -> p.getSubcategory1stLevel().equalsIgnoreCase(subcategory))
                .sorted(Comparator.comparing(Product::getName))
                .collect(Collectors.toList());
        List<Integer> testDataProductsSortedIds = testDataProductsFilteredSorted.stream()
                .map(Product::getId)
                .distinct()
                .collect(Collectors.toList());

        CategoryPage categoryPage = home
                .getTopMenu()
                .goToWomenPage()
                .selectCategoryFilterByName(subcategory)
                .sortProductsBy(SortProductsOptions.NAME_ASC);
        List<Integer> productsIds = categoryPage
                .getProducts()
                .stream()
                .map(ProductBasePage::getId)
                .collect(Collectors.toList());

        int actualValueOfProductCounter = categoryPage.getNumberOfProductsFromProductCounter();
        int expectedNumberOfProducts = testDataProductsSortedIds.size();

        // if displayed products aren't filtered correctly then there is no sense to verify filters items
        assertThat(testDataProductsSortedIds)
                .as("Displayed products are different then expected")
                .containsExactlyElementsOf(productsIds);
        Allure.step("Assert if products are filtered as expected");

        SoftAssertions soft = new SoftAssertions();
        // assert if filter is listed in enabled filters
        soft.assertThat(categoryPage.getEnabledFilters().keySet())
                .as(String.format("Filter for category '%s' should be displayed in enabled filters", subcategory))
                .contains(subcategory);
        Allure.step(String.format("Assert if filters for subcategory '%s' is displayed in enabled filters section", subcategory));
        // assert if header has info about categories filter
        soft.assertThat(categoryPage.getPageHeader())
                .usingComparator(String::compareToIgnoreCase)
                .as(String.format("Category '%s' should be displayed in page header", category))
                .contains(category)
                .contains("categories")
                .as(String.format("Category '%s' should be displayed in page header", subcategory))
                .contains(subcategory);
        Allure.step(String.format("Assert if page header contains '%s' and '%s'", category, subcategory));
        // assert header product counter
        soft.assertThat(actualValueOfProductCounter)
                .as("Value of product counter is different then expected")
                .isEqualTo(expectedNumberOfProducts);
        Allure.step("Assert if header product counter equals to number of products filtered from test data");
        // assert size filter items
        verifySizeFilter(categoryPage, testDataProductsFilteredSorted, soft);
        // assert color filter items
        verifyColorFilter(categoryPage, testDataProductsFilteredSorted, soft);
        // assert properties filter items
        verifyPropertiesFilter(categoryPage, testDataProductsFilteredSorted, soft);
        // assert compositions filter items
        verifyCompositionsFilter(categoryPage, testDataProductsFilteredSorted, soft);
        // assert styles filter items
        verifyStylesFilter(categoryPage, testDataProductsFilteredSorted, soft);
        // assert availability filter items
        verifyInStockFilter(categoryPage, testDataProductsFilteredSorted, soft);
        // assert condition filter items
        verifyConditionFilter(categoryPage, testDataProductsFilteredSorted, soft);
        // assert price range filter settings
        verifyPriceRangeFilter(categoryPage, testDataProducts_Women, soft);
        // assert pagination
        verifyPagination(categoryPage,testDataProductsSortedIds.size(), soft, numberOfProductsOnPage);
        soft.assertAll();
    }

    @Test
    @DisplayName("CP18 Filter products by two categories")
    public void CP18_filterProductsByTwoCategoriesInDresses() {
        String category = "Dresses";
        String subcategory_1 = "Casual Dresses";
        String subcategory_2 = "Evening Dresses";
        List<Product> testDataProducts_Dresses = testDataProducts
                .getProducts()
                .stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
        List<Product> testDataProductsFiltered = testDataProducts_Dresses
                .stream()
                .filter(p -> p.getSubcategory1stLevel().equalsIgnoreCase(subcategory_1) ||
                        p.getSubcategory1stLevel().equalsIgnoreCase(subcategory_2))
                .collect(Collectors.toList());
        List<Integer> testDataProductsFiltered_Ids = testDataProductsFiltered.stream()
                .map(Product::getId)
                .distinct()
                .collect(Collectors.toList());

        CategoryPage categoryPage = home
                .getTopMenu()
                .goToDressesPage()
                .selectCategoryFilterByName(subcategory_1)
                .selectCategoryFilterByName(subcategory_2);
        List<Integer> productsIds = categoryPage
                .getProducts()
                .stream()
                .map(ProductBasePage::getId)
                .collect(Collectors.toList());

        int actualValueOfProductCounter = categoryPage.getNumberOfProductsFromProductCounter();
        int expectedNumberOfProducts = testDataProductsFiltered_Ids.size();

        // if displayed products aren't filtered correctly then there is no sense to verify filters items
        assertThat(testDataProductsFiltered_Ids)
                .as("Displayed products are different then expected")
                .containsExactlyInAnyOrderElementsOf(productsIds);
        Allure.step("Assert if products are filtered as expected");

        SoftAssertions soft = new SoftAssertions();
        // assert if filter is listed in enabled filters
        soft.assertThat(categoryPage.getEnabledFilters().keySet())
                .as(String.format("Enabled filters don't contain item with subcategory '%s'", subcategory_1))
                .contains(subcategory_1)
                .as(String.format("Enabled filters don't contain item with subcategory '%s'", subcategory_2))
                .contains(subcategory_2);
        Allure.step(String.format("Assert if filter for subcategory '%s' and '%s' is displayed in enabled filters section",
                subcategory_1, subcategory_2));
        // assert if header has info about categories filter
        soft.assertThat(categoryPage.getPageHeader())
                .usingComparator(String::compareToIgnoreCase)
                .as(String.format("Category '%s' should be displayed in page header", category))
                .contains(category)
                .contains("categories")
                .as(String.format("Category '%s' should be displayed in page header", subcategory_1))
                .contains(subcategory_1)
                .as(String.format("Category '%s' should be displayed in page header", subcategory_2))
                .contains(subcategory_2);
        Allure.step(String.format("Assert if page header contains '%s', '%s' and '%s'", category, subcategory_1, subcategory_2));
        // assert header product counter
        soft.assertThat(actualValueOfProductCounter)
                .as("Value of product counter is different then expected")
                .isEqualTo(expectedNumberOfProducts);
        Allure.step("Assert if header product counter equals to number of products filtered from test data");
        // assert size filter items
        verifySizeFilter(categoryPage, testDataProductsFiltered, soft);
        // assert color filter items
        verifyColorFilter(categoryPage, testDataProductsFiltered, soft);
        // assert properties filter items
        verifyPropertiesFilter(categoryPage, testDataProductsFiltered, soft);
        // assert compositions filter items
        verifyCompositionsFilter(categoryPage, testDataProductsFiltered, soft);
        // assert styles filter items
        verifyStylesFilter(categoryPage, testDataProductsFiltered, soft);
        // assert availability filter items
        verifyInStockFilter(categoryPage, testDataProductsFiltered, soft);
        // assert condition filter items
        verifyConditionFilter(categoryPage, testDataProductsFiltered, soft);
        // assert price range filter settings
        verifyPriceRangeFilter(categoryPage, testDataProducts_Dresses, soft);
        // assert pagination
        verifyPagination(categoryPage,testDataProductsFiltered_Ids.size(), soft, numberOfProductsOnPage);
        soft.assertAll();
    }

    @Test
    @DisplayName("CP19 Disable category filter")
    public void CP19_disableCategoryFilter(){
        String category = "Dresses";
        String subcategory_1 = "Casual Dresses";
        String subcategory_2 = "Evening Dresses";
        List<Integer> testDataProductsFiltered_Ids = testDataProducts
                .getProducts()
                .stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .distinct()
                .map(Product::getId)
                .collect(Collectors.toList());
        CategoryPage categoryPage = home
                .getTopMenu()
                .goToDressesPage()
                .selectCategoryFilterByName(subcategory_1)
                .selectCategoryFilterByName(subcategory_2);
        List<Integer> productsIds = categoryPage
                .disableFilterByValue(subcategory_1)
                .disableFilterByValue(subcategory_2)
                .getProducts()
                .stream()
                .map(ProductBasePage::getId)
                .collect(Collectors.toList());
        assertThat(categoryPage.getEnabledFilters())
                .as("Enabled filters should be empty")
                .isEmpty();
        Allure.step("Assert if enabled filters section is empty");
        assertThat(testDataProductsFiltered_Ids)
                .as("Displayed products are different then expected")
                .containsExactlyInAnyOrderElementsOf(productsIds);
        Allure.step("Assert if displayed products are the same as originally before filter");
    }

    @Test
    @DisplayName("CP20 Filter products by single color")
    public void CP20_filterProductsBySingleColorInWomen() {
        String category = "Women";
        String color = "Pink";
        List<Product> testDataProducts_Women = testDataProducts
                .getProducts()
                .stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
        List<Product> testDataProductsFiltered = testDataProducts_Women
                .stream()
                .filter(p -> p.getColor().equalsIgnoreCase(color))
                .collect(Collectors.toList());
        List<Integer> testDataProductsFiltered_Ids = testDataProductsFiltered
                .stream()
                .map(Product::getId)
                .collect(Collectors.toList());

        CategoryPage categoryPage = home
                .getTopMenu()
                .goToWomenPage()
                .selectColorFilterByName(color);
        List<Integer> productsIds = categoryPage
                .getProducts()
                .stream()
                .map(ProductBasePage::getId)
                .collect(Collectors.toList());

        int actualValueOfProductCounter = categoryPage.getNumberOfProductsFromProductCounter();
        int expectedNumberOfProducts = testDataProductsFiltered_Ids.size();

        // if displayed products aren't filtered correctly then there is no sense to verify filters items
        assertThat(testDataProductsFiltered_Ids)
                .as("Displayed products are different then expected")
                .containsExactlyInAnyOrderElementsOf(productsIds);
        Allure.step("Assert if products are filtered as expected");

        SoftAssertions soft = new SoftAssertions();
        // assert if filter is listed in enabled filters
        soft.assertThat(categoryPage.getEnabledFilters().keySet())
                .as("Enabled filters don't contain items with color")
                .contains(color);
        Allure.step(String.format("Assert if filter for color '%s' is displayed in enabled filters section", color));
        // assert if header has info about categories filter
        soft.assertThat(categoryPage.getPageHeader())
                .usingComparator(String::compareToIgnoreCase)
                .as(String.format("Category '%s' should be displayed in page header", category))
                .contains(category)
                .as(String.format("Color '%s' should be displayed in page header", color))
                .contains("color")
                .contains(color);
        Allure.step(String.format("Assert if page header contains '%s' and '%s'", category, color));
        // assert header product counter
        soft.assertThat(actualValueOfProductCounter)
                .as("Value of product counter is different then expected")
                .isEqualTo(expectedNumberOfProducts);
        Allure.step("Assert if header product counter equals to number of products filtered from test data");
        // assert size filter items
        verifySizeFilter(categoryPage, testDataProductsFiltered, soft);
        // assert properties filter items
        verifyPropertiesFilter(categoryPage, testDataProductsFiltered, soft);
        // assert compositions filter items
        verifyCompositionsFilter(categoryPage, testDataProductsFiltered, soft);
        // assert styles filter items
        verifyStylesFilter(categoryPage, testDataProductsFiltered, soft);
        // assert availability filter items
        verifyInStockFilter(categoryPage, testDataProductsFiltered, soft);
        // assert condition filter items
        verifyConditionFilter(categoryPage, testDataProductsFiltered, soft);
        // assert price range filter settings
        verifyPriceRangeFilter(categoryPage, testDataProducts_Women, soft);
        // assert pagination
        verifyPagination(categoryPage,testDataProductsFiltered.size(), soft, numberOfProductsOnPage);
        soft.assertAll();
    }

    @Test
    @DisplayName("CP21 Filter products by two colors")
    public void CP21_filterProductsByTwoColorInDresses(){
        String category = "Dresses";
        String color_1 = "Pink";
        String color_2 = "Blue";
        List<Product> testDataProducts_Dresses = testDataProducts
                .getProducts()
                .stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
        List<Product> testDataProductsFiltered = testDataProducts_Dresses
                .stream()
                .filter(p -> p.getColor().equalsIgnoreCase(color_1) || p.getColor().equalsIgnoreCase(color_2))
                .collect(Collectors.toList());
        List<Integer> testDataProductsFiltered_Ids = testDataProductsFiltered
                .stream()
                .map(Product::getId)
                .distinct()
                .collect(Collectors.toList());

        CategoryPage categoryPage = home
                .getTopMenu()
                .goToDressesPage()
                .selectColorFilterByName(color_1)
                .selectColorFilterByName(color_2);
        List<Integer> productsIds = categoryPage
                .getProducts()
                .stream()
                .map(ProductBasePage::getId)
                .collect(Collectors.toList());

        int actualValueOfProductCounter = categoryPage.getNumberOfProductsFromProductCounter();
        int expectedNumberOfProducts = testDataProductsFiltered_Ids.size();

        // if displayed products aren't filtered correctly then there is no sense to verify filters items
        assertThat(testDataProductsFiltered_Ids)
                .as("Displayed products are different then expected")
                .containsExactlyInAnyOrderElementsOf(productsIds);
        Allure.step("Assert if products are filtered as expected");

        SoftAssertions soft = new SoftAssertions();
        // assert if filter is listed in enabled filters
        soft.assertThat(categoryPage.getEnabledFilters().keySet())
                .as(String.format("Filter for color '%s' is not displayed in enabled filters", color_1))
                .contains(color_1)
                .as(String.format("Filter for color '%s' is not displayed in enabled filters", color_2))
                .contains(color_2);
        Allure.step(String.format("Assert if filters for colors '%s' and '%s' are displayed in enabled filters section",
                color_1, color_2));
        // assert if header has info about categories filter
        soft.assertThat(categoryPage.getPageHeader())
                .usingComparator(String::compareToIgnoreCase)
                .contains(category)
                .contains("color")
                .as(String.format("Color '%s' should be displayed in page header", color_1))
                .contains(color_1)
                .as(String.format("Color '%s' should be displayed in page header", color_2))
                .contains(color_2);
        Allure.step(String.format("Assert if page header contains '%s', '%s' and '%s'", category, color_1, color_2));
        // assert header product counter
        soft.assertThat(actualValueOfProductCounter)
                .as("Value of product counter is different then expected")
                .isEqualTo(expectedNumberOfProducts);
        Allure.step("Assert if header product counter equals to number of products filtered from test data");
        // assert category filter items
        verifyCategoriesFilter(categoryPage, testDataProductsFiltered, soft, 1);
        // assert size filter items
        verifySizeFilter(categoryPage, testDataProductsFiltered, soft);
        // assert properties filter items
        verifyPropertiesFilter(categoryPage, testDataProductsFiltered, soft);
        // assert compositions filter items
        verifyCompositionsFilter(categoryPage, testDataProductsFiltered, soft);
        // assert styles filter items
        verifyStylesFilter(categoryPage, testDataProductsFiltered, soft);
        // assert availability filter items
        verifyInStockFilter(categoryPage, testDataProductsFiltered, soft);
        // assert condition filter items
        verifyConditionFilter(categoryPage, testDataProductsFiltered, soft);
        // assert price range filter settings
        verifyPriceRangeFilter(categoryPage, testDataProducts_Dresses, soft);
        // assert pagination
        verifyPagination(categoryPage,testDataProductsFiltered.size(), soft, numberOfProductsOnPage);
        soft.assertAll();
    }

    @Test
    @DisplayName("CP22 Filter the same product by two colors")
    public void CP22_filterTheSameProductByTwoColorsInDresses(){
        String category = "Dresses";
        String color_1 = "Black";
        String color_2 = "Blue";
        List<Integer> testDataProductsFiltered_Ids = testDataProducts
                .getProducts()
                .stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .filter(p -> p.getColor().equalsIgnoreCase(color_1) || p.getColor().equalsIgnoreCase(color_2))
                .map(Product::getId)
                .distinct()
                .collect(Collectors.toList());

        CategoryPage categoryPage = home
                .getTopMenu()
                .goToDressesPage()
                .selectColorFilterByName(color_1)
                .selectColorFilterByName(color_2);
        List<Integer> productsIds = categoryPage
                .getProducts()
                .stream()
                .map(ProductBasePage::getId)
                .collect(Collectors.toList());

        int actualValueOfProductCounter = categoryPage.getNumberOfProductsFromProductCounter();
        int expectedNumberOfProducts = testDataProductsFiltered_Ids.size();

        SoftAssertions soft = new SoftAssertions();
        // assert header product counter
        soft.assertThat(actualValueOfProductCounter)
                .as("Value of product counter is different then expected")
                .isEqualTo(expectedNumberOfProducts);
        Allure.step("Assert if header product counter equals to number of products filtered from test data");
        // assert if products displayed agree with test data products
        soft.assertThat(testDataProductsFiltered_Ids)
                .as("Displayed products are different then expected")
                .containsExactlyInAnyOrderElementsOf(productsIds);
        Allure.step("Assert if products are filtered as expected");
        soft.assertAll();
    }

    @Test
    @DisplayName("CP23 Disable color filter")
    public void CP23_disableColorFilter(){
        String category = "Dresses";
        String color_1 = "Black";
        String color_2 = "Blue";
        List<Integer> testDataProductsFiltered_Ids = testDataProducts
                .getProducts()
                .stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .distinct()
                .map(Product::getId)
                .collect(Collectors.toList());
        CategoryPage categoryPage = home
                .getTopMenu()
                .goToDressesPage()
                .selectColorFilterByName(color_1)
                .selectColorFilterByName(color_2);
        List<Integer> productsIds = categoryPage
                .disableFilterByValue(color_1)
                .disableFilterByValue(color_2)
                .getProducts()
                .stream()
                .map(ProductBasePage::getId)
                .collect(Collectors.toList());

        assertThat(categoryPage.getEnabledFilters())
                .as("Enabled filters should be empty").isEmpty();
        Allure.step("Assert if enabled filters section is empty");
        assertThat(testDataProductsFiltered_Ids)
                .as("Displayed products are different then expected")
                .containsExactlyInAnyOrderElementsOf(productsIds);
        Allure.step("Assert if displayed products are the same as originally before filter");
    }

    @Test
    @DisplayName("CP24 Filter products by single color and sort by product name: Z to A")
    public void CP24_filterProductsByColorAndSortByNameInWomen(){
        String category = "Women";
        String color = "Black";
        List<Product> testDataProductsSorted = testDataProducts
                .getProducts()
                .stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .filter(p -> p.getColor().equalsIgnoreCase(color))
                .sorted(Comparator.comparing(Product::getName))
                .collect(Collectors.toList());
        List<Integer> testDataProductsSorted_Ids = testDataProductsSorted
                .stream()
                .map(Product::getId)
                .distinct()
                .collect(Collectors.toList());

        CategoryPage categoryPage = home
                .getTopMenu()
                .goToWomenPage()
                .selectColorFilterByName(color)
                .sortProductsBy(SortProductsOptions.NAME_ASC);
        List<Integer> productsIds = categoryPage
                .getProducts()
                .stream()
                .map(ProductBasePage::getId)
                .collect(Collectors.toList());

        int actualValueOfProductCounter = categoryPage.getNumberOfProductsFromProductCounter();
        int expectedNumberOfProducts = testDataProductsSorted_Ids.size();

        // if displayed products aren't filtered and sorted correctly then there is no sense to verify other assertions
        assertThat(productsIds)
                .as("Displayed products are different then expected")
                .containsExactlyElementsOf(testDataProductsSorted_Ids);
        Allure.step("Assert if products are filtered and sorted as expected");

        SoftAssertions soft = new SoftAssertions();
        // assert if filter is listed in enabled filters
        soft.assertThat(categoryPage.getEnabledFilters().keySet())
                .as("Enabled filters don't contain items(s) with color")
                .contains(color);
        Allure.step(String.format("Assert if filter for color '%s' is displayed in enabled filters section", color));
        // assert header product counter
        soft.assertThat(actualValueOfProductCounter)
                .as("Value of product counter is different then expected")
                .isEqualTo(expectedNumberOfProducts);
        Allure.step("Assert if header product counter equals to number of products filtered from test data");
        // assert pagination
        verifyPagination(categoryPage,testDataProductsSorted_Ids.size(), soft, numberOfProductsOnPage);
        soft.assertAll();
    }

    @Test
    @DisplayName("CP25 Filter products by single property")
    public void CP25_filterProductsByPropertyInWomen(){
        String category = "Women";
        String property = "Short Sleeve";
        List<Product> testDataProducts_Women = testDataProducts
                .getProducts()
                .stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
        List<Product> testDataProductsFiltered = testDataProducts_Women
                .stream()
                .filter(p -> p.getProperties().equalsIgnoreCase(property))
                .collect(Collectors.toList());
        List<Integer> testDataProductsFiltered_Ids = testDataProductsFiltered
                .stream()
                .map(Product::getId)
                .distinct()
                .collect(Collectors.toList());

        CategoryPage categoryPage = home
                .getTopMenu()
                .goToWomenPage()
                .selectPropertyFilterByName(property);
        List<Integer> productsIds = categoryPage
                .getProducts()
                .stream()
                .map(ProductBasePage::getId)
                .collect(Collectors.toList());

        int actualValueOfProductCounter = categoryPage.getNumberOfProductsFromProductCounter();
        int expectedNumberOfProducts = testDataProductsFiltered_Ids.size();

        // if displayed products aren't filtered correctly then there is no sense to verify filters items
        assertThat(testDataProductsFiltered_Ids)
                .as("Displayed products are different then expected")
                .containsExactlyInAnyOrderElementsOf(productsIds);
        Allure.step("Assert if products are filtered as expected");

        SoftAssertions soft = new SoftAssertions();
        // assert if filter is listed in enabled filters
        soft.assertThat(categoryPage.getEnabledFilters().keySet())
                .as("Enabled filters don't contain items(s) with property")
                .contains(property);
        Allure.step(String.format("Assert if filter for property '%s' is displayed in enabled filters section", property));
        // assert if header has info about categories filter
        soft.assertThat(categoryPage.getPageHeader())
                .usingComparator(String::compareToIgnoreCase)
                .contains(category)
                .contains("properties")
                .contains(property);
        Allure.step(String.format("Assert if page header contains '%s' and '%s'", category, property));
        // assert header product counter
        soft.assertThat(actualValueOfProductCounter)
                .as("Value of product counter is different then expected")
                .isEqualTo(expectedNumberOfProducts);
        Allure.step("Assert if header product counter equals to number of products filtered from test data");
        // assert category filter items
        verifyCategoriesFilter(categoryPage, testDataProductsFiltered, soft, 1);
        // assert size filter items
        verifySizeFilter(categoryPage, testDataProductsFiltered, soft);
        // verify color filter items
        verifyColorFilter(categoryPage, testDataProductsFiltered, soft);
        // assert compositions filter items
        verifyCompositionsFilter(categoryPage, testDataProductsFiltered, soft);
        // assert styles filter items
        verifyStylesFilter(categoryPage, testDataProductsFiltered, soft);
        // assert availability filter items
        verifyInStockFilter(categoryPage, testDataProductsFiltered, soft);
        // assert condition filter items
        verifyConditionFilter(categoryPage, testDataProductsFiltered, soft);
        // assert price range filter settings
        verifyPriceRangeFilter(categoryPage, testDataProducts_Women, soft);
        // assert pagination
        verifyPagination(categoryPage,testDataProductsFiltered_Ids.size(), soft, numberOfProductsOnPage);
        soft.assertAll();
    }

    @Test
    @DisplayName("CP26 Filter products by two properties")
    public void CP26_filterProductsByTwoPropertiesInDresses() {
        String category = "Dresses";
        String property_1 = "Colorful Dress";
        String property_2 = "Maxi Dress";
        List<Product> testDataProducts_Dresses = testDataProducts
                .getProducts()
                .stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
        List<Product> testDataProductsFiltered = testDataProducts_Dresses
                .stream()
                .filter(p -> p.getProperties().equalsIgnoreCase(property_1) ||
                        p.getProperties().equalsIgnoreCase(property_2))
                .collect(Collectors.toList());
        List<Integer> testDataProductsFiltered_Ids = testDataProductsFiltered
                .stream()
                .map(Product::getId)
                .distinct()
                .collect(Collectors.toList());

        CategoryPage categoryPage = home
                .getTopMenu()
                .goToDressesPage()
                .selectPropertyFilterByName(property_1)
                .selectPropertyFilterByName(property_2);

        List<Integer> productsIds = categoryPage
                .getProducts()
                .stream()
                .map(ProductBasePage::getId)
                .collect(Collectors.toList());

        int actualValueOfProductCounter = categoryPage.getNumberOfProductsFromProductCounter();
        int expectedNumberOfProducts = testDataProductsFiltered_Ids.size();

        // if displayed products aren't filtered correctly then there is no sense to verify filters items
        assertThat(testDataProductsFiltered_Ids)
                .as("Displayed products are different then expected")
                .containsExactlyInAnyOrderElementsOf(productsIds);
        Allure.step("Assert if products are filtered as expected");

        SoftAssertions soft = new SoftAssertions();
        // assert if filter is listed in enabled filters
        soft.assertThat(categoryPage.getEnabledFilters().keySet())
                .as("Enabled filters don't contain items(s) with property")
                .contains(property_1)
                .contains(property_2);
        Allure.step(String.format("Assert if filter for properties '%s' and " +
                "%s' are displayed in enabled filters section", property_1, property_2));
        // assert if header has info about categories filter
        soft.assertThat(categoryPage.getPageHeader())
                .usingComparator(String::compareToIgnoreCase)
                .contains(category)
                .contains("properties")
                .contains(property_1)
                .contains(property_2);
        Allure.step(String.format("Assert if page header contains '%s', '%s' and '%s'", category, property_1, property_2));
        // assert header product counter
        soft.assertThat(actualValueOfProductCounter)
                .as("Value of product counter is different then expected")
                .isEqualTo(expectedNumberOfProducts);
        Allure.step("Assert if header product counter equals to number of products filtered from test data");
        // assert category filter items
        verifyCategoriesFilter(categoryPage, testDataProductsFiltered, soft, 1);
        // assert size filter items
        verifySizeFilter(categoryPage, testDataProductsFiltered, soft);
        // verify color filter items
        verifyColorFilter(categoryPage, testDataProductsFiltered, soft);
        // assert compositions filter items
        verifyCompositionsFilter(categoryPage, testDataProductsFiltered, soft);
        // assert styles filter items
        verifyStylesFilter(categoryPage, testDataProductsFiltered, soft);
        // assert availability filter items
        verifyInStockFilter(categoryPage, testDataProductsFiltered, soft);
        // assert condition filter items
        verifyConditionFilter(categoryPage, testDataProductsFiltered, soft);
        // assert price range filter settings
        verifyPriceRangeFilter(categoryPage, testDataProducts_Dresses, soft);
        // assert pagination
        verifyPagination(categoryPage,testDataProductsFiltered_Ids.size(), soft, numberOfProductsOnPage);
        soft.assertAll();
    }

    @Test
    @DisplayName("CP27 Disable properties filter")
    public void CP27_disablePropertyFilter(){
        String category = "Dresses";
        String property_1 = "Colorful Dress";
        String property_2 = "Maxi Dress";
        List<Integer> testDataProductsFiltered_Ids = testDataProducts
                .getProducts()
                .stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .distinct()
                .map(Product::getId)
                .collect(Collectors.toList());
        CategoryPage categoryPage = home
                .getTopMenu()
                .goToDressesPage()
                .selectPropertyFilterByName(property_1)
                .selectPropertyFilterByName(property_2);
        List<Integer> productsIds = categoryPage
                .disableFilterByValue(property_1)
                .disableFilterByValue(property_2)
                .getProducts()
                .stream()
                .map(ProductBasePage::getId)
                .collect(Collectors.toList());
        assertThat(categoryPage.getEnabledFilters())
                .as("Enabled filters should be empty").isEmpty();
        Allure.step("Assert if enabled filters section is empty");
        assertThat(testDataProductsFiltered_Ids)
                .as("Displayed products are different then expected")
                .containsExactlyInAnyOrderElementsOf(productsIds);
        Allure.step("Assert if displayed products are the same as originally before filter");
    }

    @Test
    @DisplayName("CP28 Filter products by single property and sort by reference: lowest first")
    public void CP28_filterProductsByPropertyAndSortByReferenceInWomen(){
        String category = "Women";
        String property = "Short Sleeve";
        List<Integer> testDataProductsSorted_Ids = testDataProducts
                .getProducts()
                .stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .filter(p -> p.getProperties().equalsIgnoreCase(property))
                .sorted(Comparator.comparing(Product::getReference))
                .map(Product::getId)
                .distinct()
                .collect(Collectors.toList());

        CategoryPage categoryPage = home
                .getTopMenu()
                .goToWomenPage()
                .selectPropertyFilterByName(property)
                .sortProductsBy(SortProductsOptions.REFERENCE_ASC);
        List<Integer> productsIds = categoryPage
                .getProducts()
                .stream()
                .map(ProductBasePage::getId)
                .collect(Collectors.toList());

        int actualValueOfProductCounter = categoryPage.getNumberOfProductsFromProductCounter();
        int expectedNumberOfProducts = testDataProductsSorted_Ids.size();

        // if displayed products aren't filtered and sorted correctly then there is no sense to verify other assertions
        assertThat(productsIds)
                .as("Displayed products are different then expected")
                .containsExactlyElementsOf(testDataProductsSorted_Ids);
        Allure.step("Assert if products are filtered and sorted as expected");

        SoftAssertions soft = new SoftAssertions();
        // assert if filter is listed in enabled filters
        soft.assertThat(categoryPage.getEnabledFilters().keySet())
                .as("Enabled filters don't contain items(s) with property")
                .contains(property);
        Allure.step(String.format("Assert if filter for property '%s' is displayed in enabled filters section", property));
        // assert header product counter
        soft.assertThat(actualValueOfProductCounter)
                .as("Value of product counter is different then expected")
                .isEqualTo(expectedNumberOfProducts);
        Allure.step("Assert if header product counter equals to number of products filtered from test data");
        // assert pagination
        verifyPagination(categoryPage,testDataProductsSorted_Ids.size(), soft, numberOfProductsOnPage);
        soft.assertAll();
    }

    @Test
    @DisplayName("CP29 Filter products by single composition")
    public void CP29_filterProductsBySingleCompositionInWomen(){
        String category = "Women";
        String composition = "Cotton";
        List<Product> testDataProducts_Women = testDataProducts
                .getProducts()
                .stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
        List<Product> testDataProductsFiltered = testDataProducts_Women
                .stream()
                .filter(p -> p.getCompositions().equalsIgnoreCase(composition))
                .collect(Collectors.toList());
        List<Integer> testDataProductsFiltered_Ids = testDataProductsFiltered
                .stream()
                .map(Product::getId)
                .distinct()
                .collect(Collectors.toList());

        CategoryPage categoryPage = home
                .getTopMenu()
                .goToWomenPage()
                .selectCompositionsFilterByName(composition);
        List<Integer> productsIds = categoryPage
                .getProducts()
                .stream()
                .map(ProductBasePage::getId)
                .collect(Collectors.toList());

        int actualValueOfProductCounter = categoryPage.getNumberOfProductsFromProductCounter();
        int expectedNumberOfProducts = testDataProductsFiltered_Ids.size();

        // if displayed products aren't filtered correctly then there is no sense to verify filters items
        assertThat(testDataProductsFiltered_Ids)
                .as("Displayed products are different then expected")
                .containsExactlyInAnyOrderElementsOf(productsIds);
        Allure.step("Assert if products are filtered as expected");

        SoftAssertions soft = new SoftAssertions();
        // assert if filter is listed in enabled filters
        soft.assertThat(categoryPage.getEnabledFilters().keySet())
                .as("Enabled filters don't contain items(s) with composition")
                .contains(composition);
        Allure.step(String.format("Assert if filter for composition '%s' is displayed in enabled filters section", composition));
        // assert if header has info about categories filter
        soft.assertThat(categoryPage.getPageHeader())
                .usingComparator(String::compareToIgnoreCase)
                .contains(category)
                .contains("compositions")
                .contains(composition);
        Allure.step(String.format("Assert if page header contains '%s' and '%s'", category, composition));
        // assert header product counter
        soft.assertThat(actualValueOfProductCounter)
                .as("Value of product counter is different then expected")
                .isEqualTo(expectedNumberOfProducts);
        Allure.step("Assert if header product counter equals to number of products filtered from test data");
        // assert category filter items
        verifyCategoriesFilter(categoryPage, testDataProductsFiltered, soft, 1);
        // assert size filter items
        verifySizeFilter(categoryPage, testDataProductsFiltered, soft);
        // verify color filter items
        verifyColorFilter(categoryPage, testDataProductsFiltered, soft);
        // assert properties filter items
        verifyPropertiesFilter(categoryPage, testDataProductsFiltered, soft);
        // assert styles filter items
        verifyStylesFilter(categoryPage, testDataProductsFiltered, soft);
        // assert availability filter items
        verifyInStockFilter(categoryPage, testDataProductsFiltered, soft);
        // assert condition filter items
        verifyConditionFilter(categoryPage, testDataProductsFiltered, soft);
        // assert price range filter settings
        verifyPriceRangeFilter(categoryPage, testDataProducts_Women, soft);
        // assert pagination
        verifyPagination(categoryPage,testDataProductsFiltered_Ids.size(), soft, numberOfProductsOnPage);
        soft.assertAll();
    }

    @Test
    @DisplayName("CP30 Filter products by two compositions")
    public void CP30_filterProductsByTwoCompositionsInWomen(){
        String category = "Women";
        String composition_1 = "Cotton";
        String composition_2 = "Viscose";
        List<Product> testDataProducts_Women = testDataProducts
                .getProducts()
                .stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
        List<Product> testDataProductsFiltered = testDataProducts_Women
                .stream()
                .filter(p -> p.getCompositions().equalsIgnoreCase(composition_1) ||
                         p.getCompositions().equalsIgnoreCase(composition_2))
                .collect(Collectors.toList());
        List<Integer> testDataProductsFiltered_Ids = testDataProductsFiltered
                .stream()
                .map(Product::getId)
                .distinct()
                .collect(Collectors.toList());

        CategoryPage categoryPage = home
                .getTopMenu()
                .goToWomenPage()
                .selectCompositionsFilterByName(composition_1)
                .selectCompositionsFilterByName(composition_2);
        List<Integer> productsIds = categoryPage
                .getProducts()
                .stream()
                .map(ProductBasePage::getId)
                .collect(Collectors.toList());

        int actualValueOfProductCounter = categoryPage.getNumberOfProductsFromProductCounter();
        int expectedNumberOfProducts = testDataProductsFiltered_Ids.size();

        // if displayed products aren't filtered correctly then there is no sense to verify filters items
        assertThat(testDataProductsFiltered_Ids)
                .as("Displayed products are different then expected")
                .containsExactlyInAnyOrderElementsOf(productsIds);
        Allure.step("Assert if products are filtered as expected");

        SoftAssertions soft = new SoftAssertions();
        // assert if filter is listed in enabled filters
        soft.assertThat(categoryPage.getEnabledFilters().keySet())
                .as("Enabled filters don't contain items(s) with composition")
                .contains(composition_1)
                .contains(composition_2);
        Allure.step(String.format("Assert if filter for compositions '%s' and '%s' are displayed in enabled filters section",
                composition_1, composition_2));
        // assert if header has info about categories filter
        soft.assertThat(categoryPage.getPageHeader())
                .usingComparator(String::compareToIgnoreCase)
                .contains(category)
                .contains("compositions")
                .contains(composition_1)
                .contains(composition_2);
        Allure.step(String.format("Assert if page header contains '%s', '%s', '%s'", category, composition_1, composition_2));
        // assert header product counter
        soft.assertThat(actualValueOfProductCounter)
                .as("Value of product counter is different then expected")
                .isEqualTo(expectedNumberOfProducts);
        Allure.step("Assert if header product counter equals to number of products filtered from test data");
        // assert category filter items
        verifyCategoriesFilter(categoryPage, testDataProductsFiltered, soft, 1);
        // assert size filter items
        verifySizeFilter(categoryPage, testDataProductsFiltered, soft);
        // verify color filter items
        verifyColorFilter(categoryPage, testDataProductsFiltered, soft);
        // assert properties filter items
        verifyPropertiesFilter(categoryPage, testDataProductsFiltered, soft);
        // assert styles filter items
        verifyStylesFilter(categoryPage, testDataProductsFiltered, soft);
        // assert availability filter items
        verifyInStockFilter(categoryPage, testDataProductsFiltered, soft);
        // assert condition filter items
        verifyConditionFilter(categoryPage, testDataProductsFiltered, soft);
        // assert price range filter settings
        verifyPriceRangeFilter(categoryPage, testDataProducts_Women, soft);
        // assert pagination
        verifyPagination(categoryPage,testDataProductsFiltered.size(), soft, numberOfProductsOnPage);
        soft.assertAll();
    }

    @Test
    @DisplayName("CP31 Disable compositions filter")
    public void CP31_disableCompositionsFilter(){
        String category = "Women";
        String composition_1 = "Cotton";
        String composition_2 = "Viscose";
        List<Integer> testDataProductsFiltered_Ids = testDataProducts
                .getProducts()
                .stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .distinct()
                .map(Product::getId)
                .collect(Collectors.toList());
        CategoryPage categoryPage = home
                .getTopMenu()
                .goToWomenPage()
                .selectCompositionsFilterByName(composition_1)
                .selectCompositionsFilterByName(composition_2);
        List<Integer> productsIds = categoryPage
                .disableFilterByValue(composition_1)
                .disableFilterByValue(composition_2)
                .getProducts()
                .stream()
                .map(ProductBasePage::getId)
                .collect(Collectors.toList());
        assertThat(categoryPage.getEnabledFilters())
                .as("Enabled filters should be empty").isEmpty();
        Allure.step("Assert if enabled filters section is empty");
        assertThat(testDataProductsFiltered_Ids)
                .as("Displayed products are different then expected")
                .containsExactlyInAnyOrderElementsOf(productsIds);
        Allure.step("Assert if displayed products are the same as originally before filter");
    }

    @Test
    @DisplayName("CP32 Filter products by single composition and sort by reference: highest first")
    public void CP32_filterProductsByCompositionAndSortByReferenceDescInWomen(){
        String category = "Women";
        String composition = "Cotton";
        List<Integer> testDataProductsSorted_Ids = testDataProducts
                .getProducts()
                .stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .filter(p -> p.getCompositions().equalsIgnoreCase(composition))
                .sorted(Comparator.comparing(Product::getReference).reversed())
                .map(Product::getId)
                .distinct()
                .collect(Collectors.toList());

        CategoryPage categoryPage = home
                .getTopMenu()
                .goToWomenPage()
                .selectCompositionsFilterByName(composition)
                .sortProductsBy(SortProductsOptions.REFERENCE_DESC);
        List<Integer> productsIds = categoryPage
                .getProducts()
                .stream()
                .map(ProductBasePage::getId)
                .collect(Collectors.toList());

        int actualValueOfProductCounter = categoryPage.getNumberOfProductsFromProductCounter();
        int expectedNumberOfProducts = testDataProductsSorted_Ids.size();

        // if displayed products aren't filtered and sorted correctly then there is no sense to verify other assertions
        assertThat(productsIds)
                .as("Displayed products are different then expected")
                .containsExactlyElementsOf(testDataProductsSorted_Ids);
        Allure.step("Assert if products are filtered and sorted as expected");

        SoftAssertions soft =  new SoftAssertions();
        // assert if filter is listed in enabled filters
        soft.assertThat(categoryPage.getEnabledFilters().keySet())
                .as("Enabled filters don't contain items(s) with composition")
                .contains(composition);
        Allure.step(String.format("Assert if filter for composition '%s' is displayed in enabled filters section",
                composition));
        Allure.step("Assert if products are filtered and sorted as expected");
        // assert header product counter
        soft.assertThat(actualValueOfProductCounter)
                .as("Value of product counter is different then expected")
                .isEqualTo(expectedNumberOfProducts);
        Allure.step("Assert if header product counter equals to number of products filtered from test data");
        // assert pagination
        verifyPagination(categoryPage,testDataProductsSorted_Ids.size(), soft, numberOfProductsOnPage);
        soft.assertAll();
    }

    @Test
    @DisplayName("CP33 Filter products by single style")
    public void CP33_filterProductsByStyleInWomen(){
        String category = "Women";
        String style = "Casual";
        List<Product> testDataProducts_Women = testDataProducts
                .getProducts()
                .stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
        List<Product> testDataProductsFiltered = testDataProducts_Women
                .stream()
                .filter(p -> p.getStyles().equalsIgnoreCase(style))
                .collect(Collectors.toList());
        List<Integer> testDataProductsFiltered_Ids = testDataProductsFiltered
                .stream()
                .map(Product::getId)
                .distinct()
                .collect(Collectors.toList());

        CategoryPage categoryPage = home
                .getTopMenu()
                .goToWomenPage()
                .selectStylesFilterByName(style);
        List<Integer> productsIds = categoryPage
                .getProducts()
                .stream()
                .map(ProductBasePage::getId)
                .collect(Collectors.toList());

        int actualValueOfProductCounter = categoryPage.getNumberOfProductsFromProductCounter();
        int expectedNumberOfProducts = testDataProductsFiltered_Ids.size();

        // if displayed products aren't filtered correctly then there is no sense to verify filters items
        assertThat(testDataProductsFiltered_Ids)
                .as("Displayed products are different then expected")
                .containsExactlyInAnyOrderElementsOf(productsIds);
        Allure.step("Assert if products are filtered as expected");

        SoftAssertions soft = new SoftAssertions();
        // assert if filter is listed in enabled filters
        soft.assertThat(categoryPage.getEnabledFilters().keySet())
                .as("Enabled filters don't contain items(s) with style")
                .contains(style);
        Allure.step(String.format("Assert if filter for styles '%s' is displayed in enabled filters section", style));
        // assert if header has info about categories filter
        soft.assertThat(categoryPage.getPageHeader())
                .usingComparator(String::compareToIgnoreCase)
                .contains(category)
                .contains("styles")
                .contains(style);
        Allure.step(String.format("Assert if page header contains '%s' and '%s'", category, style));
        // assert header product counter
        soft.assertThat(actualValueOfProductCounter)
                .as("Value of product counter is different then expected")
                .isEqualTo(expectedNumberOfProducts);
        Allure.step("Assert if header product counter equals to number of products filtered from test data");
        // assert category filter items
        verifyCategoriesFilter(categoryPage, testDataProductsFiltered, soft, 1);
        // assert size filter items
        verifySizeFilter(categoryPage, testDataProductsFiltered, soft);
        // verify color filter items
        verifyColorFilter(categoryPage, testDataProductsFiltered, soft);
        // assert properties filter items
        verifyPropertiesFilter(categoryPage, testDataProductsFiltered, soft);
        // assert compositions filter items
        verifyCompositionsFilter(categoryPage, testDataProductsFiltered, soft);
        // assert availability filter items
        verifyInStockFilter(categoryPage, testDataProductsFiltered, soft);
        // assert condition filter items
        verifyConditionFilter(categoryPage, testDataProductsFiltered, soft);
        // assert price range filter settings
        verifyPriceRangeFilter(categoryPage, testDataProducts_Women, soft);
        // assert pagination
        verifyPagination(categoryPage,testDataProductsFiltered_Ids.size(), soft, numberOfProductsOnPage);
        soft.assertAll();
    }

    @Test
    @DisplayName("CP34 Filter products by two styles")
    public void CP34_filterProductsByTwoStylesInWomen(){
        String category = "Women";
        String style_1 = "Casual";
        String style_2 = "Dressy";
        List<Product> testDataProducts_Women = testDataProducts
                .getProducts()
                .stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
        List<Product> testDataProductsFiltered = testDataProducts_Women
                .stream()
                .filter(p -> p.getStyles().equalsIgnoreCase(style_1) ||
                        p.getStyles().equalsIgnoreCase(style_2))
                .collect(Collectors.toList());
        List<Integer> testDataProductsFiltered_Ids = testDataProductsFiltered
                .stream()
                .map(Product::getId)
                .distinct()
                .collect(Collectors.toList());

        CategoryPage categoryPage = home
                .getTopMenu()
                .goToWomenPage()
                .selectStylesFilterByName(style_1)
                .selectStylesFilterByName(style_2);
        List<Integer> productsIds = categoryPage
                .getProducts()
                .stream()
                .map(ProductBasePage::getId)
                .collect(Collectors.toList());

        int actualValueOfProductCounter = categoryPage.getNumberOfProductsFromProductCounter();
        int expectedNumberOfProducts = testDataProductsFiltered_Ids.size();

        // if displayed products aren't filtered correctly then there is no sense to verify filters items
        assertThat(testDataProductsFiltered_Ids)
                .as("Displayed products are different then expected")
                .containsExactlyInAnyOrderElementsOf(productsIds);
        Allure.step("Assert if products are filtered as expected");

        SoftAssertions soft = new SoftAssertions();
        // assert if filter is listed in enabled filters
        soft.assertThat(categoryPage.getEnabledFilters().keySet())
                .as("Enabled filters don't contain items(s) with style")
                .contains(style_1)
                .contains(style_2);
        Allure.step(String.format("Assert if filter for styles '%s' and '%s' are displayed in enabled filters section",
                style_1, style_2));
        // assert if header has info about categories filter
        soft.assertThat(categoryPage.getPageHeader())
                .usingComparator(String::compareToIgnoreCase)
                .contains(category)
                .contains("styles")
                .contains(style_1)
                .contains(style_2);
        Allure.step(String.format("Assert if page header contains '%s', '%s' and '%s'", category, style_1, style_2));
        // assert header product counter
        soft.assertThat(actualValueOfProductCounter)
                .as("Value of product counter is different then expected")
                .isEqualTo(expectedNumberOfProducts);
        Allure.step("Assert if header product counter equals to number of products filtered from test data");
        // assert category filter items
        verifyCategoriesFilter(categoryPage, testDataProductsFiltered, soft, 1);
        // assert size filter items
        verifySizeFilter(categoryPage, testDataProductsFiltered, soft);
        // verify color filter items
        verifyColorFilter(categoryPage, testDataProductsFiltered, soft);
        // assert properties filter items
        verifyPropertiesFilter(categoryPage, testDataProductsFiltered, soft);
        // assert compositions filter items
        verifyCompositionsFilter(categoryPage, testDataProductsFiltered, soft);
        // assert availability filter items
        verifyInStockFilter(categoryPage, testDataProductsFiltered, soft);
        // assert condition filter items
        verifyConditionFilter(categoryPage, testDataProductsFiltered, soft);
        // assert price range filter settings
        verifyPriceRangeFilter(categoryPage, testDataProducts_Women, soft);
        // assert pagination
        verifyPagination(categoryPage,testDataProductsFiltered.size(), soft, numberOfProductsOnPage);
        soft.assertAll();
    }

    @Test
    @DisplayName("CP35 Disable styles filter")
    public void CP35_disableStylesFilter(){
        String category = "Women";
        String style_1 = "Casual";
        String style_2 = "Dressy";
        List<Integer> testDataProductsFiltered_Ids = testDataProducts
                .getProducts()
                .stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .distinct()
                .map(Product::getId)
                .collect(Collectors.toList());
        CategoryPage categoryPage = home
                .getTopMenu()
                .goToWomenPage()
                .selectStylesFilterByName(style_1)
                .selectStylesFilterByName(style_2);
        List<Integer> productsIds = categoryPage
                .disableFilterByValue(style_1)
                .disableFilterByValue(style_2)
                .getProducts()
                .stream()
                .map(ProductBasePage::getId)
                .collect(Collectors.toList());
        assertThat(categoryPage.getEnabledFilters())
                .as("Enabled filters should be empty").isEmpty();
        Allure.step("Assert if enabled filters section is empty");
        assertThat(testDataProductsFiltered_Ids)
                .as("Displayed products are different then expected")
                .containsExactlyInAnyOrderElementsOf(productsIds);
        Allure.step("Assert if displayed products are the same as originally before filter");
    }

    @Test
    @DisplayName("CP36 Filter products by single style and sort by reference: lowest first")
    public void CP36_filterProductsByStyleAndSortByReferenceDesc(){
        String category = "Women";
        String style = "Casual";
        List<Integer> testDataProductsSorted_Ids = testDataProducts
                .getProducts()
                .stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .filter(p -> p.getStyles().equalsIgnoreCase(style))
                .sorted(Comparator.comparing(Product::getReference))
                .map(Product::getId)
                .distinct()
                .collect(Collectors.toList());

        CategoryPage categoryPage = home
                .getTopMenu()
                .goToWomenPage()
                .selectStylesFilterByName(style)
                .sortProductsBy(SortProductsOptions.REFERENCE_ASC);
        List<Integer> productsIds = categoryPage
                .getProducts()
                .stream()
                .map(ProductBasePage::getId)
                .collect(Collectors.toList());

        int actualValueOfProductCounter = categoryPage.getNumberOfProductsFromProductCounter();
        int expectedNumberOfProducts = testDataProductsSorted_Ids.size();

        // if displayed products aren't filtered and sorted correctly then there is no sense to verify other assertions
        assertThat(productsIds)
                .as("Displayed products are different then expected")
                .containsExactlyElementsOf(testDataProductsSorted_Ids);
        Allure.step("Assert if products are filtered and sorted as expected");

        SoftAssertions soft = new SoftAssertions();
        // assert if filter is listed in enabled filters
        soft.assertThat(categoryPage.getEnabledFilters().keySet())
                .as("Enabled filters don't contain items(s) with style")
                .contains(style);
        Allure.step(String.format("Assert if filter for style '%s' is displayed in enabled filters section", style));
        // assert header product counter
        soft.assertThat(actualValueOfProductCounter)
                .as("Value of product counter is different then expected")
                .isEqualTo(expectedNumberOfProducts);
        Allure.step("Assert if products are filtered and sorted are as expected");
        // assert pagination
        verifyPagination(categoryPage,testDataProductsSorted_Ids.size(), soft, numberOfProductsOnPage);
    }

    @Test
    @DisplayName("CP37 Filter products by price")
    public void CP37_filterProductsByPrice() {
        String category = "Women";
        List<Product> testDataProductsSorted = testDataProducts
                .getProducts()
                .stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .sorted(Comparator.comparing(Product::getPrice))
                .collect(Collectors.toList());

        int size = testDataProductsSorted.size();
        BigDecimal smallestPrice = testDataProductsSorted.get(0).getPrice();
        BigDecimal highestPrice = testDataProductsSorted.get(size-1).getPrice();

        CategoryPage categoryPage = home
                .getTopMenu()
                .goToWomenPage()
                .setLowPriceOnFilterSlider(smallestPrice)
                .setHighPriceOnFilterSlider(highestPrice);

        BigDecimal smallestPriceFormFilter = categoryPage.getLowPriceRangeFromLabel();
        BigDecimal highestPriceFormFilter = categoryPage.getHighPriceRangeFromLabel();

        Iterator<Product> i = testDataProductsSorted.iterator();
        while(i.hasNext()){
            Product product = i.next();
            if(product.getPrice().compareTo(smallestPriceFormFilter)<0 ||
                    product.getPrice().compareTo(highestPriceFormFilter)>0){
                i.remove();
            }
        }

        List<Integer> testDataProductsSorted_Ids = testDataProductsSorted
                .stream()
                .map(Product::getId)
                .distinct()
                .collect(Collectors.toList());

        List<Integer> productsIds = categoryPage
                .getProducts()
                .stream()
                .map(ProductBasePage::getId)
                .collect(Collectors.toList());

        int actualValueOfProductCounter = categoryPage.getNumberOfProductsFromProductCounter();
        int expectedNumberOfProducts = testDataProductsSorted_Ids.size();

        // if displayed products aren't filtered correctly then there is no sense to verify filters items
        assertThat(testDataProductsSorted_Ids)
                .as("Displayed products are different then expected")
                .containsExactlyInAnyOrderElementsOf(productsIds);
        Allure.step("Assert if products are filtered as expected");

        SoftAssertions soft = new SoftAssertions();
        // assert if filter is listed in enabled filters
        soft.assertThat(categoryPage.getEnabledFilters().keySet())
                .as("Enabled filters don't contain items(s) with price")
                .contains(categoryPage.getPriceRangeFromLabel());
        Allure.step(String.format("Assert if filter for price '%s' is displayed in enabled filters section",
                categoryPage.getPriceRangeFromLabel()));
        // assert header product counter
        soft.assertThat(actualValueOfProductCounter)
                .as("Value of product counter is different then expected")
                .isEqualTo(expectedNumberOfProducts);
        Allure.step("Assert if header product counter equals to number of products filtered from test data");
        // assert category filter items
        verifyCategoriesFilter(categoryPage, testDataProductsSorted, soft, 1);
        // assert size filter items
        verifySizeFilter(categoryPage, testDataProductsSorted, soft);
        // verify color filter items
        verifyColorFilter(categoryPage, testDataProductsSorted, soft);
        // assert properties filter items
        verifyPropertiesFilter(categoryPage, testDataProductsSorted, soft);
        // assert compositions filter items
        verifyCompositionsFilter(categoryPage, testDataProductsSorted, soft);
        //assert styles filter items
        verifyStylesFilter(categoryPage, testDataProductsSorted, soft);
        // assert availability filter items
        verifyInStockFilter(categoryPage, testDataProductsSorted, soft);
        // assert condition filter items
        verifyConditionFilter(categoryPage, testDataProductsSorted, soft);
        // assert pagination
        verifyPagination(categoryPage,testDataProductsSorted.size(), soft, numberOfProductsOnPage);
        soft.assertAll();
    }

    @Test
    @DisplayName("CP38 No products displayed when price filter set to highest possible")
    public void CP38_noProductsDisplayedWhenPriceFilterSetToHighestValue(){
        CategoryPage categoryPage = home
                .getTopMenu()
                .goToWomenPage();
        BigDecimal highPriceRangeFromLabel = categoryPage.getHighPriceRangeFromLabel();
        categoryPage.setLowPriceOnFilterSlider(highPriceRangeFromLabel);

        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(categoryPage.isNoProductsAlertDisplayed())
                .as("No products alert is not displayed")
                .isTrue();
        Allure.step("Assert if no products alert is displayed");
        soft.assertThat(categoryPage.getNumberOfProductsFromProductCounter())
                .as("Products counter shows incorrect info. There shouldn't be any products.")
                .isEqualTo(0);
        Allure.step("Assert if product counter shows info that there are no products");
        soft.assertThat(categoryPage.isPaginationDisplayed())
                .as("Pagination is displayed. It shouldn't be displayed if no products available")
                .isFalse();
        Allure.step("Assert if pagination is not displayed");
        soft.assertAll();
    }

    @Test
    @DisplayName("CP39 No products displayed when price filter set to lowest possible")
    public void CP39_noProductsDisplayedWhenPriceFilterSetToLowestValue(){
        CategoryPage categoryPage = home
                .getTopMenu()
                .goToWomenPage();
        BigDecimal lowPriceRangeFromLabel = categoryPage.getLowPriceRangeFromLabel();
        categoryPage.setHighPriceOnFilterSlider(lowPriceRangeFromLabel);

        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(categoryPage.isNoProductsAlertDisplayed())
                .as("No products alert is not displayed")
                .isTrue();
        Allure.step("Assert if no products alert is displayed");
        soft.assertThat(categoryPage.getNumberOfProductsFromProductCounter())
                .as("Products counter shows incorrect info. There shouldn't be any products.")
                .isEqualTo(0);
        Allure.step("Assert if product counter shows info that there are no products");
        soft.assertThat(categoryPage.isPaginationDisplayed())
                .as("Pagination is displayed. It shouldn't be displayed if no products available")
                .isFalse();
        Allure.step("Assert if pagination is not displayed");
        soft.assertAll();
    }

    @Test
    @DisplayName("CP40 Filter products by max price and lower the price filter")
    public void CP40_filterProductsByMaxPriceAndLowerPriceFilter(){
        CategoryPage categoryPage = home
                .getTopMenu()
                .goToWomenPage();
        BigDecimal highPriceRangeFromLabel = categoryPage.getHighPriceRangeFromLabel();
        BigDecimal lowPriceRangeFromLabel = categoryPage.getLowPriceRangeFromLabel();
        categoryPage
                .setLowPriceOnFilterSlider(highPriceRangeFromLabel)
                .isNoProductsAlertDisplayed();
        categoryPage
                .setLowPriceOnFilterSlider(lowPriceRangeFromLabel);
        assertThat(categoryPage.isNoProductsAlertRemoved())
                .as("No product alert is still displayed when it shouldn't")
                .isTrue();
        Allure.step("Assert if no product alert disappeared");
    }

    @Test
    @DisplayName("CP41 Filter products by min price and rise the price filter")
    public void CP41_filterProductsByMinPriceAndRisePriceFilter(){
        CategoryPage categoryPage = home
                .getTopMenu()
                .goToWomenPage();
        BigDecimal highPriceRangeFromLabel = categoryPage.getHighPriceRangeFromLabel();
        BigDecimal lowPriceRangeFromLabel = categoryPage.getLowPriceRangeFromLabel();
        categoryPage
                .setHighPriceOnFilterSlider(lowPriceRangeFromLabel)
                .isNoProductsAlertDisplayed();
        categoryPage
                .setHighPriceOnFilterSlider(highPriceRangeFromLabel);
        assertThat(categoryPage.isNoProductsAlertRemoved())
                .as("No product alert is still displayed when it shouldn't")
                .isTrue();
    }

    @Test
    @DisplayName("CP42 Filter products by price and sort by price highest first")
    public void CP42_filterProductsByPriceAndSortByPrice(){
        String category = "Women";
        List<Product> testDataProductsSorted = testDataProducts
                .getProducts()
                .stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .sorted(Comparator.comparing(Product::getPrice))
                .collect(Collectors.toList());

        int size = testDataProductsSorted.size();
        BigDecimal smallestPrice = testDataProductsSorted.get(0).getPrice();
        BigDecimal highestPrice = testDataProductsSorted.get(size-1).getPrice();

        CategoryPage categoryPage = home
                .getTopMenu()
                .goToWomenPage()
                .setLowPriceOnFilterSlider(smallestPrice)
                .setHighPriceOnFilterSlider(highestPrice)
                .sortProductsBy(SortProductsOptions.PRICE_DESC);

        BigDecimal smallestPriceFromFilter = categoryPage.getLowPriceRangeFromLabel();
        BigDecimal highestPriceFromFilter = categoryPage.getHighPriceRangeFromLabel();

        Iterator<Product> i = testDataProductsSorted.iterator();
        while(i.hasNext()){
            Product product = i.next();
            if(product.getPrice().compareTo(smallestPriceFromFilter)<=0 ||
                    product.getPrice().compareTo(highestPriceFromFilter)>=0){
                i.remove();
            }
        }

        List<Integer> testDataProductsSorted_Ids = testDataProductsSorted
                .stream()
                .map(Product::getId)
                .distinct()
                .collect(Collectors.toList());

        List<Integer> productsIds = categoryPage
                .getProducts()
                .stream()
                .map(ProductBasePage::getId)
                .collect(Collectors.toList());

        int actualValueOfProductCounter = categoryPage.getNumberOfProductsFromProductCounter();
        int expectedNumberOfProducts = testDataProductsSorted_Ids.size();

        // if displayed products aren't filtered and sorted correctly then there is no sense to verify other assertions
        assertThat(testDataProductsSorted_Ids)
                .as("Displayed products are different then expected")
                .containsExactlyElementsOf(productsIds);

        SoftAssertions soft = new SoftAssertions();
        // assert if filter is listed in enabled filters
        soft.assertThat(categoryPage.getEnabledFilters().keySet())
                .as("Enabled filters don't contain items(s) with price")
                .contains(categoryPage.getPriceRangeFromLabel());
        // assert header product counter
        soft.assertThat(actualValueOfProductCounter)
                .as("Value of product counter is different then expected")
                .isEqualTo(expectedNumberOfProducts);
        // assert pagination
        verifyPagination(categoryPage,testDataProductsSorted_Ids.size(), soft, numberOfProductsOnPage);
    }

    @Test
    @DisplayName("CP43 Filter products by given category and two colors")
    public void CP43_filterProductsByGivenCategoryAndTwoColors(){
        String category = "Women";
        String subcategory = "Dresses";
        String color_1 = "Blue";
        String color_2 = "White";

        List<Product> testDataProducts_Women = testDataProducts
                .getProducts()
                .stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());

        List<Product> testDataProductsFiltered = testDataProducts_Women
                .stream()
                .filter(p->p.getSubcategory1stLevel().equalsIgnoreCase(subcategory))
                .filter(p -> p.getColor().equalsIgnoreCase(color_1) ||
                        p.getColor().equalsIgnoreCase(color_2))
                .collect(Collectors.toList());

        List<Integer> testDataProductsFiltered_Ids = testDataProductsFiltered
                .stream()
                .distinct()
                .map(Product::getId)
                .collect(Collectors.toList());

        CategoryPage categoryPage = home
                .getTopMenu()
                .goToWomenPage()
                .selectCategoryFilterByName(subcategory)
                .selectColorFilterByName(color_1)
                .selectColorFilterByName(color_2);

        List<Integer> productsIds = categoryPage
                .getProducts()
                .stream()
                .map(ProductBasePage::getId)
                .collect(Collectors.toList());

        // if displayed products aren't filtered correctly then there is no sense to verify other assertions
        assertThat(productsIds)
                .as("Displayed products are different then expected")
                .containsExactlyInAnyOrderElementsOf(testDataProductsFiltered_Ids);
        Allure.step("Assert if products are filtered as expected");

        SoftAssertions soft = new SoftAssertions();
        // enabled filters
        soft.assertThat(categoryPage.getEnabledFilters().keySet())
                .as("Enabled filters don't contain item with category")
                .contains(subcategory)
                .as(String.format("Enabled filters don't contain item with color '%s'", color_1))
                .contains(color_1)
                .as(String.format("Enabled filters don't contain item with color '%s'", color_2))
                .contains(color_2 );
        Allure.step(String.format("Assert if filter for category '%s' and for color '%s' and '%s' are displayed in enabled filters section",
                subcategory, color_1, color_2));
        // assert header product counter
        soft.assertThat(categoryPage.getNumberOfProductsFromProductCounter())
                .as("Value of product counter is different then expected")
                .isEqualTo(testDataProductsFiltered_Ids.size());
        Allure.step("Assert if header product counter equals to number of products filtered from test data");
        soft.assertThat(categoryPage.getPageHeader())
                .usingComparator(String::compareToIgnoreCase)
                .as(String.format("Category '%s' should be displayed in page header", category))
                .contains(category)
                .contains("categories")
                .as(String.format("Category '%s' should be displayed in page header", subcategory))
                .contains(subcategory)
                .contains("color")
                .as(String.format("Color '%s' should be displayed in page header", color_1))
                .contains(color_1)
                .as(String.format("Color '%s' should be displayed in page header", color_2))
                .contains(color_2);
        Allure.step(String.format("Assert if page header contains '%s','%s','%s' and '%s' ",
                category, subcategory, color_1, color_2));
        // filters
        // assert size filter items
        verifySizeFilter(categoryPage, testDataProductsFiltered, soft);
        // assert properties filter items
        verifyPropertiesFilter(categoryPage, testDataProductsFiltered, soft);
        // assert compositions filter items
        verifyCompositionsFilter(categoryPage, testDataProductsFiltered, soft);
        //assert styles filter items
        verifyStylesFilter(categoryPage, testDataProductsFiltered, soft);
        // assert availability filter items
        verifyInStockFilter(categoryPage, testDataProductsFiltered, soft);
        // assert condition filter items
        verifyConditionFilter(categoryPage, testDataProductsFiltered, soft);
        // assert price range filter settings
        verifyPriceRangeFilter(categoryPage, testDataProducts_Women, soft);
        // assert pagination
        verifyPagination(categoryPage,testDataProductsFiltered.size(), soft, numberOfProductsOnPage);
        soft.assertAll();
    }

    @DisplayName("CP44 Filter products by price and style")
    @ParameterizedTest(name = "{index} => Price: {0}-{1} Style: {2}")
    @CsvSource({"22.29 , 29.69, Casual",
            "16.00 , 26.73, Casual"})
    public void CP44_filterProductsByPriceAndStyle(String lowPriceR, String highPriceR, String style){
        String category = "Women";
        BigDecimal lowPrice = new BigDecimal(lowPriceR);
        BigDecimal highPrice = new BigDecimal(highPriceR);

        List<Product> testDataProducts_Women = testDataProducts
                .getProducts()
                .stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());

        List<Product> testDataProductsFiltered = testDataProducts_Women
                .stream()
                .filter(p->p.getPrice().compareTo(lowPrice)>=0 &&
                        p.getPrice().compareTo(highPrice)<=0)
                .filter(p -> p.getStyles().equalsIgnoreCase(style))
                .collect(Collectors.toList());

        List<Integer> testDataProductsFiltered_Ids = testDataProductsFiltered
                .stream()
                .distinct()
                .map(Product::getId)
                .collect(Collectors.toList());

        CategoryPage categoryPage = home
                .getTopMenu()
                .goToWomenPage()
                .setHighPriceOnFilterSlider(highPrice)
                .setLowPriceOnFilterSlider(lowPrice)
                .selectStylesFilterByName(style);

        List<Integer> productsIds = categoryPage
                .getProducts()
                .stream()
                .map(ProductBasePage::getId)
                .collect(Collectors.toList());

        SoftAssertions soft = new SoftAssertions();
        // products
        soft.assertThat(productsIds)
                .as("Displayed products are different then expected")
                .containsExactlyInAnyOrderElementsOf(testDataProductsFiltered_Ids);
        // enabled filters
        soft.assertThat(categoryPage.getEnabledFilters().keySet())
                .as("Enabled filters don't contain item with style")
                .contains(style)
                .as("Enabled filters don't contain item with price")
                .contains(categoryPage.getPriceRangeFromLabel());
        // assert header product counter
        soft.assertThat(categoryPage.getNumberOfProductsFromProductCounter())
                .as("Value of product counter is different then expected")
                .isEqualTo(testDataProductsFiltered_Ids.size());
        // filters
        // assert categories filter
        verifyCategoriesFilter(categoryPage, testDataProductsFiltered, soft ,1);
        // assert size filter items
        verifySizeFilter(categoryPage, testDataProductsFiltered, soft);
        // assert color filter items
        verifyColorFilter(categoryPage, testDataProductsFiltered, soft);
        // assert properties filter items
        verifyPropertiesFilter(categoryPage, testDataProductsFiltered, soft);
        // assert compositions filter items
        verifyCompositionsFilter(categoryPage, testDataProductsFiltered, soft);
        // assert availability filter items
        verifyInStockFilter(categoryPage, testDataProductsFiltered, soft);
        // assert condition filter items
        verifyConditionFilter(categoryPage, testDataProductsFiltered, soft);
        // assert pagination
        verifyPagination(categoryPage,testDataProductsFiltered_Ids.size(), soft, numberOfProductsOnPage);

        soft.assertAll();
    }

    private void verifyCategoriesFilter(CategoryPage categoryPage,
                                        List<Product> testDataProducts,
                                        SoftAssertions soft,
                                        int categoryLevel) {
        HashMap<String, Integer> expectedCategoriesFilterItems = new HashMap<>();
        List<Product> testDataDistinctProducts = testDataProducts
                .stream()
                .distinct()
                .collect(Collectors.toList());
        for (Product product : testDataDistinctProducts) {
            String category;
            if (categoryLevel == 1) {
                category = product.getSubcategory1stLevel();
            } else if (categoryLevel == 2) {
                category = product.getSubcategory2ndLevel();
            } else throw new IllegalArgumentException("Wrong category level. Available levels are: 1 and 2.");
            if (expectedCategoriesFilterItems.containsKey(category)) {
                Integer incrementedAmount = expectedCategoriesFilterItems.get(category) + 1;
                expectedCategoriesFilterItems.replace(category, incrementedAmount);
            } else {
                expectedCategoriesFilterItems.put(category, 1);
            }
        }
        soft.assertThat(categoryPage.getCategoriesFromFilter())
                .as("Categories filter items are different then expected")
                .containsExactlyInAnyOrderEntriesOf(expectedCategoriesFilterItems);
        Allure.step(String.format("Assert if categories filter items are : %s", expectedCategoriesFilterItems));
    }

    private void verifySizeFilter(CategoryPage categoryPage, List<Product> testDataProducts, SoftAssertions soft) {
        // sizes for each product are in one string - e.g. "S,M,L" and should be split to separate them
        HashMap<String, Integer> expectedSizeFilterItems = new HashMap<>();
        List<Product> testDataDistinctProducts = testDataProducts
                .stream()
                .distinct()
                .collect(Collectors.toList());
        for (Product product : testDataDistinctProducts) {
            String[] sizesTemp = product.getSizes().split(",");
            for (String size : sizesTemp) {
                size = size.toUpperCase();
                if (expectedSizeFilterItems.containsKey(size)) {
                    Integer incrementedAmount = expectedSizeFilterItems.get(size) + 1;
                    expectedSizeFilterItems.replace(size, incrementedAmount);
                } else {
                    expectedSizeFilterItems.put(size, 1);
                }
            }
        }
        soft.assertThat(categoryPage.getSizesFromFilter())
                .as("Size filter items are different then expected")
                .containsExactlyInAnyOrderEntriesOf(expectedSizeFilterItems);
        Allure.step(String.format("Assert if size filter items are : %s", expectedSizeFilterItems));
    }

    private void verifyColorFilter(CategoryPage categoryPage, List<Product> testDataProducts, SoftAssertions soft) {
        HashMap<String, Integer> expectedColorFilterItems = new HashMap<>();
        for (Product product : testDataProducts) {
            String color = product.getColor();
            if (expectedColorFilterItems.containsKey(color)) {
                Integer incrementedAmount = expectedColorFilterItems.get(color) + 1;
                expectedColorFilterItems.replace(color, incrementedAmount);
            } else {
                expectedColorFilterItems.put(color, 1);
            }
        }
        soft.assertThat(categoryPage.getColorsFromFilter())
                .as("Colors filter items are different then expected")
                .containsExactlyInAnyOrderEntriesOf(expectedColorFilterItems);
        Allure.step(String.format("Assert if colors filter items are : %s", expectedColorFilterItems));
    }

    private void verifyPropertiesFilter(CategoryPage categoryPage, List<Product> testDataProducts, SoftAssertions soft) {
        List<Product> testDataDistinctProducts = testDataProducts
                .stream()
                .distinct()
                .collect(Collectors.toList());
        HashMap<String, Integer> expectedPropertiesFilterItems = new HashMap<>();
        for (Product product : testDataDistinctProducts) {
            String properties = product.getProperties();
            if (expectedPropertiesFilterItems.containsKey(properties)) {
                Integer incrementedAmount = expectedPropertiesFilterItems.get(properties) + 1;
                expectedPropertiesFilterItems.replace(properties, incrementedAmount);
            } else {
                expectedPropertiesFilterItems.put(properties, 1);
            }
        }
        soft.assertThat(categoryPage.getPropertiesFromFilter())
                .as("Properties filter items are different then expected")
                .containsExactlyInAnyOrderEntriesOf(expectedPropertiesFilterItems);
        Allure.step(String.format("Assert if properties filters are : %s", expectedPropertiesFilterItems));
    }

    private void verifyCompositionsFilter(CategoryPage categoryPage, List<Product> testDataProducts, SoftAssertions soft) {
        List<Product> testDataDistinctProducts = testDataProducts
                .stream()
                .distinct()
                .collect(Collectors.toList());
        HashMap<String, Integer> expectedCompositionsFilterItems = new HashMap<>();
        for (Product product : testDataDistinctProducts) {
            String compositions = product.getCompositions();
            if (expectedCompositionsFilterItems.containsKey(compositions)) {
                Integer incrementedAmount = expectedCompositionsFilterItems.get(compositions) + 1;
                expectedCompositionsFilterItems.replace(compositions, incrementedAmount);
            } else {
                expectedCompositionsFilterItems.put(compositions, 1);
            }
        }
        soft.assertThat(categoryPage.getCompositionsFromFilter())
                .as("Compositions filter items are different then expected")
                .containsExactlyInAnyOrderEntriesOf(expectedCompositionsFilterItems);
        Allure.step(String.format("Assert if compositions filters are : %s", expectedCompositionsFilterItems));
    }

    private void verifyStylesFilter(CategoryPage categoryPage, List<Product> testDataProducts, SoftAssertions soft) {
        List<Product> testDataDistinctProducts = testDataProducts
                .stream()
                .distinct()
                .collect(Collectors.toList());
        HashMap<String, Integer> expectedStylesFilterItems = new HashMap<>();
        for (Product product : testDataDistinctProducts) {
            String styles = product.getStyles();
            if (expectedStylesFilterItems.containsKey(styles)) {
                Integer incrementedAmount = expectedStylesFilterItems.get(styles) + 1;
                expectedStylesFilterItems.replace(styles, incrementedAmount);
            } else {
                expectedStylesFilterItems.put(styles, 1);
            }
        }
        soft.assertThat(categoryPage.getStylesFromFilter())
                .as("Styles filter items are different then expected")
                .containsExactlyInAnyOrderEntriesOf(expectedStylesFilterItems);
        Allure.step(String.format("Assert if styles filters are : %s", expectedStylesFilterItems));
    }

    private void verifyPriceRangeFilter(CategoryPage categoryPage, List<Product> testDataProducts, SoftAssertions soft) {
        List<BigDecimal> testDataProductsPricesSorted = testDataProducts
                .stream()
                .map(Product::getPrice)
                .sorted()
                .collect(Collectors.toList());
        BigDecimal expectedLowPriceRange = testDataProductsPricesSorted
                .get(0)
                .setScale(0, RoundingMode.DOWN)
                .setScale(2,RoundingMode.DOWN)
                .subtract(new BigDecimal(1));

        BigDecimal expectedHighPriceRange = testDataProductsPricesSorted
                .get(testDataProductsPricesSorted.size() - 1)
                .setScale(0, RoundingMode.CEILING)
                .setScale(2,RoundingMode.DOWN)
                .add(new BigDecimal(2));
        soft.assertThat(categoryPage.getLowPriceRangeFromLabel())
                .as("Price filter low limit is different then expected")
                .isGreaterThanOrEqualTo(expectedLowPriceRange);
        Allure.step(String.format("Assert if price low limit is greater or equal to %s", expectedLowPriceRange));
        soft.assertThat(categoryPage.getHighPriceRangeFromLabel())
                .as("Price filter high limit is different then expected")
                .isLessThanOrEqualTo(expectedHighPriceRange);
        Allure.step(String.format("Assert if price high limit is less or equal to %s", expectedHighPriceRange));
    }

    private void verifyConditionFilter(CategoryPage categoryPage, List<Product> testDataProducts, SoftAssertions soft) {
        List<Product> testDataDistinctProducts = testDataProducts
                .stream()
                .distinct()
                .collect(Collectors.toList());
        HashMap<String, Integer> expectedConditionsFilterItems = new HashMap<>();
        for (Product product : testDataDistinctProducts) {
            String compositions = product.getCondition();
            if (expectedConditionsFilterItems.containsKey(compositions)) {
                Integer incrementedAmount = expectedConditionsFilterItems.get(compositions) + 1;
                expectedConditionsFilterItems.replace(compositions, incrementedAmount);
            } else {
                expectedConditionsFilterItems.put(compositions, 1);
            }
        }
        soft.assertThat(categoryPage.getConditionFromFilter())
                .as("Condition filter items are different then expected")
                .containsExactlyInAnyOrderEntriesOf(expectedConditionsFilterItems);
        Allure.step(String.format("Assert if condition filter items are : %s", expectedConditionsFilterItems));
    }

    private void verifyInStockFilter(CategoryPage categoryPage, List<Product> testDataProducts, SoftAssertions soft) {
        List<Product> testDataDistinctProductsInStock = testDataProducts
                .stream()
                .filter(p -> p.getAvailability().getValue().equalsIgnoreCase("In stock"))
                .distinct()
                .collect(Collectors.toList());
        soft.assertThat(categoryPage.getAvailabilityFromFilter().get("In stock"))
                .as("Number of products in stock is different then expected")
                .isEqualTo(testDataDistinctProductsInStock.size());
        Allure.step(String.format("Assert if number of products in stock is %d", testDataDistinctProductsInStock.size()));

    }

    private void verifyPagination(CategoryPage categoryPage,
                                  int numberOfTestProducts,
                                  SoftAssertions soft,
                                  int numberOfProductsOnPage){
        soft.assertThat(categoryPage.getFirstNumberFromPagination())
                .as("The beginning number in pagination should be 1")
                .isEqualTo(1);
        Allure.step("Assert if pagination starts with 1");
        if(numberOfTestProducts<=numberOfProductsOnPage){
            soft.assertThat(categoryPage.getLastNumberFromPagination())
                    .as("End number in pagination is different then number of products filtered from test data")
                    .isEqualTo(numberOfTestProducts);
            Allure.step(String.format("Assert if pagination ends with %d",numberOfTestProducts));
        } else {
            soft.assertThat(categoryPage.getLastNumberFromPagination())
                    .as("End number in pagination is different then number of products displayed on the page")
                    .isEqualTo(numberOfProductsOnPage);
            Allure.step(String.format("Assert if pagination ends with %d",numberOfProductsOnPage));
        }
        soft.assertThat(categoryPage.getTotalNumberFromPagination())
                .as("Total number of items in pagination is different then number of products filtered from test data")
                .isEqualTo(numberOfTestProducts);
        Allure.step(String.format("Assert if total number of items in pagination is %d",numberOfTestProducts));
    }

}
