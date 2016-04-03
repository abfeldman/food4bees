package nl.food4bees.backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionFactory
{
    private static Logger logger = LoggerFactory.getLogger(ConnectionFactory.class);

    private static ConnectionFactory instance_;

    public static ConnectionFactory instance()
    {
        if (instance_ == null) {
            instance_ = new ConnectionFactory();
        }
        return instance_;
    }

    Connection connection_;

    public void initialize(Properties properties) throws ClassNotFoundException, SQLException
    {
        String driver = org.postgresql.Driver.class.getName();

        Class.forName(driver);

        String host = properties.getProperty("host");
        String port = properties.getProperty("port");
        String db = properties.getProperty("db");
        String user = properties.getProperty("user");
        String password = properties.getProperty("password");

        String url = String.format("jdbc:postgresql://%s:%s/%s", host, port, db);
        
        connection_ = DriverManager.getConnection(url, user, password);
    }

    public Connection getConnection()
    {
        return connection_;
    }
}
