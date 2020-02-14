package cz.zemkoz.excercise.packagedelivery.service;

import cz.zemkoz.excercise.packagedelivery.domain.PostPackage;
import cz.zemkoz.excercise.packagedelivery.exception.ParseStringFailedException;
import cz.zemkoz.excercise.packagedelivery.repository.PostDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PostServiceImpl_processPostPackageFromStringTest {
    @Mock StringParser<PostPackage> mockPackageStringParser;
    @Mock PostDao mockPostDao;

    private PostService postService;

    @BeforeEach
    public void setUp() {
        postService = new PostServiceImpl(mockPackageStringParser, mockPostDao);
    }

    @Test
    public void testWhenInputIsValidString() {
        String inputString = "08801 15.960";

        PostPackage postPackage = new PostPackage("08801", 15.960D);
        when(mockPackageStringParser.parse(inputString)).thenReturn(postPackage);

        postService.processPostPackageFromString(inputString);

        verify(mockPackageStringParser).parse(any());
        verify(mockPostDao).deliverPackage(postPackage);
    }

    @Test
    public void testWhenInputIsInvalidString_throwsParseStringFailedException() {
        String inputString = "thisIsNotValidInput";
        when(mockPackageStringParser.parse(inputString)).thenThrow(ParseStringFailedException.class);

        assertThrows(ParseStringFailedException.class, () -> postService.processPostPackageFromString(inputString));

        verify(mockPackageStringParser).parse(any());
        verify(mockPostDao, never()).deliverPackage(any());
    }
}
