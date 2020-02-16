package cz.zemkoz.excercise.packagedelivery.service;

import cz.zemkoz.excercise.packagedelivery.TestUtility;
import cz.zemkoz.excercise.packagedelivery.domain.PriceListItem;
import cz.zemkoz.excercise.packagedelivery.parser.StringParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DeliveryPriceListService_getPriceByWeightTest {
    @Mock StringParser<PriceListItem> mockPriceListItemStringParser;

    private DeliveryPriceListService deliveryPriceListService;

    @BeforeEach
    public void setUp() throws IOException {
        deliveryPriceListService = new DeliveryPriceListServiceImpl(mockPriceListItemStringParser);
    }

    @Test
    public void testWhenPriceListIsAvailable() throws IOException {
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

        File inputPriceListFile = TestUtility.createTempFile("price-list-test", fileContent);

        when(mockPriceListItemStringParser.parse(firstLine)).thenReturn(priceListItem1);
        when(mockPriceListItemStringParser.parse(secondLine)).thenReturn(priceListItem2);
        when(mockPriceListItemStringParser.parse(thirdLine)).thenReturn(priceListItem3);

        deliveryPriceListService.loadPriceListFromFile(inputPriceListFile.toPath());

        // Test
        Optional<BigDecimal> actualPrice1 = deliveryPriceListService.getPriceByWeight(10.5D);
        Optional<BigDecimal> actualPrice2 = deliveryPriceListService.getPriceByWeight(10D);
        Optional<BigDecimal> actualPrice3 = deliveryPriceListService.getPriceByWeight(9.999D);
        Optional<BigDecimal> actualPrice4 = deliveryPriceListService.getPriceByWeight(5D);
        Optional<BigDecimal> actualPrice5 = deliveryPriceListService.getPriceByWeight(4.999D);
        Optional<BigDecimal> actualPrice6 = deliveryPriceListService.getPriceByWeight(2.5D);
        Optional<BigDecimal> actualPrice7 = deliveryPriceListService.getPriceByWeight(2.499D);

        // Verify
        assertTrue(actualPrice1.isPresent());
        assertEquals(priceListItem1.getPrice(), actualPrice1.get());

        assertTrue(actualPrice2.isPresent());
        assertEquals(priceListItem1.getPrice(), actualPrice2.get());

        assertTrue(actualPrice3.isPresent());
        assertEquals(priceListItem2.getPrice(), actualPrice3.get());

        assertTrue(actualPrice4.isPresent());
        assertEquals(priceListItem2.getPrice(), actualPrice4.get());

        assertTrue(actualPrice5.isPresent());
        assertEquals(priceListItem3.getPrice(), actualPrice5.get());

        assertTrue(actualPrice6.isPresent());
        assertEquals(priceListItem3.getPrice(), actualPrice6.get());

        assertFalse(actualPrice7.isPresent());
    }

    @Test
    public void testWhenPriceListIsEmpty() {
        Optional<BigDecimal> actualPrice = deliveryPriceListService.getPriceByWeight(10D);
        assertFalse(actualPrice.isPresent());
    }
}
