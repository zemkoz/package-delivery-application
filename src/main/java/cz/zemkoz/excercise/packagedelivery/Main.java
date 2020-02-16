package cz.zemkoz.excercise.packagedelivery;

import cz.zemkoz.excercise.packagedelivery.domain.PostPackage;
import cz.zemkoz.excercise.packagedelivery.domain.PriceListItem;
import cz.zemkoz.excercise.packagedelivery.exception.LoadPostPackagesFailedException;
import cz.zemkoz.excercise.packagedelivery.exception.LoadPriceListFailedException;
import cz.zemkoz.excercise.packagedelivery.exception.ParseStringFailedException;
import cz.zemkoz.excercise.packagedelivery.parser.PostPackageParser;
import cz.zemkoz.excercise.packagedelivery.parser.PriceListItemParser;
import cz.zemkoz.excercise.packagedelivery.parser.StringParser;
import cz.zemkoz.excercise.packagedelivery.repository.PostDao;
import cz.zemkoz.excercise.packagedelivery.repository.PostRegister;
import cz.zemkoz.excercise.packagedelivery.service.DeliveryPriceListService;
import cz.zemkoz.excercise.packagedelivery.service.DeliveryPriceListServiceImpl;
import cz.zemkoz.excercise.packagedelivery.service.PostService;
import cz.zemkoz.excercise.packagedelivery.service.PostServiceImpl;
import cz.zemkoz.excercise.packagedelivery.task.PostPrinterTimerTask;
import org.apache.commons.cli.*;

import java.io.File;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class Main {
    private static final long MINUTE_IN_MILLIS = 60000L;
    private static final String QUIT_COMMAND = "quit";

    private static final StringParser<PostPackage> postPackageParser;
    private static final StringParser<PriceListItem> priceListItemParser;
    private static final PostService postService;
    private static final DeliveryPriceListService deliveryPriceListService;
    private static final PostDao postDao;
    private static final TimerTask postPrinterTimerTask;

    static { // Simple DI configuration.
        postDao = new PostRegister();
        postPackageParser = new PostPackageParser();
        priceListItemParser = new PriceListItemParser();
        deliveryPriceListService = new DeliveryPriceListServiceImpl(priceListItemParser);
        postService = new PostServiceImpl(postPackageParser, postDao, deliveryPriceListService);
        postPrinterTimerTask = new PostPrinterTimerTask(postService);
    }

    public static void main(String...args) {
        handleCommandLineArgs(args);

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

    public static void handleCommandLineArgs(String[] cliArgs) {
        Options cliOptions = new Options();
        cliOptions.addOption("p", "price-list", true,
                "File with price list of delivery based on the post package weight.");
        cliOptions.addOption("i", "init", true,
                "File with initial list of post packages witch should be delivered to posts.");
        cliOptions.addOption("h", "help", false, "Shows this help.");

        CommandLineParser commandLineParser = new DefaultParser();
        final CommandLine commandLine;
        try {
            commandLine = commandLineParser.parse(cliOptions, cliArgs);
        } catch (ParseException e) {
            System.err.println("The application was not executed correctly. Please see the application menu.");
            System.out.println();
            printHelp(cliOptions);
            System.exit(1);
            return;
        }

        if (commandLine.hasOption("h")) {
            printHelp(cliOptions);
            System.exit(0);
            return;
        }

        if (commandLine.hasOption("p")) {
            try {
                deliveryPriceListService.loadPriceListFromFile(new File(commandLine.getOptionValue("p")).toPath());
            } catch (LoadPriceListFailedException e) {
                System.err.println(e.getMessage());
            }
        }

        if (commandLine.hasOption("i")) {
            try {
                postService.processPostPackagesFromTextFile(new File(commandLine.getOptionValue("i")).toPath());
            } catch (LoadPostPackagesFailedException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    public static void printHelp(Options cliOptions) {
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp("package-delivery-application", cliOptions, true);
    }
}
