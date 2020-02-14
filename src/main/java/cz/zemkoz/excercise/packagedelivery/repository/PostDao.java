package cz.zemkoz.excercise.packagedelivery.repository;

import cz.zemkoz.excercise.packagedelivery.domain.Post;
import cz.zemkoz.excercise.packagedelivery.domain.PostPackage;

import java.util.List;

public interface PostDao {
    void deliverPackage(PostPackage postPackage);

    List<Post> findAllPosts();
}
