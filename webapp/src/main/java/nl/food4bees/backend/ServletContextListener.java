package nl.food4bees.backend;

import java.io.IOException;
import java.io.FileInputStream;

import java.net.URL;

import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.Driver;

import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.ServletContextEvent;

import org.apache.log4j.BasicConfigurator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServletContextListener implements javax.servlet.ServletContextListener
{
    private static String sourceClass = ServletContextListener.class.getName();
    private static Logger logger = LoggerFactory.getLogger(sourceClass);

    @Override
    public void contextInitialized(ServletContextEvent arg0)
    {
        BasicConfigurator.configure();
        try {
            ConnectionFactory.instance().initialize(Config.instance());
        } catch (ClassNotFoundException | IOException | SQLException e) {
            logger.error("Error initializing database.", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent arg0)
    {
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            try {
                DriverManager.deregisterDriver(driver);
            } catch (SQLException e) {
                String message = "Error deregistering driver: " + driver;

                logger.error(message, e);
            }
        }
    }
}
