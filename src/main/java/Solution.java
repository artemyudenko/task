import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Solution {

    private Connection conn;

    private static final String JDBC_DRIVER = "org.hsqldb.jdbc.JDBCDriver";
    private static final String DB_NAME = "cs/cs_task";
    private static final String CONNECTION_STRING = "jdbc:hsqldb:file:";

    public void run() {
        openDbConnection();
    }

    private void openDbConnection() {
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(CONNECTION_STRING + DB_NAME, "sa", "");
            Statement st = conn.createStatement();
            conn.setAutoCommit(false);
            conn.commit();
        } catch (Exception e) {
            System.exit(1);
        }
    }

}
