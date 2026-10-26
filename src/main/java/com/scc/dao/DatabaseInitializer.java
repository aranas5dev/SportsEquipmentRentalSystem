package com.scc.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    private DatabaseInitializer() {
    }

    public static boolean initialize() {
        String createUsers = """
                CREATE TABLE IF NOT EXISTS users (
                    user_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    full_name TEXT NOT NULL,
                    username TEXT NOT NULL UNIQUE,
                    password TEXT NOT NULL,
                    contact_number TEXT,
                    email TEXT,
                    address TEXT,
                    role TEXT NOT NULL DEFAULT 'Customer',
                    status TEXT NOT NULL DEFAULT 'PENDING'
                )
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("PRAGMA foreign_keys = ON");
            statement.execute(createUsers);
            return true;
        } catch (SQLException e) {
            System.out.println("Database initialization error: " + e.getMessage());
            return false;
        }
    }
}
