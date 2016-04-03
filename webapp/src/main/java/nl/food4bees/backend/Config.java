package nl.food4bees.backend;

import java.util.Properties;

import java.io.IOException;
import java.io.FileInputStream;

public class Config extends Properties
{
    private static Config instance_;

    public static Config instance() throws IOException
    {
        if (instance_ == null) {
            instance_ = new Config();
        }
        return instance_;
    }

    private String location = "/var/webapps/food4bees.org/conf/f4bi.properties";

    private Config() throws IOException
    {
        super();

        load(new FileInputStream(location));
    }
}
