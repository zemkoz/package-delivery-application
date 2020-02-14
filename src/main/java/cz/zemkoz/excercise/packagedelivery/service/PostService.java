package cz.zemkoz.excercise.packagedelivery.service;

import cz.zemkoz.excercise.packagedelivery.domain.Post;

import java.nio.file.Path;
import java.util.List;

public interface PostService {
    void processPostPackagesFromTextFile(Path path);

    void processPostPackageFromString(String stringPackage);

    List<Post> getPostList();
}
