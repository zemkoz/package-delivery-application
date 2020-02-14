package cz.zemkoz.excercise.packagedelivery.service;

import cz.zemkoz.excercise.packagedelivery.domain.Post;

import java.util.List;
import java.util.TimerTask;

public final class PostPrinterTimerTask extends TimerTask {
    private final PostService postService;

    public PostPrinterTimerTask(PostService postService) {
        this.postService = postService;
    }

    @Override
    public void run() {
        List<Post> postList = postService.getPostList();
        System.out.println("Posts:");
        for (Post currentPost : postList) {
            System.out.printf("%s %.3f\n", currentPost.getPostcode(), currentPost.getTotalWeight());
        }
    }
}
