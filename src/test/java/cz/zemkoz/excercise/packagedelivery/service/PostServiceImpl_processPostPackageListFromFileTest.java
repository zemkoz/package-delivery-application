package cz.zemkoz.excercise.packagedelivery.service;

import cz.zemkoz.excercise.packagedelivery.TestUtility;
import cz.zemkoz.excercise.packagedelivery.domain.PostPackage;
import cz.zemkoz.excercise.packagedelivery.exception.LoadPostPackagesFailedException;
import cz.zemkoz.excercise.packagedelivery.exception.ParseStringFailedException;
import cz.zemkoz.excercise.packagedelivery.parser.StringParser;
import cz.zemkoz.excercise.packagedelivery.repository.PostDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PostServiceImpl_processPostPackageListFromFileTest {
    @Mock StringParser<PostPackage> mockPostPackageStringParser;
    @Mock PostDao mockPostDao;
    @Mock DeliveryPriceListService mockDeliveryPriceListService;

    private PostService postService;

    @BeforeEach
    public void setUp() {
        postService = new PostServiceImpl(mockPostPackageStringParser, mockPostDao, mockDeliveryPriceListService);
    }

    @Test
    public void testWhenInputFileHasValidValid() throws IOException {
        // Given
        PostPackage postPackage1 = new PostPackage("08801", 3.4D);
        PostPackage postPackage2 = new PostPackage("90005", 2D);
        PostPackage postPackage3 = new PostPackage("08801", 12.56D);

        String firstLine = "3.4 08801";
        String secondLine = "2 90005";
        String thirdLine = "12.56 08801";

        String fileContent = firstLine + "\n" +
                secondLine + "\n" +
                thirdLine + "\n";

        final File tempFile = TestUtility.createTempFile("post-packages-test", fileContent);

        when(mockPostPackageStringParser.parse(firstLine)).thenReturn(postPackage1);
        when(mockPostPackageStringParser.parse(secondLine)).thenReturn(postPackage2);
        when(mockPostPackageStringParser.parse(thirdLine)).thenReturn(postPackage3);
        when(mockDeliveryPriceListService.getPriceByWeight(anyDouble())).thenReturn(Optional.empty());

        // Test
        postService.processPostPackagesFromTextFile(tempFile.toPath());

        // Verify
        verify(mockPostPackageStringParser).parse(firstLine);
        verify(mockPostPackageStringParser).parse(secondLine);
        verify(mockPostPackageStringParser).parse(thirdLine);
        verify(mockDeliveryPriceListService, times(3)).getPriceByWeight(anyDouble());
        verify(mockPostDao).deliverPackage(postPackage1);
        verify(mockPostDao).deliverPackage(postPackage2);
        verify(mockPostDao).deliverPackage(postPackage3);
    }

    @Test
    public void testWhenInputFileDoesNotExists_throwsLoadPostPackagesFailedException() {
        File inputFile = new File("NonExistingFile.txt");
        assertThrows(LoadPostPackagesFailedException.class, () -> postService.processPostPackagesFromTextFile(inputFile.toPath()));

        verify(mockPostPackageStringParser, never()).parse(anyString());
    }

    @Test
    public void testWhenParserCantParseSecondLine_throwsLoadPriceListFailedException() throws IOException {
        // Given
        PostPackage postPackage1 = new PostPackage("08801", 3.4D);

        String firstLine = "3.4 08801";
        String secondLine = "InvalidLine";
        String thirdLine = "12.56 08801";

        String fileContent = firstLine + "\n" +
                secondLine + "\n" +
                thirdLine + "\n";

        final File tempFile = TestUtility.createTempFile("post-packages-test", fileContent);

        when(mockPostPackageStringParser.parse(firstLine)).thenReturn(postPackage1);
        when(mockPostPackageStringParser.parse(secondLine)).thenThrow(new ParseStringFailedException());
        when(mockDeliveryPriceListService.getPriceByWeight(anyDouble())).thenReturn(Optional.empty());

        // Test
        assertThrows(LoadPostPackagesFailedException.class, () -> postService.processPostPackagesFromTextFile(tempFile.toPath()));

        // Verify
        verify(mockPostPackageStringParser).parse(firstLine);
        verify(mockPostPackageStringParser).parse(secondLine);
        verify(mockPostPackageStringParser, never()).parse(thirdLine);
        verify(mockDeliveryPriceListService).getPriceByWeight(anyDouble());
        verify(mockPostDao).deliverPackage(postPackage1);
    }
}
