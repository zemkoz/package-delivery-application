package cz.zemkoz.excercise.packagedelivery.parser;

import cz.zemkoz.excercise.packagedelivery.domain.PriceListItem;
import cz.zemkoz.excercise.packagedelivery.exception.ParseStringFailedException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class PriceListParserTest {
    private final StringParser<PriceListItem> priceListItemParser = new PriceListItemParser();

    @Test
    public void testWeightAndPriceAreDecimalNumbers() {
        String input = "3.50 2.7";
        PriceListItem priceListItem = priceListItemParser.parse(input);

        assertNotNull(priceListItem);
        assertEquals(3.5D, priceListItem.getWeight());
        assertEquals(new BigDecimal("2.7"), priceListItem.getPrice());
    }

    @Test
    public void testValidWeightIsIntegerAndPriceIsDecimalNumber() {
        String input = "3 2.7";
        PriceListItem priceListItem = priceListItemParser.parse(input);

        assertNotNull(priceListItem);
        assertEquals(3D, priceListItem.getWeight());
        assertEquals(new BigDecimal("2.7"), priceListItem.getPrice());
    }

    @Test
    public void testNullInput_throwsException() {
        String input = null;
        assertThrows(ParseStringFailedException.class, () -> priceListItemParser.parse(input));
    }

    @Test
    public void testWeightAndPriceValueAreMissing_throwsException() {
        String input = " ";
        assertThrows(ParseStringFailedException.class, () -> priceListItemParser.parse(input));
    }

    @Test
    public void testOnlyPriceValueIsMissing_throwsException() {
        String input = "3.5 ";
        assertThrows(ParseStringFailedException.class, () -> priceListItemParser.parse(input));
    }

    @Test
    public void testWeightValueIsMissing_throwsException() {
        String input = " 2.7";
        assertThrows(ParseStringFailedException.class, () -> priceListItemParser.parse(input));
    }

    @Test
    public void testNegativeWeightValue_throwsException() {
        String input = "-3.50 2.7";
        assertThrows(ParseStringFailedException.class, () -> priceListItemParser.parse(input));
    }

    @Test
    public void testNegativePriceValue_throwsException() {
        String input = "3.50 -2.7";
        assertThrows(ParseStringFailedException.class, () -> priceListItemParser.parse(input));
    }

    @Test
    public void testWeightValueStartWithDot_throwsException() {
        String input = ".50 2.7";
        assertThrows(ParseStringFailedException.class, () -> priceListItemParser.parse(input));
    }

    @Test
    public void testWeightValueEndsWithDot_throwsException() {
        String input = "3. 2.7";
        assertThrows(ParseStringFailedException.class, () -> priceListItemParser.parse(input));
    }

    @Test
    public void testPriceValueStartWithDot_throwsException() {
        String input = "3.50 .7";
        assertThrows(ParseStringFailedException.class, () -> priceListItemParser.parse(input));
    }

    @Test
    public void testPriceValueEndsWithDot_throwsException() {
        String input = "3.5 2.";
        assertThrows(ParseStringFailedException.class, () -> priceListItemParser.parse(input));
    }
}
