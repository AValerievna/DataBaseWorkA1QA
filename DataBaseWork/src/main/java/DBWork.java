
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class DBWork {


    static Logger log;
    private final String driverName = "com.mysql.jdbc.Driver";
    private final String connectionString = "jdbc:mysql://localhost:3306/union_reporting";
    private final String login = "root";
    private final String password = "root";

    private final static String TEST_MIN_TIME_REQUEST = "select p.name PROJECT, t.name TEST, min(t.end_time - t.start_time) MIN_WORKING_TIME\n" +
            "from test t join project p on p.id=t.project_id\n" +
            "group by t.name, p.name;";
    private final static String DISTINCT_TESTS_REQUEST = "select p.name PROJECT, count(distinct t.name) TESTS_COUNT\n" +
            "from test t join project p on p.id=t.project_id\n" +
            "group by p.name;";
    private final static String AFTER_DATE_TESTS_REQUEST = "select p.name PROJECT, t.name TEST, t.start_time DATE\n" +
            "from test t join project p on p.id=t.project_id\n" +
            "where start_time >= '2015-07-11 00:00:00'\n" +
            "order by p.name, t.name;";
    private final static String BROWSER_TEST_COUNT_REQUEST = "select count(name) BROWSERS\n" +
            "from test \n" +
            "where browser='firefox'\n" +
            "union\n" +
            "select count(name)\n" +
            "from test \n" +
            "where browser='chrome';\n";

    public static void main(String args[]) throws Exception {
        log = Logger.getGlobal();
        DBWork app = new DBWork();
        Connection dbConnection = app.getConnection();
        app.getExecutionResult(dbConnection, TEST_MIN_TIME_REQUEST);
        app.getExecutionResult(dbConnection, DISTINCT_TESTS_REQUEST);
        app.getExecutionResult(dbConnection, AFTER_DATE_TESTS_REQUEST);
        app.getExecutionResult(dbConnection, BROWSER_TEST_COUNT_REQUEST);
        app.closeConnection(dbConnection);
    }

    public Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName(driverName);
        Connection connection = DriverManager.getConnection(connectionString, login, password);
        return connection;
    }

    private void closeConnection(Connection connection) throws SQLException {
        connection.close();
    }


    private ResultSet getExecutionResult(Connection connection, String query) throws Exception {
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        log.info("\n" + fromResultSet(rs));
        return rs;
    }

    public String fromResultSet(ResultSet resultSet) throws SQLException {
        if (resultSet == null) throw new NullPointerException("resultSet == null");
        if (!resultSet.isBeforeFirst()) throw new IllegalStateException("Result set not at first.");

        List<String> headers = new ArrayList<>();
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        int columnCount = resultSetMetaData.getColumnCount();
        for (int column = 0; column < columnCount; column++) {
            headers.add(resultSetMetaData.getColumnLabel(column + 1));
        }

        List<String[]> data = new ArrayList<>();
        while (resultSet.next()) {
            String[] rowData = new String[columnCount];
            for (int column = 0; column < columnCount; column++) {
                rowData[column] = resultSet.getString(column + 1);
            }
            data.add(rowData);
        }

        String[] headerArray = headers.toArray(new String[headers.size()]);
        String[][] dataArray = data.toArray(new String[data.size()][]);
        return new FlipTable(headerArray, dataArray).toString();
    }


}