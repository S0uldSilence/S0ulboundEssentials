package me.s0uldsilence.s0ulboundutils.utils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;



public class DatabaseManager {

    private static final Logger logger = Logger.getLogger("S0ulboundUtils");

    public static void createTables()    {
        // Get the database connection based on the specified database type
        Connection connection = null;
        String databaseType = ConfigManager.getString("databaseType");
        if (databaseType.equalsIgnoreCase("mysql")) {
            connection = MySQLDatabase.getConnection();
        } else if (databaseType.equalsIgnoreCase("sqlite")) {
            connection = SQLiteDatabase.getConnection();
        }

        if (connection == null) {
            logger.warning("Could not create tables: No database connection found.");
            return;
        }

        // List of table creation queries
        List<String> tableCreationQueries = new ArrayList<>();

        // homes table
        //tableCreationQueries.add("CREATE TABLE IF NOT EXISTS homes (id INT AUTO_INCREMENT PRIMARY KEY, player_uuid VARCHAR(36) NOT NULL, home_name VARCHAR(64) NOT NULL, x DOUBLE NOT NULL, y DOUBLE NOT NULL, z DOUBLE NOT NULL, pitch FLOAT NOT NULL, yaw FLOAT NOT NULL, world_name VARCHAR(64) NOT NULL)");

        // Add more table creation queries here if needed

        // Execute table creation queries
        try {
            Statement statement = connection.createStatement();
            for (String query : tableCreationQueries) {
                statement.executeUpdate(query);
            }
            statement.close();
            logger.info("Tables created successfully.");
        } catch (SQLException e) {
            logger.warning("Error creating tables: " + e.getMessage());
        }
    }
    public static void createDefaultTables() {
        Connection connection = SQLiteDatabase.getConnection();
        List<String> tableCreationQueries = new ArrayList<>();
        //tableCreationQueries.add("CREATE TABLE IF NOT EXISTS homes (id INT AUTO_INCREMENT PRIMARY KEY, player_uuid VARCHAR(36) NOT NULL, home_name VARCHAR(64) NOT NULL, x DOUBLE NOT NULL, y DOUBLE NOT NULL, z DOUBLE NOT NULL, pitch FLOAT NOT NULL, yaw FLOAT NOT NULL, world_name VARCHAR(64) NOT NULL)");
        tableCreationQueries.add("CREATE TABLE IF NOT EXISTS homes (id INTEGER PRIMARY KEY AUTOINCREMENT, player_uuid TEXT NOT NULL, home_name TEXT NOT NULL, x DOUBLE NOT NULL, y DOUBLE NOT NULL, z DOUBLE NOT NULL, pitch FLOAT NOT NULL, yaw FLOAT NOT NULL, world_name TEXT NOT NULL, item_stack TEXT, UNIQUE(player_uuid, home_name))");

        try {
            Statement statement = connection.createStatement();
            for (String query : tableCreationQueries) {
                statement.executeUpdate(query);
            }
            statement.close();
            logger.info("Tables created successfully.");
        } catch (SQLException e) {
            logger.warning("Error creating tables: " + e.getMessage());
        }
    }
    public static void connectToDatabase() {
        String databaseType = ConfigManager.getString("databaseType");
        try {
            if (databaseType.equalsIgnoreCase("mysql")) {
                MySQLDatabase.connect();
                logger.info("Connected to MySQL database.");
            } else if (databaseType.equalsIgnoreCase("sqlite")) {
                SQLiteDatabase.connect();
                logger.info("Connected to SQLite database.");
            } else {
                logger.warning("Invalid database type in config.yml: " + databaseType);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void disconnectFromDatabase() {
        String databaseType = ConfigManager.getString("databaseType");
        try {
            if (databaseType.equalsIgnoreCase("mysql")) {
                MySQLDatabase.disconnect();
                logger.info("Disconnected from MySQL database.");
            } else if (databaseType.equalsIgnoreCase("sqlite")) {
                SQLiteDatabase.disconnect();
                logger.info("Disconnected from SQLite database.");
            } else {
                logger.warning("Invalid database type in config.yml: " + databaseType);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}