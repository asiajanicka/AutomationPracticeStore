package utils;

import enums.ContactUsSubject;
import lombok.Getter;
import lombok.Setter;

import java.util.Properties;


@Getter
@Setter
public class ContactUsMessage {

    private ContactUsSubject subject;
    private String email;
    private String orderReference;
    private String messageText;
    private String attachment;

    public ContactUsMessage() {
        subject = ContactUsSubject.CHOOSE;
        email = "";
        orderReference = "";
        messageText = "";
        attachment = "";
    }

    public ContactUsMessage(Properties properties) {
        subject = ContactUsSubject.valueOf(properties.getProperty("contactUsMessage.subject"));
        email = properties.getProperty("contactUsMessage.email");
        orderReference = properties.getProperty("contactUsMessage.orderReference");
        messageText = properties.getProperty("contactUsMessage.message");
        attachment = properties.getProperty("contactUsMessage.attachment");
    }

    public ContactUsMessage(ContactUsMessage that) {
        this.subject = that.getSubject();
        this.email = that.getEmail();
        this.orderReference = that.getOrderReference();
        this.messageText = that.getMessageText();
        this.attachment = that.getAttachment();
    }


}
