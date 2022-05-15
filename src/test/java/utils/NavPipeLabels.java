package utils;

import lombok.Getter;

import java.util.Properties;

@Getter
public class NavPipeLabels {

    private String contactUs;

    public NavPipeLabels(Properties properties) {
        contactUs = properties.getProperty("navigationPipe.contactUs");
    }
}
