package tests;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pageObjects.homePages.HomePage;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import static org.assertj.core.api.Assertions.assertThat;

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
    }

    @Test
    @DisplayName("Go to Women category page")
    public void shouldDisplayWomenPage() {
        assertThat(home.getTopMenu()
                .goToWomenPage()
                .getCategoryName()).isEqualTo("WOMEN"); // dodac zmienna
    }

    @Test
    @DisplayName("Go to Dresses category page")
    public void shouldDisplayDressesPage() {
        assertThat(home.getTopMenu()
                .goToDressesPage()
                .getCategoryName()).isEqualTo("DRESSES"); // dodać zmienna
    }

    @Test
    @DisplayName("Go to T-shirts category page")
    public void shouldTShirtsWomenPage() {
        assertThat(home.getTopMenu()
                .goToTShirtsPage()
                .getCategoryName()).isEqualTo("T-SHIRTS"); // dodać zmienna
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
        assertThat(titleOfBlogPage).contains("Blog"); // dodac zmienna
    }

    @Test
    @DisplayName("Go to first level subcategory page")
    public void shouldDisplay1stLevelSubcategoryPage() throws InterruptedException {
        assertThat(home.getTopMenu()
                .hoverOnWomenMenu()
                .getWomenSubcategory("TOPS")
                .goToPage()
                .getCategoryName()).isEqualTo("TOPS");

    }

    @Test
    @DisplayName("Go to second level subcategory page")
    public void shouldDisplay2ndLevelSubcategory() {
        assertThat(home.getTopMenu()
                .hoverOnWomenMenu()
                .getWomenSubcategory("TOPS")
                .getSubcategory("Blouses")
                .goToPage()
                .getCategoryName()).isEqualTo("BLOUSES");
    }
}
