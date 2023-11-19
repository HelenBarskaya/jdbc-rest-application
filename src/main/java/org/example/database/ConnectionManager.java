package org.example.database;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManager {

    private String url;
    private String username;
    private String password;

    public ConnectionManager() {
        Properties props;

        try {
            props = PropertiesLoader.loadProperty();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        url = props.getProperty("db.url");
        username = props.getProperty("db.username");
        password = props.getProperty("db.password");
    }

    public Connection getConnection() {
        String connectionUrl = getUrl();
        String connectionUsername = getUsername();
        String connectionPassword = getPassword();

        try {
            return DriverManager.getConnection(connectionUrl, connectionUsername, connectionPassword);
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
