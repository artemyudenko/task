import org.apache.commons.cli.*;

public class Application {
    public static void main(String[] args) {
        Option inputFile = new Option("f", "file", true, "Input file path");
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

        commandLine.getOptionValue("file");

        Solution solution = new Solution();
        solution.run();
    }

}
