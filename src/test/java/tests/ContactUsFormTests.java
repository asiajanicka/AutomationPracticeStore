package tests;

import enums.ContactUsSubject;
import io.qameta.allure.Allure;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import org.apache.commons.lang.RandomStringUtils;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pageObjects.ContactUsPage;
import pageObjects.NavigationPipePage;
import urls.Urls;
import utils.ContactUsMessage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@Feature("Contact Us Form")
public class ContactUsFormTests extends BaseTest {

    private ContactUsPage contactUs;
    private NavigationPipePage navigationPipe;

    @BeforeEach
    public void testSetup() {
        super.testSetup();
        String contactUsURL = configuration.getBaseUrl() + Urls.contactUs;
        driver.navigate().to(contactUsURL);

        contactUs = new ContactUsPage(driver);
        navigationPipe = new NavigationPipePage(driver);
    }

    @Test()
    @DisplayName("Send message with valid data without attachment and go to home page")
    public void shouldSendContactUsFormWithValidDataWithoutAttachment() {
        SoftAssertions softly = new SoftAssertions();
        verifyContactUsLabelInNavigationPipe(softly);
        verifyNumberOfEntriesInSubjectList(softly);

        ContactUsMessage message = new ContactUsMessage(testData.getMessage());
        message.setAttachment("");

        contactUs.sendFormWithoutAttachment(message);

        softly.assertThat(contactUs.isGreenAlertDisplayed())
                .withFailMessage("Green alert is not displayed")
                .isTrue();

        goToHomePageAfterClickOnHome(softly);
        softly.assertAll();
    }

    @Test()
    @DisplayName("Send message with valid data with attachment")
    public void shouldSendContactUsFormWithValidDataWithAttachment(){
        ContactUsMessage message = new ContactUsMessage(testData.getMessage());

        contactUs.sendFormWithAttachment(message);

        await().untilAsserted(()->assertThat(contactUs.isGreenAlertDisplayed())
                .withFailMessage("Green alert is not displayed")
                .isTrue());
        Allure.step("Assert if green alert is displayed after sending message with attachment");
    }

    @Test
    @DisplayName("Don't send empty message and close red alert")
    public void shouldNotAllowToSendEmptyContactUsForm() {
        contactUs.sendMessage();

        assertThat(contactUs.isRedAlertDisplayed())
                .withFailMessage("Red alert is not displayed")
                .isTrue();
        Allure.step("Assert if red alert is displayed");

        contactUs.closeRedAlert();

        assertThat(contactUs.isRedAlertClosed())
                .withFailMessage("Red alert is still displayed")
                .isTrue();
        Allure.step("Assert if red alert is closed after click on cross button");
    }

    @Test
    @DisplayName(("Don't send message with subject only"))
    public void shouldNotAllowToSendContactUsFormWithSubjectHeadingOnly() {
        ContactUsMessage message = new ContactUsMessage();
        message.setSubject(testData.getMessage().getSubject());

        contactUs.sendFormWithoutAttachment(message);

        assertThat(contactUs.isRedAlertDisplayed())
                .withFailMessage("Red alert is not displayed")
                .isTrue();
        Allure.step("Assert if red alert is displayed");
    }

    @Test
    @DisplayName("Don't send message without subject")
    public void shouldNotAllowToSendContactUsFormWithoutSubjectHeading() {
        ContactUsMessage message = new ContactUsMessage(testData.getMessage());
        message.setSubject(ContactUsSubject.CHOOSE);

        contactUs.sendFormWithoutAttachment(message);

        assertThat(contactUs.isRedAlertDisplayed())
                .withFailMessage("Red alert is not displayed")
                .isTrue();
        Allure.step("Assert if red alert is displayed");
    }

    @Test
    @DisplayName("Don't send message with empty email")
    public void shouldNotAllowToSendContactUsFormWithEmptyEmail() {
        ContactUsMessage message = new ContactUsMessage(testData.getMessage());
        message.setEmail("");

        contactUs.sendFormWithoutAttachment(message);

        assertThat(contactUs.isRedAlertDisplayed())
                .withFailMessage("Red alert is not displayed")
                .isTrue();
        Allure.step("Assert if red alert is displayed");
    }

    @Test
    @DisplayName("Don't send message with email address only")
    public void shouldNotAllowToSendContactUsFormWithEmailAddressOnly() {
        ContactUsMessage message = new ContactUsMessage();
        message.setEmail(testData.getMessage().getEmail());

        contactUs.sendFormWithoutAttachment(message);

        assertThat(contactUs.isRedAlertDisplayed())
                .withFailMessage("Red alert is not displayed")
                .isTrue();
        Allure.step("Assert if red alert is displayed");
    }

    @Test
    @DisplayName("Don't send message with invalid email")
    public void shouldNotAllowToSendContactUsFormWithInvalidEmail() {
        String invalidEmail = "www@pl";
        ContactUsMessage message = new ContactUsMessage(testData.getMessage());
        message.setEmail(invalidEmail);

        contactUs.sendFormWithoutAttachment(message);

        assertThat(contactUs.isRedAlertDisplayed())
                .withFailMessage("Red alert is not displayed")
                .isTrue();
        Allure.step(String.format("Assert if red alert is displayed when message has invalid email: %s ", invalidEmail));
    }

    @Test
    @DisplayName("Send message with max length valid email")
    public void shouldAllowToSendMessageWithMaxLengthEmail() {
        String validEmail = RandomStringUtils.randomAlphabetic(64) + "@" +
                RandomStringUtils.randomAlphabetic(185) + ".com";

        ContactUsMessage message = new ContactUsMessage(testData.getMessage());
        message.setEmail(validEmail);

        contactUs.sendFormWithoutAttachment(message);

        assertThat(contactUs.isGreenAlertDisplayed())
                .withFailMessage(String.format("Green alert is not displayed for valid message where max length (%d) " +
                                "email: %s", validEmail.length(), validEmail))
                .isTrue();
        Allure.step("Assert if green alert is displayed for valid message with max length email: "
                + validEmail);
    }

    @Test
    @DisplayName("Don't send message with order reference only")
    public void shouldNotAllowToSendContactUsFormWithOrderReferenceOnly() {
        ContactUsMessage message = new ContactUsMessage();
        message.setOrderReference(testData.getMessage().getOrderReference());

        contactUs.sendFormWithoutAttachment(message);

        assertThat(contactUs.isRedAlertDisplayed())
                .withFailMessage("Red alert is not displayed")
                .isTrue();
        Allure.step("Assert if red alert is displayed");
    }

    @Test
    @DisplayName("Send message with empty order reference")
    public void shouldAllowToSendContactUsFormWithEmptyOrderReference() {
        ContactUsMessage message = new ContactUsMessage(testData.getMessage());
        message.setOrderReference("");

        contactUs.sendFormWithoutAttachment(message);

        assertThat(contactUs.isGreenAlertDisplayed())
                .withFailMessage("Green alert is not displayed")
                .isTrue();
        Allure.step("Assert if green alert is displayed");
    }

    @Test
    @DisplayName("Don't send message with text only")
    public void shouldNotAllowToSendContactUsFormWithMessageTextOnly() {
        ContactUsMessage message = new ContactUsMessage();
        message.setMessageText(testData.getMessage().getMessageText());

        contactUs.sendFormWithoutAttachment(message);

        assertThat(contactUs.isRedAlertDisplayed())
                .withFailMessage("Red alert is not displayed")
                .isTrue();
        Allure.step("Assert if red alert is displayed");
    }

    @Test
    @DisplayName("Don't send message with empty text")
    public void shouldNotAllowToSendContactUsFormWithEmptyMessageText() {
        ContactUsMessage message = new ContactUsMessage(testData.getMessage());
        message.setMessageText("");

        contactUs.sendFormWithoutAttachment(message);

        assertThat(contactUs.isRedAlertDisplayed())
                .withFailMessage("Red alert is not displayed")
                .isTrue();
        Allure.step("Assert if red alert is displayed");
    }

   @Step("Verify if it is possible to go to home page after sending message")
    public void goToHomePageAfterClickOnHome(SoftAssertions softly) {
        String expectedHomePageUrl = configuration.getBaseUrl() + Urls.homeLong;
        softly.assertThat(contactUs.goToHomePage())
                .withFailMessage("Redirection was to %s instead to %s",
                        driver.getCurrentUrl(),
                        expectedHomePageUrl)
                .isEqualTo(expectedHomePageUrl);
    }

    @Step("Verify if contact us label is displayed in navigation pipe")
    public void verifyContactUsLabelInNavigationPipe(SoftAssertions softly){
        String contactUsLabel = appProperties.getNavPipeLabel().getContactUs();

        softly.assertThat(navigationPipe.getElements().size())
                .withFailMessage("Level of navigation in page for contact us is deeper then 1")
                .isEqualTo(1);

        softly.assertThat(navigationPipe.getElements())
                .withFailMessage(String.format("Navigation pipe does not display \"%s\" label", contactUsLabel))
                .contains(contactUsLabel);
    }

    @Step("Verify if subject list is not empty")
    public void verifyNumberOfEntriesInSubjectList(SoftAssertions softly) {
        softly.assertThat(contactUs.getNumberOfAvailableSubjects())
                .withFailMessage(" There should be at least one subject in the list, " +
                                "but there is %d"
                        , contactUs.getNumberOfAvailableSubjects())
                .isGreaterThanOrEqualTo(1);
    }
}
