package by.itstep.khodosevich.jdbcrunner.util;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Plan:
 * 1. Make four static variables: (password_key, username_key, url_key, pool_size_key);
 * 2. Make DEFAULT_POOL_SIZE;
 * 3. Make BlockingQueue
 * 4. Manually (customize) load driver for your DB;
 * 5. Make private method open() which return new connection;
 * 6. Make initialisation of our pool;
 * 7. Create method get() to take our connection to client;
 * 8. Make proxy for this pool when we require close().
 * 9. Create static method to close our pool!!!!
 **/

public class ConnectionPool {
    private static final String PASSWORD_KEY = "db.password";
    private static final String USERNAME_KEY = "db.username";
    private static final String URL_KEY = "db.url";
    private static final String POOL_SIZE_KEY = "db.size";
    private static final String DEFAULT_POOL_SIZE = "10";
    private static BlockingQueue<Connection> pool;

    private static List<Connection> sourceConnections;

    static {
        loadDrive();
        initConnection();
    }

    private static void loadDrive() {

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void initConnection() {

        String size = PropertiesUtilPool.get(POOL_SIZE_KEY);
        String result = size == null ? DEFAULT_POOL_SIZE : size;
        int blockingQueueSize = Integer.parseInt(result);

        pool = new ArrayBlockingQueue<>(blockingQueueSize);
        sourceConnections = new ArrayList<>(blockingQueueSize);

        for (int i = 0; i < blockingQueueSize; i++) {
            Connection connection = open();

            //create proxy for our connection
            Connection proxyConnection = (Connection) Proxy.newProxyInstance(ConnectionPool.class.getClassLoader(),
                    new Class[]{Connection.class},
                    (proxy, method, args) -> method.getName().equals("close")
                            ? pool.add((Connection) proxy)
                            : method.invoke(connection, args));
            sourceConnections.add(connection);
            pool.add(proxyConnection);
        }
    }

    private ConnectionPool() {
    }

    private static Connection open() {
        try {
            return DriverManager.getConnection(PropertiesUtilPool.get(URL_KEY),
                    PropertiesUtilPool.get(USERNAME_KEY),
                    PropertiesUtilPool.get(PASSWORD_KEY));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection get() {
        try {
            return pool.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void closePool() {
        for (int i = 0; i < sourceConnections.size(); i++) {
            try {
                sourceConnections.get(i).close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            ;
        }
    }
}
