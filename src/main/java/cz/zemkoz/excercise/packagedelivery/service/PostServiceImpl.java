package cz.zemkoz.excercise.packagedelivery.service;

import cz.zemkoz.excercise.packagedelivery.domain.PostPackage;

public final class PostServiceImpl implements PostService {
    private final StringParser<PostPackage> packageStringParser;

    public PostServiceImpl(StringParser<PostPackage> packageStringParser) {
        this.packageStringParser = packageStringParser;
    }

    public void processPostPackageFromString(String stringPackage) {
        PostPackage postPackage = packageStringParser.parse(stringPackage);
        System.out.println("Parsed Package: " + postPackage);
    }
}
