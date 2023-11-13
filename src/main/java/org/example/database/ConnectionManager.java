package org.example.database;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManager {

    private static Connection connection = null;

    private ConnectionManager() {
    }

    public static Connection getConnection() {
        if (connection != null)
            return connection;
        else {
            try {
                Properties props = PropertiesLoader.loadProperty();

                String url = props.getProperty("db.url");
                String username = props.getProperty("db.username");
                String password = props.getProperty("db.password");

                connection = DriverManager.getConnection(url, username, password);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return connection;
        }
    }

    public static class PropertiesLoader {
        private static final Properties properties = new Properties();

        private PropertiesLoader() {
        }

        public static Properties loadProperty() throws IOException {
            if (properties.isEmpty()) {
                String dbSettingsPropertyFile = "src/main/resources/database.properties";
                try (FileReader fReader = new FileReader(dbSettingsPropertyFile)) {
                    properties.load(fReader);
                }
            }
            return properties;
        }
    }
}
