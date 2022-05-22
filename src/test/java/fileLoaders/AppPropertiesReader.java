package fileLoaders;

import lombok.Getter;
import utils.NavPipeLabels;

import java.math.BigDecimal;
import java.util.Currency;

@Getter
public class AppPropertiesReader extends PropertiesReader {

    private int numberOfProductsInBestSellers;
    private int numberOfDiscountedProductsInBestSellers;
    private String currency;
    private int lengthOfProductNameLabelInCart;
    private BigDecimal shippingCostValue;
    private int numberOfSlidesInCarousel;
    private int minNumberOfThumbnailsForProduct;
    private String bestSellersTabLabel;
    private String popularItemsTabLabel;
    private String bestSellersTabContentId;
    private String popularItemsTabContentId;

    private NavPipeLabels navPipeLabel;

    public AppPropertiesReader() {
        super("app.properties");
    }

    void loadData() {
        numberOfProductsInBestSellers = Integer
                .parseInt(properties.getProperty("numberOfProductsInBestSellers"));
        numberOfDiscountedProductsInBestSellers = Integer
                .parseInt(properties.getProperty("numberOfDiscountedProductsInBestSellers"));
        currency = Currency.getInstance(properties.getProperty("currency")).getSymbol();
        lengthOfProductNameLabelInCart = Integer.parseInt(properties.getProperty("lengthOfProductNameLabelInCart"));
        shippingCostValue = new BigDecimal(properties.getProperty("shippingCostValue"));
        numberOfSlidesInCarousel = Integer.parseInt(properties.getProperty("numberOfSlidesInCarousel"));
        minNumberOfThumbnailsForProduct = Integer
                .parseInt(properties.getProperty("minNumberOfThumbnailsForProduct"));

        navPipeLabel = new NavPipeLabels(properties);
        bestSellersTabLabel = properties.getProperty("home.bestSellersLabel");
        popularItemsTabLabel = properties.getProperty("home.popularItemsLabel");
        bestSellersTabContentId = properties.getProperty("home.bestSellersId");
        popularItemsTabContentId = properties.getProperty("home.popularItemsId");
    }
}
