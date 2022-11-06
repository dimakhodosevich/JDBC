package by.itstep.khodosevich.jdbcrunner.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Plan:
 * 1. Make private static variable: (Properties properties);
 * 2. Manually (customize) load this class and create InputStream;
 * **/

public class PropertiesUtilPool {
    private static final Properties PROPERTIES = new Properties();

    static {
        loadProperties();
    }

    private PropertiesUtilPool(){
    }

    private static void loadProperties() {
        try (InputStream inputStream = PropertiesUtilPool.class.getClassLoader()
                .getResourceAsStream("connectionpool.properties");) {
            PROPERTIES.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String get(String key){
        return PROPERTIES.getProperty(key);
    }
}
