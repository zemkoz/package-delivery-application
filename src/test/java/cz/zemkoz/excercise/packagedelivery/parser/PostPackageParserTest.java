package cz.zemkoz.excercise.packagedelivery.parser;

import cz.zemkoz.excercise.packagedelivery.domain.PostPackage;
import cz.zemkoz.excercise.packagedelivery.exception.ParseStringFailedException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PostPackageParserTest {
    private final StringParser<PostPackage> postPackageStringParser = new PostPackageParser();

    @Test
    public void testWeightIsDoubleAndValidPostCode_succeed() {
        String input = "1.234 80152";
        PostPackage postPackage = postPackageStringParser.parse(input);

        assertNotNull(postPackage);
        assertEquals("80152", postPackage.getPostcode());
        assertEquals(1.234D, postPackage.getPackageWeight());
    }

    @Test
    public void testValidWeightIsIntegerAndValidPostCode_succeed() {
        String input = "1 80152";
        PostPackage postPackage = postPackageStringParser.parse(input);

        assertNotNull(postPackage);
        assertEquals("80152", postPackage.getPostcode());
        assertEquals(1D, postPackage.getPackageWeight());
    }

    @Test
    public void testNullInput_throwsException() {
        String input = null;
        assertThrows(ParseStringFailedException.class, () -> postPackageStringParser.parse(input));
    }

    @Test
    public void testWeightAndPostCodeIsMissing_throwsException() {
        String input = " ";
        assertThrows(ParseStringFailedException.class, () -> postPackageStringParser.parse(input));
    }

    @Test
    public void testWeightValueIsMissing_throwsException() {
        String input = "80152";
        assertThrows(ParseStringFailedException.class, () -> postPackageStringParser.parse(input));
    }

    @Test
    public void testPostCodeIsMissing_throwsException() {
        String input = "1.234 ";
        assertThrows(ParseStringFailedException.class, () -> postPackageStringParser.parse(input));
    }

    @Test
    public void testNegativeWeight_throwsException() {
        String input = "-1.234 80152";
        assertThrows(ParseStringFailedException.class, () -> postPackageStringParser.parse(input));
    }

    @Test
    public void testWeightValueStartWithDot_throwsException() {
        String input = ".234 80152";
        assertThrows(ParseStringFailedException.class, () -> postPackageStringParser.parse(input));
    }

    @Test
    public void testWeightValueEndsWithDot_throwsException() {
        String input = "1. 80152";
        assertThrows(ParseStringFailedException.class, () -> postPackageStringParser.parse(input));
    }

    @Test
    public void testPostCodeIsTooShort_throwsException() {
        String input = "1.234 8015";
        assertThrows(ParseStringFailedException.class, () -> postPackageStringParser.parse(input));
    }

    @Test
    public void testPostCodeIsTooLong_throwsException() {
        String input = "1.234 8015";
        assertThrows(ParseStringFailedException.class, () -> postPackageStringParser.parse(input));
    }
}
