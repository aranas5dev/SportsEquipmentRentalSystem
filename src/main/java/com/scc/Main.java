package com.scc;

import com.scc.dao.DatabaseConnection;
import com.scc.dao.DatabaseInitializer;
import com.scc.ui.Authentication;

public class Main {
    public static void main(String[] args) {
        if (!DatabaseInitializer.initialize()) {
            System.out.println("Could not initialize the database. Check the database path and SQLite driver.");
            return;
        }

        try {
            DatabaseConnection.enableForeignKeys();
            new Authentication().start();
        } catch (Exception e) {
            System.out.println("Application startup failed: " + e.getMessage());
        }
    }
}
