import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class Application {
    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {
        String filePath = parseOptionsAndGetFilePath(args);
        File file = new File(filePath);

        if (!file.exists()) {
            System.exit(1);
        }

        Solution solution = new Solution();
        solution.run(file);
    }

    private static String parseOptionsAndGetFilePath(String[] args) {
        String longOpt = "file";

        Option inputFile = new Option("f", longOpt, true, "Input file path");
        inputFile.setRequired(true);

        Options options = new Options();
        options.addOption(inputFile);

        CommandLine commandLine = null;
        CommandLineParser commandLineParser = new DefaultParser();

        try {
            commandLine = commandLineParser.parse(options, args);
        } catch (ParseException e) {
            System.exit(1);
        }

        return commandLine.getOptionValue(longOpt);
    }

}
