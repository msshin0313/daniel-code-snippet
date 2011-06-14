package jdbcdemo;

import java.sql.*;

public class JDBCDemo {
    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            // MYSQL JDBC
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost/pivots?user=root");

            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT nid,title FROM node");
            while (rs.next()) {
                int nid = rs.getInt("nid");
                String title = rs.getString("title");
                System.out.printf("%3d: %s\n", nid, title);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            // it is a good idea to release resources in a finally{} block in reverse-order of their creation
            //  if they are no-longer needed
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
