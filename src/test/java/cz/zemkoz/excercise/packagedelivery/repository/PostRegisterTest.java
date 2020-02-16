package cz.zemkoz.excercise.packagedelivery.repository;

import cz.zemkoz.excercise.packagedelivery.domain.Post;
import cz.zemkoz.excercise.packagedelivery.domain.PostPackage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class PostRegisterTest {
    private PostDao postDao;

    @BeforeEach
    public void setUp() {
        postDao = new PostRegister();
    }

    @Test
    public void testDeliverPackageToPost() {
        PostPackage postPackage = new PostPackage("94072", 1.578D);
        postDao.deliverPackage(postPackage);

        List<Post> actualPostList = postDao.findAllPosts();
        assertNotNull(actualPostList);
        assertEquals(1, actualPostList.size());

        Post actualPost = actualPostList.get(0);
        assertEquals(postPackage.getPostcode(), actualPost.getPostcode());
        assertEquals(postPackage.getPackageWeight(), actualPost.getTotalWeight());
    }

    @Test
    public void testDeliverTwoPackageToSamePost() {
        PostPackage postPackage1 = new PostPackage("94072", 1.578D);
        PostPackage postPackage2 = new PostPackage("94072", 4.578D);

        postDao.deliverPackage(postPackage1);
        postDao.deliverPackage(postPackage2);

        List<Post> actualPostList = postDao.findAllPosts();
        assertNotNull(actualPostList);
        assertEquals(1, actualPostList.size());

        Post actualPost = actualPostList.get(0);
        assertEquals(postPackage1.getPostcode(), actualPost.getPostcode());
        double expectedTotalWeight = postPackage1.getPackageWeight() + postPackage2.getPackageWeight();
        assertEquals(expectedTotalWeight, actualPost.getTotalWeight());
    }

    @Test
    public void testDeliverTwoPackageToFirstPostAndOneToSecondPost() {
        String postcode1 = "94072";
        String postcode2 = "68021";

        PostPackage postPackage1 = new PostPackage(postcode1, 1.578D);
        PostPackage postPackage2 = new PostPackage(postcode2, 12.000D);
        PostPackage postPackage3 = new PostPackage(postcode1, 4.530D);

        postDao.deliverPackage(postPackage1);
        postDao.deliverPackage(postPackage2);
        postDao.deliverPackage(postPackage3);

        // Verify
        List<Post> actualPostList = postDao.findAllPosts();
        assertNotNull(actualPostList);
        assertEquals(2, actualPostList.size());

        Map<String, Post> actualPostMap = actualPostList.stream()
                .collect(Collectors.toMap(Post::getPostcode, post -> post));

        Post actualPost1 = actualPostMap.get(postcode1);
        assertNotNull(actualPost1);
        assertEquals(postcode1, actualPost1.getPostcode());
        double expectedTotalWeight1 = postPackage1.getPackageWeight() + postPackage3.getPackageWeight();
        assertEquals(expectedTotalWeight1, actualPost1.getTotalWeight());

        Post actualPost2 = actualPostMap.get(postcode2);
        assertNotNull(actualPost2);
        assertEquals(postcode2, actualPost2.getPostcode());
        double expectedTotalWeight2 = postPackage2.getPackageWeight();
        assertEquals(expectedTotalWeight2, actualPost2.getTotalWeight());
    }

    @Test
    public void testDeliverPackageToPostWithDeliveryFee() {
        // Given
        PostPackage postPackage = new PostPackage("94072", 1.578D);
        BigDecimal deliveryFee = new BigDecimal(10);

        // Test
        postDao.deliverPackage(postPackage, deliveryFee);

        // Verify
        List<Post> actualPostList = postDao.findAllPosts();
        assertNotNull(actualPostList);
        assertEquals(1, actualPostList.size());

        Post actualPost = actualPostList.get(0);
        assertEquals(postPackage.getPostcode(), actualPost.getPostcode());
        assertEquals(postPackage.getPackageWeight(), actualPost.getTotalWeight());

        assertTrue(actualPost.getTotalDeliveryFee().isPresent());
        assertEquals(deliveryFee, actualPost.getTotalDeliveryFee().get());
    }

    @Test
    public void testDeliverTwoPackageToSamePostWithDeliveryFee() {
        // Given
        PostPackage postPackage1 = new PostPackage("94072", 1.578D);
        BigDecimal deliveryFee1 = new BigDecimal(10);

        PostPackage postPackage2 = new PostPackage("94072", 4.578D);
        BigDecimal deliveryFee2 = new BigDecimal(20);

        // Test
        postDao.deliverPackage(postPackage1, deliveryFee1);
        postDao.deliverPackage(postPackage2, deliveryFee2);

        // Verify
        List<Post> actualPostList = postDao.findAllPosts();
        assertNotNull(actualPostList);
        assertEquals(1, actualPostList.size());

        Post actualPost = actualPostList.get(0);
        assertEquals(postPackage1.getPostcode(), actualPost.getPostcode());
        double expectedTotalWeight = postPackage1.getPackageWeight() + postPackage2.getPackageWeight();
        assertEquals(expectedTotalWeight, actualPost.getTotalWeight());

        assertTrue(actualPost.getTotalDeliveryFee().isPresent());
        assertEquals(new BigDecimal(30), actualPost.getTotalDeliveryFee().get());
    }
}
