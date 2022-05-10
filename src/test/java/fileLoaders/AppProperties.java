package fileLoaders;

import java.math.BigDecimal;
import java.util.Currency;

public class AppProperties extends PropertiesLoader{

    public AppProperties() {
        super("app.properties");
    }

    public int getNumberOfDisplayedElementsInTabContent(){
        return Integer.valueOf(super.properties.getProperty("numberOfDisplayedElementsInTabContent"));
    }

    public String getCurrencySymbol(){
        Currency cur = Currency.getInstance(super.properties.getProperty("currency"));
        return cur.getSymbol();
    }

    public int getLengthOfProductNameLabelInCart(){
        return Integer.valueOf(super.properties.getProperty("lengthOfProductNameLabelInCart"));
    }

    public BigDecimal getShippingCostValue(){
        return new BigDecimal(super.properties.getProperty("shippingCostValue"));
    }

    public int getNumberOfSlidesInCarousel(){
        return Integer.valueOf(super.properties.getProperty("numberOfSlidesInCarousel"));
    }

    public int getNumberOfThumbnailsDisplayedForProduct(){
        return Integer.valueOf(super.properties.getProperty("minRequiredNumberOfThumbnailsDisplayedForProduct"));
    }
}
