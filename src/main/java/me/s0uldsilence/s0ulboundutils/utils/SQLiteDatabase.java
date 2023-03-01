package me.s0uldsilence.s0ulboundutils.utils;

import me.s0uldsilence.s0ulboundutils.S0ulboundUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteDatabase {

    private static Connection connection;

    public static void connect() throws SQLException {
        String database = ConfigManager.getString("sqlite.database");

        String url = "jdbc:sqlite:" + S0ulboundUtils.getInstance().getDataFolder().getAbsolutePath() + "/" + database;

        connection = DriverManager.getConnection(url);
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