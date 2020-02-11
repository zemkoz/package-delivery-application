package cz.zemkoz.excercise.packagedelivery;

import cz.zemkoz.excercise.packagedelivery.domain.PostPackage;
import cz.zemkoz.excercise.packagedelivery.exception.ParseStringFailedException;
import cz.zemkoz.excercise.packagedelivery.service.PostPackageParser;
import cz.zemkoz.excercise.packagedelivery.service.PostService;
import cz.zemkoz.excercise.packagedelivery.service.PostServiceImpl;
import cz.zemkoz.excercise.packagedelivery.service.StringParser;

import java.util.Scanner;

public class Main {
    private static final String QUIT_COMMAND = "quit";

    private static final StringParser<PostPackage> postPackageParser;
    private static final PostService postService;

    static { // Simple DI configuration.
        postPackageParser = new PostPackageParser();
        postService = new PostServiceImpl(postPackageParser);
    }

    public static void main(String...args) {
        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.println("Enter the next post package data (Use format \"<weight> <postcode>\"):");
            String inputLine = scanner.nextLine();
            if(QUIT_COMMAND.equals(inputLine)) {
                System.out.println("Goodbye ...");
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
