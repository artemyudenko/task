package db;

import model.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.Map;

import static db.SQLQueries.CREATE_TABLE_QUERY;
import static db.SQLQueries.DROP_TABLE_QUERY;
import static db.SQLQueries.INSERT_QUERY;

public class Connector {

    private static final Logger LOGGER = LoggerFactory.getLogger(Connector.class);

    private Connection conn;

    private static final String JDBC_DRIVER = "org.hsqldb.jdbc.JDBCDriver";
    private static final String DB_NAME = "cs/cs_task";
    private static final String CONNECTION_STRING = "jdbc:hsqldb:file:";

    public void openDbConnection() throws SQLException, ClassNotFoundException {
        Class.forName(JDBC_DRIVER);
        conn = DriverManager.getConnection(CONNECTION_STRING + DB_NAME, "sa", "");
        Statement st = conn.createStatement();
        st.execute(DROP_TABLE_QUERY.getValue());
        st.execute(CREATE_TABLE_QUERY.getValue());
        conn.setAutoCommit(false);
        conn.commit();
    }

    public void close() {
        try {
            LOGGER.info("Closing the connection with database.");

            conn.close();

            LOGGER.info("Database closed.");
        } catch (SQLException e) {
            LOGGER.error("Error while connection closing {}", e.getLocalizedMessage());
        }
    }

    public void insert(Map<String, Event> values) {
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(INSERT_QUERY.getValue());

            Iterator<Map.Entry<String, Event>> iterator = values.entrySet().iterator();
            int counter = 0;

            LOGGER.debug("Inserting data to the database.");

            while (iterator.hasNext()) {
                Event entry = iterator.next().getValue();

                preparedStatement.setString(1, entry.getId());
                preparedStatement.setString(2, entry.getState());
                preparedStatement.setTimestamp(3, new Timestamp(entry.getTimestamp()));
                preparedStatement.setString(4, entry.getType());
                preparedStatement.setString(5, entry.getHost());
                preparedStatement.setBoolean(6, entry.isAlert());
                preparedStatement.addBatch();

                counter++;

                if (counter % 10000 == 0 && counter != 0) {
                    int[] ints = preparedStatement.executeBatch();
                    LOGGER.info("Inserted {} rows", ints.length);
                } else if (counter == values.entrySet().size()) {
                    int[] ints = preparedStatement.executeBatch();
                    LOGGER.info("Inserted {} rows", ints.length);
                }
            }
            conn.commit();
            LOGGER.info("Data is successfully inserted");
        } catch (SQLException e) {
            LOGGER.error("Error while saving to database {}", e.getLocalizedMessage());
        }
    }

}
