package cz.zemkoz.excercise.packagedelivery.service;

import cz.zemkoz.excercise.packagedelivery.domain.Post;
import cz.zemkoz.excercise.packagedelivery.domain.PostPackage;
import cz.zemkoz.excercise.packagedelivery.exception.LoadPostPackagesFailedException;
import cz.zemkoz.excercise.packagedelivery.exception.ParseStringFailedException;
import cz.zemkoz.excercise.packagedelivery.parser.StringParser;
import cz.zemkoz.excercise.packagedelivery.repository.PostDao;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public final class PostServiceImpl implements PostService {
    private final StringParser<PostPackage> packageStringParser;
    private final PostDao postDao;
    private final DeliveryPriceListService priceListService;

    public PostServiceImpl(StringParser<PostPackage> packageStringParser,
                           PostDao postDao,
                           DeliveryPriceListService deliveryPriceListService) {
        this.packageStringParser = packageStringParser;
        this.postDao = postDao;
        this.priceListService = deliveryPriceListService;
    }

    @Override
    public void processPostPackagesFromTextFile(Path path) {
        try {
            Files.readAllLines(path).forEach(this::processPostPackageFromString);
        } catch (IOException | ParseStringFailedException e) {
            throw new LoadPostPackagesFailedException("Read post packages from file on path=\"" + path + "\" failed.");
        }
    }

    @Override
    public void processPostPackageFromString(String stringPackage) {
        PostPackage postPackage = packageStringParser.parse(stringPackage);
        Optional<BigDecimal> foundPrice = priceListService.getPriceByWeight(postPackage.getPackageWeight());
        if (foundPrice.isPresent()) {
            postDao.deliverPackage(postPackage, foundPrice.get());
        } else {
            postDao.deliverPackage(postPackage);
        }
    }

    @Override
    public List<Post> getPostList() {
        List<Post> postList = postDao.findAllPosts();
        postList.sort(Comparator.comparingDouble(Post::getTotalWeight).reversed());
        return postList;
    }
}
