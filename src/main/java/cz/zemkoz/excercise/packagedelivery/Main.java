package cz.zemkoz.excercise.packagedelivery;

import cz.zemkoz.excercise.packagedelivery.domain.PostPackage;
import cz.zemkoz.excercise.packagedelivery.exception.LoadPostPackagesFailed;
import cz.zemkoz.excercise.packagedelivery.exception.ParseStringFailedException;
import cz.zemkoz.excercise.packagedelivery.repository.PostDao;
import cz.zemkoz.excercise.packagedelivery.repository.PostRegister;
import cz.zemkoz.excercise.packagedelivery.service.*;

import java.io.File;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class Main {
    private static final long MINUTE_IN_MILLIS = 60000L;
    private static final String QUIT_COMMAND = "quit";

    private static final StringParser<PostPackage> postPackageParser;
    private static final PostService postService;
    private static final PostDao postDao;
    private static final TimerTask postPrinterTimerTask;

    static { // Simple DI configuration.
        postDao = new PostRegister();
        postPackageParser = new PostPackageParser();
        postService = new PostServiceImpl(postPackageParser, postDao);
        postPrinterTimerTask = new PostPrinterTimerTask(postService);
    }

    public static void main(String...args) {
        if(args.length > 0) {
            try {
                postService.processPostPackagesFromTextFile(new File(args[0]).toPath());
            } catch (LoadPostPackagesFailed e) {
                System.err.println(e.getMessage());
            }
        }

        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(postPrinterTimerTask, 0, MINUTE_IN_MILLIS);

        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.println("Enter the next post package data (Use format \"<weight> <postcode>\"):");
            String inputLine = scanner.nextLine();
            if(QUIT_COMMAND.equals(inputLine)) {
                System.out.println("Goodbye ...");
                timer.cancel();
                break;
            } else {
                try {
                    postService.processPostPackageFromString(inputLine);
                } catch (ParseStringFailedException e) {
                    System.err.println("Error: I can't read post package data from the entered input string.");
                }
            }
        }
    }
}
