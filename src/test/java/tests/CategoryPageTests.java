package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pageObjects.categoryPages.CategoryPage;
import pageObjects.homePages.HomePage;

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

        System.out.println(home.getTopMenu().goToWomenPage().getProducts());
    }
}
