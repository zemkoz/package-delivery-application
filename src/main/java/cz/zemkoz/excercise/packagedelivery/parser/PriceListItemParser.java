package cz.zemkoz.excercise.packagedelivery.parser;

import cz.zemkoz.excercise.packagedelivery.domain.PriceListItem;
import cz.zemkoz.excercise.packagedelivery.exception.ParseStringFailedException;

import java.math.BigDecimal;

public class PriceListItemParser implements StringParser<PriceListItem> {
    private static final String PACKAGE_INPUT_REGEX = "^\\d+(.\\d+)?\\s\\d+(.\\d+)?$";

    @Override
    public PriceListItem parse(String input) {
        if (input == null || !input.matches(PACKAGE_INPUT_REGEX)) {
            throw new ParseStringFailedException();
        }
        String[] fields = input.split(" ");
        double weight = Double.parseDouble(fields[0]);
        BigDecimal price = new BigDecimal(fields[1]);
        return new PriceListItem(weight, price);
    }
}
