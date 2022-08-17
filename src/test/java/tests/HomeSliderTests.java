package tests;

import io.qameta.allure.Allure;
import io.qameta.allure.Feature;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;
import pageObjects.homePages.HomePage;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@Feature("Home Slider")
public class HomeSliderTests extends BaseTest {

    private HomePage home;

    @BeforeEach
    public void testSetup() {
        super.testSetup();
        driver.navigate().to(configuration.getBaseUrl());
        home = new HomePage(driver);
    }

    @Test
    @DisplayName("Slider has defined number of slides")
    public void shouldHaveDefinedNumberOfSlidesInSlider() {
        int expectedNumberOfSlides = appProperties.getNumberOfSlidesInCarousel();
        assertThat(home.getSlider().getNumberOfSlides())
                .withFailMessage("Number of slides is different then %d", expectedNumberOfSlides)
                .isEqualTo(expectedNumberOfSlides);
        Allure.step(String.format("Assert if number of slides is equal to %d", expectedNumberOfSlides));
    }

    @Test
    @DisplayName("Display first slide after page launch")
    public void shouldSeeFirstSlideAfterPageLaunch() {
        assertThat(home.getSlider().isElementInViewPort(0))
                .withFailMessage("First slide is not displayed in view port")
                .isTrue();
        Allure.step("Assert if first slide from the list is displayed after page launch");
    }

    @Test
    @DisplayName("Move to next slide")
    public void shouldMoveToNextSlideByClickOnForwardBtn() {
        home.getSlider().goToNextSlide();

        await().atMost(3, SECONDS).untilAsserted(() -> assertThat(home.getSlider().isElementInViewPort(1))
                .withFailMessage("Slide did not change from 0 do 1")
                .isTrue());
        Allure.step("Assert if next slide (with index 1) is displayed");
    }

    @Test
    @DisplayName("Display all slides one by one")
    public void shouldDisplayAllSlidesOneByOne() {
        SoftAssertions softly = new SoftAssertions();
        for (WebElement slide : home.getSlider().getElements()) {
            int indexOfSlide = home.getSlider().getElements().indexOf(slide);
            await()
                    .pollInterval(500, MILLISECONDS)
                    .untilAsserted(() -> assertThat(home.getSlider().isElementInViewPort(slide))
                            .withFailMessage("Slide %d not is displayed", indexOfSlide)
                            .isTrue());
            Allure.step(String.format("Assert if slide %d is displayed", indexOfSlide));

            // check heading
            softly.assertThat(home.getSlider().getCurrentHeading())
                    .withFailMessage("Heading of slide %d is empty", indexOfSlide)
                    .isNotEmpty();
            // check description
            softly.assertThat(home.getSlider().getCurrentDescription())
                    .withFailMessage("Description of slide %d is empty", indexOfSlide)
                    .isNotEmpty();
            // check img
            softly.assertThat(home.getSlider().getCurrentImg())
                    .withFailMessage("Image of slide %d is empty", indexOfSlide)
                    .isNotEmpty();
            // check hyperlink
            softly.assertThat(home.getSlider().hasCurrentSlideHyperLink())
                    .withFailMessage("Slide %d does not have href attribute or is empty", indexOfSlide)
                    .isTrue();
            // check "SHOP NOW" button
            softly.assertThat(home.getSlider().isShopNowBtnClickable()).isTrue();
            home.getSlider().goToNextSlide();
        }
        softly.assertAll();
        Allure.step("Assert if all slides have header, description, image, hyperlink and 'SHOP NOW' button");
    }

    @Test
    @DisplayName("Move to first slide after last slide")
    public void shouldMoveToFirstSlideAfterLastSlide() {
        for (WebElement slide : home.getSlider().getElements()) {
            await().until(() -> home.getSlider().isElementInViewPort(slide));
            int indexOfSlide = home.getSlider().getElements().indexOf(slide);
            Allure.step(String.format("Slide %d is displayed", indexOfSlide));
            home.getSlider().goToNextSlide();
        }

        await().atMost(1000, MILLISECONDS).pollInterval(500, MILLISECONDS)
                .untilAsserted(() -> assertThat(home.getSlider().isElementInViewPort(0))
                        .withFailMessage("First slide (with index 0) is not displayed")
                        .isTrue());
        Allure.step("Assert if first slide (with index 0) is displayed again");
    }

    @Test
    @Feature("Home Slider")
    @DisplayName("Move to previous slide")
    public void shouldMoveBackToPreviousSlideAfterClickOnBackBtn() {
        home.getSlider().goToNextSlide();
        await().atMost(1000, MILLISECONDS).until(() -> home.getSlider().isElementInViewPort(1));
        home.getSlider().goToPreviousSlide();

        await().atMost(1000, MILLISECONDS)
                .untilAsserted(() -> assertThat(home.getSlider().isElementInViewPort(0))
                        .withFailMessage("Previous slide (with index 0) is not displayed")
                        .isTrue());
        Allure.step("Assert if previous slide (with index 0) is displayed again");
    }

    @Test
    @DisplayName("Move back to last slide from first slide")
    public void shouldMoveToLastSlideFromFirstSlideAfterClickOnBackBtnTest() {
        int indexOfLastSlide = home.getSlider().getNumberOfSlides() - 1;
        home.getSlider().goToPreviousSlide();

        await().atMost(1000, MILLISECONDS).pollInterval(500, MILLISECONDS)
                .untilAsserted(() -> assertThat(home.getSlider().isElementInViewPort(indexOfLastSlide))
                        .withFailMessage(String.format("Last slide with index %d is not displayed", indexOfLastSlide))
                        .isTrue());
        Allure.step(String.format("Assert if last slide (with index %d) is displayed", indexOfLastSlide));
    }
}



