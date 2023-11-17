package org.example.database;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManager {

    private static Connection connection = null;

    private ConnectionManager() {
    }

    public static Connection getConnection() {
        if (connection == null) {
            try {
                Properties props = PropertiesLoader.loadProperty();

                String url = props.getProperty("db.url");
                String username = props.getProperty("db.username");
                String password = props.getProperty("db.password");

                connection = DriverManager.getConnection(url, username, password);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return connection;
    }

    public static class PropertiesLoader {
        private static final Properties properties = new Properties();

        private PropertiesLoader() {
        }

        public static Properties loadProperty() throws IOException {
            if (properties.isEmpty()) {
                InputStream inputStream = ConnectionManager.class.getClassLoader()
                        .getResourceAsStream("database.properties");
                properties.load(inputStream);
            }
            return properties;
        }
    }
}
