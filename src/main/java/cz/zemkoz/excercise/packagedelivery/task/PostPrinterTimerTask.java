package cz.zemkoz.excercise.packagedelivery.task;

import cz.zemkoz.excercise.packagedelivery.domain.Post;
import cz.zemkoz.excercise.packagedelivery.service.PostService;

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
            System.out.printf("%s %.3f", currentPost.getPostcode(), currentPost.getTotalWeight());
            if (currentPost.getTotalDeliveryFee().isPresent()) {
                System.out.printf(" %.3f", currentPost.getTotalDeliveryFee().get());
            }
            System.out.println();
        }
    }
}
