import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import db.Connector;
import model.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class Solution {

    private static final Logger LOGGER = LoggerFactory.getLogger(Solution.class);

    private final Connector connector = new Connector();
    private ObjectMapper objectMapper = new ObjectMapper();

    private Map<String, Event> values = new HashMap<>();

    public void run(File file) throws IOException, SQLException, ClassNotFoundException {
        LOGGER.debug("Starting reading the file {}", file.getName());

        objectMapper.registerModule(new JavaTimeModule());
        connector.openDbConnection();
        Stream<String> lines = Files.lines(Paths.get(file.getAbsolutePath()));
        long startTime = System.currentTimeMillis();

        lines.forEach(line -> {
            try {
                Event event = objectMapper.readValue(line, Event.class);
                Event loggedEvent = values.get(event.getId());

                if (loggedEvent != null) {
                    LOGGER.debug("Event {} is compared with event {}", event.getId(), loggedEvent.getId());

                    long alertValue;
                    if (loggedEvent.getState().equals("STARTED")) {
                        alertValue = event.getTimestamp() - loggedEvent.getTimestamp();

                    } else {
                        alertValue = loggedEvent.getTimestamp() - event.getTimestamp();
                    }


                    if (alertValue > 4) {
                        event.setAlert(true);
                    }
                }

                values.put(event.getId(), event);


            } catch (IOException e) {
                LOGGER.error("Exception occured {}", e.getLocalizedMessage());
            }
        });
        long elapsedTime = System.currentTimeMillis() - startTime;
        LOGGER.info("File is successfully parsed. Inserting to DB. Elapsed time : {} seconds", TimeUnit.MILLISECONDS.toSeconds(elapsedTime));
        connector.insert(values);
        connector.close();
    }
}
