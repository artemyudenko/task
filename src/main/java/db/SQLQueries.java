package db;

public enum SQLQueries {

    CREATE_TABLE_QUERY ("CREATE TABLE EVENT (ID_EVENT VARCHAR(100) , STATE VARCHAR(8) , TIME_STAMP TIMESTAMP, TYPE VARCHAR(50) , HOST VARCHAR(30), ALERT SMALLINT);"),
    DROP_TABLE_QUERY ("DROP TABLE EVENT;");

    private String value;

    SQLQueries(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
