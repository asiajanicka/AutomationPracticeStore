package pageObjects.base;

import com.opencsv.bean.CsvBindByName;
import enums.Stock;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
public class Product{
    @CsvBindByName(column = "id")
    private int id;
    @CsvBindByName(column = "name")
    private String name;
    @CsvBindByName(column = "category")
    private String category;
    @CsvBindByName(column = "1st_level_subcategory")
    private String subcategory1stLevel;
    @CsvBindByName(column = "2nd_level_subcategory")
    private String subcategory2ndLevel;
    @CsvBindByName(column = "reference")
    private String reference;
    @CsvBindByName(column = "condition")
    private String condition;
    @CsvBindByName(column = "color")
    private String color;
    @CsvBindByName(column = "availability")
    private String availability;
    @CsvBindByName(column = "sizes")
    private String sizes;
    @CsvBindByName(column = "price")
    private String price;
    @CsvBindByName(column = "old_price")
    private String oldPrice;
    @CsvBindByName(column = "price_reduction")
    private String priceReduction;
    @CsvBindByName(column = "compositions")
    private String compositions;
    @CsvBindByName(column = "styles")
    private String styles;
    @CsvBindByName(column = "properties")
    private String properties;

    public Product() {

    }

    public BigDecimal getPrice(){
        return new BigDecimal(price);
    }

    public BigDecimal getOldPrice(){
        if(this.oldPrice!=null &&!this.oldPrice.isBlank()){
            return  new BigDecimal(oldPrice);
        } else
        return BigDecimal.ZERO;
    }

    public Stock getAvailability(){
        return Stock.valueOf(availability);
    }


    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", subcategory1stLevel='" + subcategory1stLevel + '\'' +
                ", subcategory2ndLevel='" + subcategory2ndLevel + '\'' +
                ", reference='" + reference + '\'' +
                ", availability='" + availability + '\'' +
                ", price='" + price + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return reference.equals(product.reference);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reference);
    }
}
