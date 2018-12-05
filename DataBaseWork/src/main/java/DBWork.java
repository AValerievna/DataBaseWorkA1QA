import java.sql.ResultSet;

public class DBWork {


    private static Configuration conf;

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
        conf = new Configuration();
        try (DBConnectionWork app = new DBConnectionWork(conf.getProperty("driver.name"), conf.getProperty("connection.string"), conf.getProperty("login"), conf.getProperty("password"))) {
            ResultSet testMinTimeResult = app.getExecutionResult(TEST_MIN_TIME_REQUEST);
            Utils.printResultSetToLog(testMinTimeResult);
            ResultSet distinctTestsResult = app.getExecutionResult(DISTINCT_TESTS_REQUEST);
            Utils.printResultSetToLog(distinctTestsResult);
            ResultSet afterDateTestsResult = app.getExecutionResult(AFTER_DATE_TESTS_REQUEST);
            Utils.printResultSetToLog(afterDateTestsResult);
            ResultSet browserTestCountResult = app.getExecutionResult(BROWSER_TEST_COUNT_REQUEST);
            Utils.printResultSetToLog(browserTestCountResult);
        }
    }

}