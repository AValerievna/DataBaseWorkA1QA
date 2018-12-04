import java.sql.*;
import java.util.logging.Logger;

public class DBWork {


    private final String driverName = "com.mysql.jdbc.Driver";
    private final String connectionString = "jdbc:mysql://localhost:3306/taskdb";
    private final String login = "root";
    private final String password = "root";

    private final static String TEST_MIN_TIME_REQUEST = "select p.name PROJECT, t.name TEST, min(t.end_time - t.start_time) MIN_WORKING_TIME\n" +
            "from test t join project p on p.id=t.project_id\n" +
            "group by t.name, p.name;";
    private final static String DISTINCT_TESTS_REQUEST ="select p.name PROJECT, count(distinct t.name) TESTS_COUNT\n" +
            "from test t join project p on p.id=t.project_id\n" +
            "group by p.name;";
    private final static String AFTER_DATE_TESTS_REQUEST ="select p.name PROJECT, t.name TEST, t.start_time DATE\n" +
            "from test t join project p on p.id=t.project_id\n" +
            "where start_time >= '2015-07-11 00:00:00'\n" +
            "order by p.name, t.name;";
    private final static String BROWSER_TEST_COUNT_REQUEST ="select count(name) BROWSERS\n" +
            "from test \n" +
            "where browser='firefox'\n" +
            "union\n" +
            "select count(name)\n" +
            "from test \n" +
            "where browser='chrome';\n";

    public static void main(String args[]) throws SQLException, ClassNotFoundException {
        Logger log = Logger.getGlobal();
        DBWork app = new DBWork();
        Connection dbConnection = app.getConnection();
        app.getExecutionResult(dbConnection);
        app.closeConnection(dbConnection);
    }

    public Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName(driverName);
        Connection connection = null;
        connection = DriverManager.getConnection(connectionString, login, password);
        return connection;
    }

    private void closeConnection(Connection connection) throws SQLException {
            connection.close();
    }



    private ResultSet getExecutionResult(Connection connection) throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("select p.name PROJECT, t.name TEST, min(t.end_time - t.start_time) MIN_WORKING_TIME\n" +
                "from test t join project p on p.id=t.project_id\n" +
                "group by t.name, p.name;\n");

        System.out.println(rs);
        return rs;
    }
}