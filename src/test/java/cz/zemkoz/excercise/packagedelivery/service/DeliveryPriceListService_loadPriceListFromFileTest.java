package cz.zemkoz.excercise.packagedelivery.service;

import cz.zemkoz.excercise.packagedelivery.TestUtility;
import cz.zemkoz.excercise.packagedelivery.domain.PriceListItem;
import cz.zemkoz.excercise.packagedelivery.exception.LoadPriceListFailedException;
import cz.zemkoz.excercise.packagedelivery.exception.ParseStringFailedException;
import cz.zemkoz.excercise.packagedelivery.parser.StringParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeliveryPriceListService_loadPriceListFromFileTest {
    @Mock StringParser<PriceListItem> mockPriceListItemStringParser;

    private DeliveryPriceListService deliveryPriceListService;

    @BeforeEach
    public void setUp() {
        deliveryPriceListService = new DeliveryPriceListServiceImpl(mockPriceListItemStringParser);
    }

    @Test
    public void testWhenInputHasValidData() throws IOException {
        // Given
        PriceListItem priceListItem1 = new PriceListItem(10D, new BigDecimal("5"));
        PriceListItem priceListItem2 = new PriceListItem(5D, new BigDecimal("2.5"));
        PriceListItem priceListItem3 = new PriceListItem(2.5D, new BigDecimal("1.5"));

        String firstLine = "10 5.00";
        String secondLine = "5 2.50";
        String thirdLine = "2.5 1.5";

        String fileContent = firstLine + "\n" +
                secondLine + "\n" +
                thirdLine + "\n";

        final File tempFile = TestUtility.createTempFile("price-list-test", fileContent);

        when(mockPriceListItemStringParser.parse(firstLine)).thenReturn(priceListItem1);
        when(mockPriceListItemStringParser.parse(secondLine)).thenReturn(priceListItem2);
        when(mockPriceListItemStringParser.parse(thirdLine)).thenReturn(priceListItem3);

        // Test
        deliveryPriceListService.loadPriceListFromFile(tempFile.toPath());

        // Verify
        verify(mockPriceListItemStringParser).parse(firstLine);
        verify(mockPriceListItemStringParser).parse(secondLine);
        verify(mockPriceListItemStringParser).parse(thirdLine);
    }

    @Test
    public void testWhenInputFileDoesNotExists_throwsLoadPriceListFailedException() {
        File inputFile = new File("NonExistingFile.txt");
        assertThrows(LoadPriceListFailedException.class, () -> deliveryPriceListService.loadPriceListFromFile(inputFile.toPath()));

        verify(mockPriceListItemStringParser, never()).parse(anyString());
    }

    @Test
    public void testWhenParserCantParseSecondLine_throwsLoadPriceListFailedException() throws IOException {
        // Given
        PriceListItem priceListItem1 = new PriceListItem(10D, new BigDecimal("5"));

        String firstLine = "10 5.00";
        String secondLine = "InvalidEntry";
        String thirdLine = "2.5 1.5";

        String fileContent = firstLine + "\n" +
                secondLine + "\n" +
                thirdLine + "\n";

        final File tempFile = TestUtility.createTempFile("price-list-test", fileContent);

        when(mockPriceListItemStringParser.parse(firstLine)).thenReturn(priceListItem1);
        when(mockPriceListItemStringParser.parse(secondLine)).thenThrow(new ParseStringFailedException());

        // Test
        assertThrows(LoadPriceListFailedException.class, () -> deliveryPriceListService.loadPriceListFromFile(tempFile.toPath()));

        // Verify
        verify(mockPriceListItemStringParser).parse(firstLine);
        verify(mockPriceListItemStringParser).parse(secondLine);
        verify(mockPriceListItemStringParser, never()).parse(thirdLine);
    }
}
