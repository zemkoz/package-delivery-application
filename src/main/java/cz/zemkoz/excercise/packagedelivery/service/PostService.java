package cz.zemkoz.excercise.packagedelivery.service;

import cz.zemkoz.excercise.packagedelivery.domain.Post;

import java.util.List;

public interface PostService {
    void processPostPackageFromString(String stringPackage);

    List<Post> getPostList();
}
