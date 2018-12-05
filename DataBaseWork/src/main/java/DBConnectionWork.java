import java.sql.*;

public class DBConnectionWork implements AutoCloseable {
    private Connection dbConnection;

    public DBConnectionWork(String driverName, String connectionStr, String login, String password) throws ClassNotFoundException, SQLException {
        Class.forName(driverName);
        dbConnection = DriverManager.getConnection(connectionStr, login, password);
    }

    ResultSet getExecutionResult(String query) throws Exception {
        Statement stmt = dbConnection.createStatement();
        return stmt.executeQuery(query);
    }

    @Override
    public void close() throws Exception {
        dbConnection.close();
    }
}
