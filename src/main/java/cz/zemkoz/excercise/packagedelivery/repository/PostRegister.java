package cz.zemkoz.excercise.packagedelivery.repository;

import cz.zemkoz.excercise.packagedelivery.domain.Post;
import cz.zemkoz.excercise.packagedelivery.domain.PostPackage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class PostRegister implements PostDao {
    private final Map<String, Post> postMap = new HashMap<>();

    @Override
    public synchronized void deliverPackage(PostPackage postPackage) {
        String targetPostcode = postPackage.getPostcode();
        double packageWeight = postPackage.getPackageWeight();

        Post foundPost = postMap.get(targetPostcode);
        if (foundPost == null) {
            foundPost = new Post(targetPostcode);
            postMap.put(targetPostcode, foundPost);
        }
        foundPost.setTotalWeight(foundPost.getTotalWeight() + packageWeight);
    }

    @Override
    public synchronized List<Post> findAllPosts() {
        return new ArrayList<>(postMap.values());
    }
}
