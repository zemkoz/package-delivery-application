package cz.zemkoz.excercise.packagedelivery.domain;

import java.math.BigDecimal;
import java.util.Objects;

public final class PriceListItem {
    private double weight;
    private BigDecimal price;

    public PriceListItem(double weight, BigDecimal price) {
        this.weight = weight;
        this.price = price;
    }

    public double getWeight() {
        return weight;
    }

    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PriceListItem that = (PriceListItem) o;
        return Double.compare(that.weight, weight) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(weight);
    }

    @Override
    public String toString() {
        return "PriceListValue{" +
                "weight=" + weight +
                ", price=" + price +
                '}';
    }
}
