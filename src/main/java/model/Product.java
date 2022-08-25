package model;

import enums.Stock;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class Product {
    private String name;
    private String category;
    private String subcategory1stLevel;
    private String subcategory2ndLevel;
    private String reference;
    private String condition;
    private String color;
    private Stock availability;
    private String sizes;
    private BigDecimal price;
    private BigDecimal oldPrice;
    private String priceReduction;
    private String compositions;
    private String styles;
    private String properties;

    public Product(String name, String category, String subcategory1stLevel, String subcategory2ndLevel,
                   String reference, String condition, String color, Stock availability, String sizes,
                   BigDecimal price, BigDecimal oldPrice, String priceReduction, String compositions,
                   String styles, String properties) {
        this.name = name;
        this.category = category;
        this.subcategory1stLevel = subcategory1stLevel;
        this.subcategory2ndLevel = subcategory2ndLevel;
        this.reference = reference;
        this.condition = condition;
        this.color = color;
        this.availability = availability;
        this.sizes = sizes;
        this.price = price;
        this.oldPrice = oldPrice;
        this.priceReduction = priceReduction;
        this.compositions = compositions;
        this.styles = styles;
        this.properties = properties;
    }

    @Override
    public String toString() {
        return String.format("Name: %s, category: %s, sub1stLevel: %s, sub2ndLevel: %s, availability: %s, old price: %s",
                name, category, subcategory1stLevel, subcategory2ndLevel, availability.getValue(), oldPrice.toString());
    }
}
