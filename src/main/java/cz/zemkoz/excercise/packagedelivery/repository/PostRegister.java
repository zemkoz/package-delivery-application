package cz.zemkoz.excercise.packagedelivery.repository;

import cz.zemkoz.excercise.packagedelivery.domain.Post;
import cz.zemkoz.excercise.packagedelivery.domain.PostPackage;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The implementation is thread safe because will be used by two threads.
 */
public final class PostRegister implements PostDao {
    private final Map<String, Post> postMap = new HashMap<>();

    @Override
    public synchronized void deliverPackage(PostPackage postPackage) {
        deliverPackage(postPackage, null);
    }

    @Override
    public synchronized void deliverPackage(PostPackage postPackage, BigDecimal deliveryPrice) {
        String targetPostcode = postPackage.getPostcode();
        double packageWeight = postPackage.getPackageWeight();

        Post foundPost = postMap.get(targetPostcode);
        if (foundPost == null) {
            foundPost = new Post(targetPostcode);
            postMap.put(targetPostcode, foundPost);
        }
        foundPost.addWeight(packageWeight);
        if(deliveryPrice != null) {
            foundPost.addDeliveryFee(deliveryPrice);
        }
    }

    @Override
    public synchronized List<Post> findAllPosts() {
        return new ArrayList<>(postMap.values());
    }
}
