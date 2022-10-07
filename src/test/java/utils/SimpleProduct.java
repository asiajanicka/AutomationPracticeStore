package utils;

import lombok.AllArgsConstructor;
import pageObjects.base.Product;

import java.math.BigDecimal;
import java.util.Objects;


@AllArgsConstructor
public class SimpleProduct {
    private int id;
    private String name;
   private BigDecimal price;
    private String color;

    public SimpleProduct(Product product){
        this.id = product.getId();
        this.name = product.getName();
        this.price = new BigDecimal(product.getPrice().toString()).setScale(2);
        this.color = product.getColor().toLowerCase() ;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SimpleProduct)) return false;
        SimpleProduct that = (SimpleProduct) o;
        return id == that.id && name.equals(that.name) && price.equals(that.price) && color.equalsIgnoreCase(that.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, color);
    }

    @Override
    public String toString() {
        return "SimpleProduct{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", color='" + color + '\'' +
                '}';
    }
}
