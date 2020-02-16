package cz.zemkoz.excercise.packagedelivery;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TestUtility {
    private TestUtility() {
        throw new AssertionError("Utility class shouldn't be instantiated.");
    }

    public static File createTempFile(String tempFilePrefixName, String fileContent) throws IOException {
        final File tempFile = File.createTempFile(tempFilePrefixName, ".tmp");
        try(FileWriter writer = new FileWriter(tempFile)) {
            writer.write(fileContent);
        }
        return tempFile;
    }
}
