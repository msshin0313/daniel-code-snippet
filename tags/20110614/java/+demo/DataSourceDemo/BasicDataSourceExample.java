import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import org.apache.commons.dbcp.BasicDataSource;

public class BasicDataSourceExample {

    public static void main(String[] args) throws Exception {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        //ds.setUsername("remote");
        //ds.setPassword("mercyme");
        ds.setUrl("jdbc:mysql://141.211.184.206/mitbbs?user=remote&password=mercyme");

        Connection conn = null;
        Statement stmt = null;
        ResultSet rset = null;

        conn = ds.getConnection();
        stmt = conn.createStatement();
        rset = stmt.executeQuery("SELECT count(*) FROM file");
        System.out.println("Results:");
        int numcols = rset.getMetaData().getColumnCount();
        while(rset.next()) {
            for(int i=1;i<=numcols;i++) {
                System.out.print("\t" + rset.getString(i));
            }
            System.out.println("");
        }

        ds.close();
    }
}

