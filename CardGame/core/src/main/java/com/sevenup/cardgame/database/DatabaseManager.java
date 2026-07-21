package com.sevenup.cardgame.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    public static Connection connect() throws SQLException {
        // Placeholder: no DB configured. Throwing SQLException makes callers handle it.
        throw new SQLException("Database not configured in this environment.");
    }
}
