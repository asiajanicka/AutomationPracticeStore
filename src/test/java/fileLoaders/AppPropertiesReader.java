package fileLoaders;

import lombok.Getter;
import utils.NavPipeLabels;

import java.math.BigDecimal;
import java.util.Currency;

@Getter
public class AppPropertiesReader extends PropertiesReader {

    private int numberOfDisplayedElementsInTabContent;
    private String currency;
    private int lengthOfProductNameLabelInCart;
    private BigDecimal shippingCostValue;
    private int numberOfSlidesInCarousel;
    private int minNumberOfThumbnailsForProduct;

    private NavPipeLabels navPipeLabel;

    public AppPropertiesReader() {
        super("app.properties");
    }

    void loadData(){
        numberOfDisplayedElementsInTabContent = Integer
                .valueOf(super.properties.getProperty("numberOfDisplayedElementsInTabContent"));
        currency = Currency.getInstance(super.properties.getProperty("currency")).getSymbol();
        lengthOfProductNameLabelInCart = Integer.valueOf(super.properties.getProperty("lengthOfProductNameLabelInCart"));
        shippingCostValue = new BigDecimal(super.properties.getProperty("shippingCostValue"));
        numberOfSlidesInCarousel = Integer.valueOf(super.properties.getProperty("numberOfSlidesInCarousel"));
        minNumberOfThumbnailsForProduct = Integer
                .valueOf(super.properties.getProperty("minNumberOfThumbnailsForProduct"));

        navPipeLabel = new NavPipeLabels(properties);
    }
}
