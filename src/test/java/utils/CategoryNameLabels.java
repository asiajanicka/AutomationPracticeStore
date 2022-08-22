package utils;

import lombok.Getter;

import java.util.Properties;

@Getter
public class CategoryNameLabels {

    private String women;
    private String dresses;
    private String tShirts;
    private String women1stLevelSub;
    private String women2ndLevelSub;

    public CategoryNameLabels(Properties properties){
        women = properties.getProperty("categoryName.women");
        dresses = properties.getProperty("categoryName.dresses");
        tShirts = properties.getProperty("categoryName.tShirts");
        women1stLevelSub = properties.getProperty("categoryName.women.1stLevelSub");
        women2ndLevelSub = properties.getProperty("categoryName.women.2ndLevelSub");
    }
}
