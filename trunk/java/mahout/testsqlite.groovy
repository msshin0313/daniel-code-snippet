package mahout

import java.sql.Connection
import java.sql.ResultSet
import java.sql.Statement
import org.apache.commons.dbcp.BasicDataSource
import org.apache.commons.dbcp.BasicDataSourceFactory

Properties dbProperties = new Properties();
dbProperties.put("driverClassName", "org.sqlite.JDBC")
dbProperties.put("url", "jdbc:sqlite:/home/daniel/Development/snippet/java/mahout/sqlite.db")
BasicDataSource dataSource = BasicDataSourceFactory.createDataSource(dbProperties);
dataSource.setMaxActive(1)
Connection conn = dataSource.getConnection();

//Class.forName("org.sqlite.JDBC");
//Connection conn = DriverManager.getConnection("jdbc:sqlite:/home/daniel/Development/snippet/java/mahout/sqlite.db");

Statement stat = conn.createStatement();
ResultSet rs = stat.executeQuery("select user, item from ratings;");
while (rs.next()) {
  System.out.println(rs.getInt("user") + ',' + rs.getInt("item"));
}
rs.close();
conn.close();