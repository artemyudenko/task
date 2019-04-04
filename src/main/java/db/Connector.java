package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static db.SQLQueries.CREATE_TABLE_QUERY;
import static db.SQLQueries.DROP_TABLE_QUERY;

public class Connector {

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
}
