package cz.zemkoz.excercise.packagedelivery.domain;

import java.util.Objects;

public final class PostPackage {
    private final String postcode;
    private final double packageWeight;

    public PostPackage(String postcode, double packageWeight) {
        this.postcode = postcode;
        this.packageWeight = packageWeight;
    }

    public String getPostcode() {
        return postcode;
    }

    public double getPackageWeight() {
        return packageWeight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostPackage aPackage = (PostPackage) o;
        return Double.compare(aPackage.packageWeight, packageWeight) == 0 &&
                Objects.equals(postcode, aPackage.postcode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postcode, packageWeight);
    }

    @Override
    public String toString() {
        return "Package{" +
                "postcode='" + postcode + '\'' +
                ", packageWeight=" + packageWeight +
                '}';
    }
}
