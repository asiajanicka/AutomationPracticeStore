package tests;

import io.qameta.allure.Allure;
import io.qameta.allure.Feature;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pageObjects.homePages.HomePage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@Feature("Top Menu")
public class TopMenuTests extends BaseTest {

    private HomePage home;

    @BeforeEach
    public void testSetup() {
        super.testSetup();
        driver.navigate().to(configuration.getBaseUrl());
        home = new HomePage(driver);
    }

    @Test
    @DisplayName("All subcategories should have names")
    public void shouldHaveNamesTest() {
        List<String> womenSubcategoriesNames = home
                .getTopMenu()
                .hoverOnWomenMenu()
                .getCategoriesNames();
        List<String> listOfEmptyWomenCategoryNames = womenSubcategoriesNames
                .stream()
                .filter(cat -> cat.isEmpty())
                .collect(Collectors.toList());

        List<String> dressesSubcategoriesNames = home
                .getTopMenu()
                .hoverOnDressesMenu()
                .getCategoriesNames();
        List<String> listOfEmptyDressesCategoryNames = dressesSubcategoriesNames
                .stream()
                .filter(cat -> cat.isEmpty()
                ).collect(Collectors.toList());

        SoftAssertions softy = new SoftAssertions();
        softy.assertThat(listOfEmptyWomenCategoryNames).isEmpty();
        softy.assertThat(listOfEmptyDressesCategoryNames).isEmpty();
        softy.assertAll();
        Allure.step("Assert is there are no empty subcategories in Women and in Dresses");
    }

    @Test
    @DisplayName("Go to Women category page")
    public void shouldDisplayWomenPage() {
        String expectedCategoryName = "WOMEN";
        assertThat(home.getTopMenu()
                .goToWomenPage()
                .getCategoryName()).isEqualTo(expectedCategoryName); // dodac zmienna
        Allure.step(String.format("Assert if page category is '%s'", expectedCategoryName));
    }

    @Test
    @DisplayName("Go to Dresses category page")
    public void shouldDisplayDressesPage() {
        String expectedCategoryName = "DRESSES";
        assertThat(home.getTopMenu()
                .goToDressesPage()
                .getCategoryName()).isEqualTo(expectedCategoryName); // dodać zmienna
        Allure.step(String.format("Assert if page category is '%s'", expectedCategoryName));
    }

    @Test
    @DisplayName("Go to T-shirts category page")
    public void shouldTShirtsWomenPage() {
        String expectedCategoryName = "T-SHIRTS";
        assertThat(home.getTopMenu()
                .goToTShirtsPage()
                .getCategoryName()).isEqualTo(expectedCategoryName); // dodać zmienna
        Allure.step(String.format("Assert if page category is '%s'", expectedCategoryName));
    }

    @Test
    @DisplayName("Go to Blog page")
    public void shouldDisplayBlogPage() {
        String parentWindowHandle = driver.getWindowHandle();
        home.getTopMenu().goToBlogPage();
        Set<String> windowHandles = driver.getWindowHandles();
        windowHandles.remove(parentWindowHandle);
        String windowHandleOfBlogPage = windowHandles.iterator().next();
        driver.switchTo().window(windowHandleOfBlogPage);
        isPageCurrentLoaded();
        String titleOfBlogPage = driver.getTitle();
        assertThat(titleOfBlogPage).contains("Blog");
        Allure.step("Assert if title of newly opened pages contains word 'Blog");
    }

    @Test
    @DisplayName("Go to first level subcategory page")
    public void shouldDisplay1stLevelSubcategoryPage(){
        String expected1stLevelSubcategoryName = "TOPS";
        assertThat(home.getTopMenu()
                .hoverOnWomenMenu()
                .getWomenSubcategory(expected1stLevelSubcategoryName)
                .goToPage()
                .getCategoryName()).isEqualTo(expected1stLevelSubcategoryName);
        Allure.step(String.format("Assert if page category is '%s'", expected1stLevelSubcategoryName));
    }

    @Test
    @DisplayName("Go to second level subcategory page")
    public void shouldDisplay2ndLevelSubcategory() {
        String expected1stLevelSubcategoryName = "TOPS";
        String expected2ndLevelSubcategoryName = "BLOUSES";
        assertThat(home.getTopMenu()
                .hoverOnWomenMenu()
                .getWomenSubcategory(expected1stLevelSubcategoryName)
                .getSubcategory(expected2ndLevelSubcategoryName)
                .goToPage()
                .getCategoryName()).isEqualTo(expected2ndLevelSubcategoryName);
        Allure.step(String.format("Assert if page category is '%s'", expected2ndLevelSubcategoryName));
    }
}
