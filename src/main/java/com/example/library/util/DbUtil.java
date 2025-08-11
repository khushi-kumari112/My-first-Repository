package com.example.library.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Utility class to provide JDBC connections using properties from classpath file db.properties.
 *
 * Expected properties:
 * - db.url (e.g. jdbc:mysql://localhost:3306/library_db?useSSL=false&serverTimezone=UTC)
 * - db.user
 * - db.password
 */
public final class DbUtil {

    private static final String PROPERTIES_FILE = "/db.properties";
    private static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream input = DbUtil.class.getResourceAsStream(PROPERTIES_FILE)) {
            if (input == null) {
                throw new IllegalStateException("Could not find " + PROPERTIES_FILE + " on the classpath");
            }
            PROPERTIES.load(input);
        } catch (IOException e) {
            throw new ExceptionInInitializerError("Failed to load database properties: " + e.getMessage());
        }
    }

    private DbUtil() { }

    /**
     * Returns a new JDBC {@link Connection} to the configured database.
     */
    public static Connection getConnection() throws SQLException {
        String url = PROPERTIES.getProperty("db.url");
        String user = PROPERTIES.getProperty("db.user");
        String password = PROPERTIES.getProperty("db.password");
        return DriverManager.getConnection(url, user, password);
    }
}