import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbcp.BasicDataSource;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.ResultSetMetaData;

public class DbUtilsDemo {
    private static DataSource dataSource = null;
    public static void main(String[] args) throws Exception {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        ds.setUrl("jdbc:mysql://141.211.184.206/mitbbs?user=remote&password=mercyme");
        dataSource = ds;
        method1();

    }

    private static void method1() throws SQLException {
        // Create a ResultSetHandler implementation to convert the first row into an Object[].
        ResultSetHandler h = new ResultSetHandler() {
            public Object handle(ResultSet rs) throws SQLException {
                if (!rs.next()) {
                    return null;
                }
                ResultSetMetaData meta = rs.getMetaData();
                int cols = meta.getColumnCount();
                Object[] result = new Object[cols];

                for (int i = 0; i < cols; i++) {
                    result[i] = rs.getObject(i + 1);
                }
                return result;
            }
        };
        // Create a QueryRunner that will use connections from the given DataSource
        QueryRunner run = new QueryRunner(dataSource);

        // Execute the query and get the results back from the handler
        Object[] result = (Object[]) run.query("SELECT * FROM file WHERE fid=?", 57589, h);
        System.out.println(result[3]);
    }
}
