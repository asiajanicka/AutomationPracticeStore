package tests;

import fileLoaders.AppPropertiesReader;
import fileLoaders.ConfigReader;
import fileLoaders.ProductTestDataFromExcelReader;
import fileLoaders.TestDataReader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pageObjects.categoryPages.CategoryPage;
import pageObjects.homePages.HomePage;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class CategoryPageTests extends BaseTest{

    private HomePage home;
    private ProductTestDataFromExcelReader testDataProducts;

    @BeforeEach
    public void testSetup() {
        super.testSetup();
        driver.navigate().to(configuration.getBaseUrl());
        home = new HomePage(driver);
    }

    @BeforeAll
    public void initialSetup() {
        super.initialSetup();
        testDataProducts = new ProductTestDataFromExcelReader();

    }

    @Test
    public void test(){

//        CategoryPage categoryPage = home.getTopMenu().goToWomenPage();
//        int size = categoryPage.getProducts().size();
//        int numberOfProductsFromProductCounter = categoryPage.getNumberOfProductsFromProductCounter();
//        assertThat(size).isEqualTo(numberOfProductsFromProductCounter);

        System.out.println(testDataProducts.getProducts().get(0));


    }
}
