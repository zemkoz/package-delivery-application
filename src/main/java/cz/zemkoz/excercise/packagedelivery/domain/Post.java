package cz.zemkoz.excercise.packagedelivery.domain;

import java.util.Objects;

public final class Post {
    private final String postcode;
    private double packageWeightSum;

    public Post(String postcode) {
        this.postcode = postcode;
        packageWeightSum = 0D;
    }

    public String getPostcode() {
        return postcode;
    }

    public double getPackageWeightSum() {
        return packageWeightSum;
    }

    public void setPackageWeightSum(double packageWeightSum) {
        this.packageWeightSum = packageWeightSum;
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
                ", packageWeightSum=" + packageWeightSum +
                '}';
    }
}
