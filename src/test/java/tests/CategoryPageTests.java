package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pageObjects.categoryPages.CategoryPage;
import pageObjects.homePages.HomePage;

import static org.assertj.core.api.Assertions.assertThat;

public class CategoryPageTests extends BaseTest{

    private HomePage home;

    @BeforeEach
    public void testSetup() {
        super.testSetup();
        driver.navigate().to(configuration.getBaseUrl());
        home = new HomePage(driver);
    }

    @Test
    public void test(){

        CategoryPage categoryPage = home.getTopMenu().goToWomenPage();
        int size = categoryPage.getProducts().size();
        int numberOfProductsFromProductCounter = categoryPage.getNumberOfProductsFromProductCounter();
        assertThat(size).isEqualTo(numberOfProductsFromProductCounter);
    }
}
