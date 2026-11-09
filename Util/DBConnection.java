package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {
    private static final String BASE_URL = "jdbc:mysql://127.0.0.1:3306/?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String DB_NAME = "math_whiz_db";
    private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/" + DB_NAME + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static String user = "root";
    private static String password = "";
    private static boolean dbInitialized = false;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            try { Class.forName("com.mysql.jdbc.Driver"); } catch (Exception ignored) {}
        }
    }

    public static synchronized void ensureDatabaseAndTableExist() {
        if (dbInitialized) return;
        try (Connection rootConn = DriverManager.getConnection(BASE_URL, user, password);
             Statement stmt = rootConn.createStatement()) {
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME + " CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");
            stmt.executeUpdate("USE " + DB_NAME);
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS game_scores (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "player_name VARCHAR(100) NOT NULL, " +
                    "score INT NOT NULL, " +
                    "difficulty VARCHAR(20) NOT NULL DEFAULT 'NORMAL', " +
                    "played_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
            dbInitialized = true;
        } catch (SQLException ignored) {}
    }

    public static Connection getConnection() throws SQLException {
        ensureDatabaseAndTableExist();
        return DriverManager.getConnection(DB_URL, user, password);
    }
}
