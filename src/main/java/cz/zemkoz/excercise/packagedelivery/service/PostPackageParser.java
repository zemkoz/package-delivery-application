package cz.zemkoz.excercise.packagedelivery.service;

import cz.zemkoz.excercise.packagedelivery.domain.PostPackage;
import cz.zemkoz.excercise.packagedelivery.exception.ParseStringFailedException;

public final class PostPackageParser implements StringParser<PostPackage> {
    private static final String PACKAGE_INPUT_REGEX = "^\\d+(.\\d+)?\\s\\d{5}$";

    public PostPackage parse(String input) {
        if (input == null || !input.matches(PACKAGE_INPUT_REGEX)) {
            throw new ParseStringFailedException();
        }
        String[] fields = input.split(" ");
        double weight = Double.parseDouble(fields[0]);
        String postalCode = fields[1];
        return new PostPackage(postalCode, weight);
    }
}
