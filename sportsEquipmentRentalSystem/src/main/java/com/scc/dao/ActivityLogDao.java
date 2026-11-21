package com.scc.dao;

import com.scc.model.ActivityLog;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ActivityLogDao {

    // INSERT ACTIVITY LOG
    public boolean logAction(
            int userId,
            String actionType,
            String description) {

        String sql = """
                INSERT INTO activity_logs
                (user_id, action_type, description)
                VALUES (?, ?, ?)
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (userId <= 0) {
                stmt.setNull(1, Types.INTEGER);
            } else {
                stmt.setInt(1, userId);
            }

            stmt.setString(2, actionType);
            stmt.setString(3, description);

            return stmt.executeUpdate() == 1;

        } catch (SQLException e) {
            System.out.println("Activity log error: " + e.getMessage());
            return false;
        }
    }

    // GET ALL SYSTEM LOGS
    public List<ActivityLog> getAllLogs() {

        List<ActivityLog> logs = new ArrayList<>();

        String sql = """
                SELECT *
                FROM activity_logs
                ORDER BY timestamp DESC, log_id DESC
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                logs.add(new ActivityLog(
                        rs.getInt("log_id"),
                        rs.getInt("user_id"),
                        rs.getString("action_type"),
                        rs.getString("description"),
                        rs.getString("timestamp")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Unable to retrieve logs: " + e.getMessage());
        }

        return logs;
    }

    // FILTER LOGS BY USER ID
    public List<ActivityLog> getLogsByUserId(int userId) {

        List<ActivityLog> logs = new ArrayList<>();

        String sql = """
                SELECT *
                FROM activity_logs
                WHERE user_id = ?
                ORDER BY timestamp DESC, log_id DESC
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {

                    logs.add(new ActivityLog(
                            rs.getInt("log_id"),
                            rs.getInt("user_id"),
                            rs.getString("action_type"),
                            rs.getString("description"),
                            rs.getString("timestamp")
                    ));
                }
            }

        } catch (SQLException e) {
            System.out.println("Unable to filter logs: " + e.getMessage());
        }

        return logs;
    }

    // CLEAR ALL HISTORICAL LOGS
    public boolean clearAllLogs() {

        String sql = "DELETE FROM activity_logs";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Unable to clear logs: " + e.getMessage());
            return false;
        }
    }
}