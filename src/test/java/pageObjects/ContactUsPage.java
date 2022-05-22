package pageObjects;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;
import utils.ContactUsMessage;

public class ContactUsPage extends BasePage{

    public ContactUsPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(id = "id_contact")
    private WebElement subjectSelect;
    @FindBy(id = "email")
    private WebElement emailInput;
    @FindBy(id = "id_order")
    private WebElement orderReferenceInput;
    @FindBy(className = "filename")
    private WebElement attachFileBox;
    @FindBy(id = "fileUpload")
    private WebElement attachmentInput;
    @FindBy(id = "message")
    private WebElement messageTextArea;
    @FindBy(id = "submitMessage")
    private WebElement sendBtn;
    @FindBy(className = "alert-danger")
    private WebElement redAlert;
    @FindBy(className = "alert-success")
    private WebElement greenAlert;
    @FindBy(css = ".footer_links .btn")
    private WebElement homeBtn;

    public boolean isRedAlertDisplayed(){
        return isElementDisplayed(redAlert);
    }

    public boolean isRedAlertClosed(){
        return isElementRemoved(redAlert);
    }

    public boolean isGreenAlertDisplayed(){
        return isElementDisplayed(greenAlert);
    }

    @Step("Send message")
    public void sendMessage(){
        sendBtn.click();
    }

    @Step("Send message without attachment")
    public void sendFormWithoutAttachment(ContactUsMessage message){
        Select subject = new Select(subjectSelect);
        subject.selectByVisibleText(message.getSubject().getValue());
        emailInput.sendKeys(message.getEmail());
        orderReferenceInput.sendKeys(message.getOrderReference());
        messageTextArea.sendKeys(message.getMessageText());
        sendBtn.click();
    }

    @Step("Send message with attachment")
    public void sendFormWithAttachment(ContactUsMessage message){
        Select subject = new Select(subjectSelect);
        subject.selectByVisibleText(message.getSubject().getValue());
        emailInput.sendKeys(message.getEmail());
        orderReferenceInput.sendKeys(message.getOrderReference());
        messageTextArea.sendKeys(message.getMessageText());
        attachmentInput.sendKeys(message.getAttachment());
        sendBtn.click();
    }

    public int getNumberOfAvailableSubjects(){
        Select subject = new Select(subjectSelect);
        if(subject.getOptions().size()==0)
        {
            throw new IllegalStateException("Subject dropdown list in Contact Us form is empty. " +
                    "There is no default entry 'choose' in the list.");
        }
//        default entry "choose" is subtracted from total amount of subjects in the list
        return subject.getOptions().size() - 1;
    }

    @Step("Close alert")
    public void closeRedAlert(){
        WebElement p = redAlert.findElement(By.cssSelector("p"));
        int xOffset = p.getRect().getX() + 12;
        int yOffset = p.getRect().getY() + 9;
        Actions action = new Actions(driver);
        action.moveByOffset(xOffset,yOffset).click().build().perform();
    }

    @Step("Go to home page")
    public String goToHomePage(){
        homeBtn.click();
        return driver.getCurrentUrl();
    }
}
