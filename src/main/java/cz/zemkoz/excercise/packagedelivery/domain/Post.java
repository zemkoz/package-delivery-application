package cz.zemkoz.excercise.packagedelivery.domain;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

public final class Post {
    private final String postcode;
    private double totalWeight;
    private BigDecimal totalDeliveryFee;

    public Post(String postcode) {
        this(postcode, 0D, null);
    }

    public Post(String postcode, double totalWeight) {
        this(postcode, totalWeight, null);
    }

    public Post(String postcode, double totalWeight, BigDecimal totalDeliveryFee) {
        this.postcode = postcode;
        this.totalWeight = totalWeight;
        this.totalDeliveryFee = totalDeliveryFee;
    }

    public String getPostcode() {
        return postcode;
    }

    public double getTotalWeight() {
        return totalWeight;
    }

    public Optional<BigDecimal> getTotalDeliveryFee() {
        return Optional.ofNullable(totalDeliveryFee);
    }

    public void addWeight(double weight) {
        this.totalWeight += weight;
    }

    public void addDeliveryFee(BigDecimal deliveryFee) {
        this.totalDeliveryFee = this.totalDeliveryFee == null
                ? deliveryFee
                : this.totalDeliveryFee.add(deliveryFee);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return Objects.equals(postcode, post.postcode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postcode);
    }

    @Override
    public String toString() {
        return "Post{" +
                "postcode='" + postcode + '\'' +
                ", packageWeightSum=" + totalWeight +
                '}';
    }
}
