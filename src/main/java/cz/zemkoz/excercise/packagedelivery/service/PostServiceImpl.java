package cz.zemkoz.excercise.packagedelivery.service;

import cz.zemkoz.excercise.packagedelivery.domain.Post;
import cz.zemkoz.excercise.packagedelivery.domain.PostPackage;
import cz.zemkoz.excercise.packagedelivery.repository.PostDao;

import java.util.List;

public final class PostServiceImpl implements PostService {
    private final StringParser<PostPackage> packageStringParser;
    private final PostDao postDao;

    public PostServiceImpl(StringParser<PostPackage> packageStringParser, PostDao postDao) {
        this.packageStringParser = packageStringParser;
        this.postDao = postDao;
    }

    public void processPostPackageFromString(String stringPackage) {
        PostPackage postPackage = packageStringParser.parse(stringPackage);
        System.out.println("Parsed Package: " + postPackage);
    }

    @Override
    public List<Post> getPostList() {
        return postDao.findAllPosts();
    }
}
