package jsonparser;

import java.io.File;

public class ArgumentToFileParser {
    private ArgumentToFileParser() {}
    public static File parse(String[] args) {
        if (args.length != 1) {
            System.out.println("DEBUG:" + args.length);
            throw new IllegalArgumentException("Error: expected exactly one argument.");
        }

        File inputFile = new File(args[0]);

        if (inputFile.exists() && inputFile.isFile()) {
            return inputFile;
        } else {
            throw new IllegalArgumentException("Error: input file is invalid or does not exist.");
        }
    }
}
