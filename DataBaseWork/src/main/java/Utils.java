import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Utils {
    private static final String NEW_LINE = "\n";
    private static Logger log;

    private static String fromResultSet(ResultSet resultSet) throws SQLException {
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

    static void printResultSetToLog(ResultSet resSet) throws SQLException {
        log = Logger.getGlobal();
        log.info(NEW_LINE + fromResultSet(resSet));
    }
}
