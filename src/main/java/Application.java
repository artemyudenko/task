import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {
        String filePath = parseOptionsAndGetFilePath(args);
        LOGGER.info("Arguments parsed successfully.");

        File file = new File(filePath);

        if (!file.exists()) {
            LOGGER.warn("File {} does not exist", filePath);
            System.exit(1);
        }

        Solution solution = new Solution();
        solution.run(file);
    }

    private static String parseOptionsAndGetFilePath(String[] args) {
        LOGGER.trace("Parsing the arguments.");
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
            LOGGER.error("Error while parsing input arguments {}", e.getLocalizedMessage());
            System.exit(1);
        }

        return commandLine.getOptionValue(longOpt);
    }

}
