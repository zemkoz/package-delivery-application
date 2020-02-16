package cz.zemkoz.excercise.packagedelivery.service;

import cz.zemkoz.excercise.packagedelivery.domain.Post;
import cz.zemkoz.excercise.packagedelivery.domain.PostPackage;
import cz.zemkoz.excercise.packagedelivery.parser.StringParser;
import cz.zemkoz.excercise.packagedelivery.repository.PostDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostServiceImpl_getPostListTest {
    @Mock
    StringParser<PostPackage> mockPackageStringParser;
    @Mock PostDao mockPostDao;
    @Mock DeliveryPriceListService mockDeliveryPriceListService;

    private PostService postService;

    @BeforeEach
    public void setUp() {
        postService = new PostServiceImpl(mockPackageStringParser, mockPostDao, mockDeliveryPriceListService);
    }

    @Test
    public void testNoPostsHere_returnsEmptyList() {
        when(mockPostDao.findAllPosts()).thenReturn(new ArrayList<>());

        List<Post> actualPostList = postService.getPostList();

        verify(mockPostDao).findAllPosts();
        assertNotNull(actualPostList);
        assertTrue(actualPostList.isEmpty());
    }

    @Test
    public void testOnePostIsHere_returnsListWithOnePost() {
        Post post = new Post("08801", 15.960D);
        when(mockPostDao.findAllPosts()).thenReturn(Collections.singletonList(post));

        List<Post> actualPostList = postService.getPostList();

        verify(mockPostDao).findAllPosts();
        assertNotNull(actualPostList);
        assertEquals(1, actualPostList.size());
        assertEquals(post, actualPostList.get(0));
    }

    @Test
    public void testThreePostIsHere_returnsListWithThreePostOrderedByTotalWeight() {
        Post post1 = new Post("08801", 15.960D);
        Post post2 = new Post("97101", 24.778D);
        Post post3 = new Post("55501", 7.718D);
        when(mockPostDao.findAllPosts()).thenReturn(Arrays.asList(post1, post2, post3));

        List<Post> actualPostList = postService.getPostList();

        verify(mockPostDao).findAllPosts();
        assertNotNull(actualPostList);
        assertEquals(3, actualPostList.size());
        assertEquals(post2, actualPostList.get(0));
        assertEquals(post1, actualPostList.get(1));
        assertEquals(post3, actualPostList.get(2));
    }
}
