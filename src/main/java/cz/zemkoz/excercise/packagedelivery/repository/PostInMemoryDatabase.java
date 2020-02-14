package cz.zemkoz.excercise.packagedelivery.repository;

import cz.zemkoz.excercise.packagedelivery.domain.Post;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostInMemoryDatabase implements PostDao {
    private final Map<String, Post> postMap = new HashMap<>();

    public PostInMemoryDatabase() {
        Post post1 = new Post("94072");
        Post post2 = new Post("68000");

        postMap.put(post1.getPostcode(), post1);
        postMap.put(post2.getPostcode(), post2);
    }

    @Override
    public List<Post> findAllPosts() {
        return new ArrayList<>(postMap.values());
    }
}
