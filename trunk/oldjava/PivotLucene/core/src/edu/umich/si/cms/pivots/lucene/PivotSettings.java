package edu.umich.si.cms.pivots.lucene;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * The settings file
 */
public class PivotSettings {

    private final int PID;
    private Properties properties;

    public PivotSettings(int pid) {
        this.PID = pid;
        properties = new Properties();
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream("config_" + pid + ".xml");
            properties.loadFromXML(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String conn_string = properties.getProperty("connection");
            conn = DriverManager.getConnection(conn_string);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
