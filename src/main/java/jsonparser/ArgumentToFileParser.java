package jsonparser;

import java.io.File;

public class ArgumentToFileParser {
    private ArgumentToFileParser() {}
    public static File parse(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("Error: expected exactly one argument, received " + args.length);
        }

        File inputFile = new File(args[0]);

        if (inputFile.exists() && inputFile.isFile()) {
            return inputFile;
        } else {
            throw new IllegalArgumentException("Error: input file is invalid or does not exist.");
        }
    }
}
