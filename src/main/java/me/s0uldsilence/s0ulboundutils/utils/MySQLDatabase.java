package me.s0uldsilence.s0ulboundutils.utils;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLDatabase {

    private static Connection connection;

    public static void connect() throws SQLException {
        String host = ConfigManager.getString("mysql.host");
        String port = ConfigManager.getString("mysql.port");
        String database = ConfigManager.getString("mysql.database");
        String username = ConfigManager.getString("mysql.username");
        String password = ConfigManager.getString("mysql.password");

        String url = "jdbc:mysql://" + host + ":" + port + "/" + database;

        connection = DriverManager.getConnection(url, username, password);
    }

    public static Connection getConnection() {
        return connection;
    }

    public static void disconnect() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
}