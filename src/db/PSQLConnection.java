package db;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class PSQLConnection {
    private final String DRIVER;
    private final String URL;
    private final String USER;
    private final String PASSWORD;

    
    public PSQLConnection() throws Exception {
        InputStream psqlConfig = getClass().getClassLoader().getResourceAsStream("config.psql.properties");

        Properties properties = new Properties();
        properties.load(psqlConfig);

        DRIVER = properties.getProperty("driver");
        Class.forName(this.DRIVER);

        URL = properties.getProperty("url");
        USER = properties.getProperty("user");
        PASSWORD = properties.getProperty("password");
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}