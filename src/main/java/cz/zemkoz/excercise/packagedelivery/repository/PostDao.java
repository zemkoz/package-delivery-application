package cz.zemkoz.excercise.packagedelivery.repository;

import cz.zemkoz.excercise.packagedelivery.domain.Post;

import java.util.List;

public interface PostDao {
    List<Post> findAllPosts();
}
