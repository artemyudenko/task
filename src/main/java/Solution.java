import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import db.Connector;
import model.Event;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class Solution {

    private final Connector connector = new Connector();
    private ObjectMapper objectMapper = new ObjectMapper();

    private Map<String, Event> values = new HashMap<>();


    public void run(File file) throws IOException, SQLException, ClassNotFoundException {
        objectMapper.registerModule(new JavaTimeModule());
        connector.openDbConnection();
        Stream<String> lines = Files.lines(Paths.get(file.getAbsolutePath()));
        lines.forEach(line -> {
            try {
                Event event = objectMapper.readValue(line, Event.class);
                Event loggedEvent = values.get(event.getId());

                if (loggedEvent != null) {
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

            }
        });

        values.forEach((s, event) -> System.out.println(event.getId() + " " + event.getState() + " " + event.isAlert()));
    }
}
