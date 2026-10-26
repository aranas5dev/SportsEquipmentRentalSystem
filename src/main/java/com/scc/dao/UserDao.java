package com.scc.dao;

import com.scc.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    private User mapUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("user_id"),
                rs.getString("full_name"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("contact_number"),
                rs.getString("email"),
                rs.getString("address"),
                rs.getString("role"),
                rs.getString("status")
        );
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = """
                SELECT user_id, full_name, username, password,
                       contact_number, email, address, role, status
                FROM users
                ORDER BY user_id
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                users.add(mapUser(rs));
            }
        } catch (SQLException e) {
            System.out.println("Unable to load users: " + e.getMessage());
        }
        return users;
    }

    public List<User> getPendingUsers() {
        List<User> users = new ArrayList<>();
        String sql = """
                SELECT user_id, full_name, username, password,
                       contact_number, email, address, role, status
                FROM users
                WHERE UPPER(status) = 'PENDING'
                ORDER BY user_id
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                users.add(mapUser(rs));
            }
        } catch (SQLException e) {
            System.out.println("Unable to load pending users: " + e.getMessage());
        }
        return users;
    }

    public boolean approveUser(int userId) {
        String sql = """
                UPDATE users
                SET status = 'APPROVED'
                WHERE user_id = ? AND UPPER(status) = 'PENDING'
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            System.out.println("Unable to approve user: " + e.getMessage());
            return false;
        }
    }

    public User authenticate(String username, String password) {
        String sql = """
                SELECT user_id, full_name, username, password,
                       contact_number, email, address, role, status
                FROM users
                WHERE username = ? AND password = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = mapUser(rs);
                    if ("APPROVED".equalsIgnoreCase(user.getStatus())) {
                        return user;
                    }
                    if ("PENDING".equalsIgnoreCase(user.getStatus())) {
                        System.out.println("Your account is pending admin approval.");
                    } else {
                        System.out.println("Your account is not approved or is inactive.");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Login error: " + e.getMessage());
        }
        return null;
    }

    public boolean registerUser(User user) {
        String sql = """
                INSERT INTO users
                    (full_name, username, password, contact_number,
                     email, address, role, status)
                VALUES (?, ?, ?, ?, ?, ?, ?, 'PENDING')
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getFullName());
            stmt.setString(2, user.getUsername());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getContactNumber());
            stmt.setString(5, user.getEmail());
            stmt.setString(6, user.getAddress());
            stmt.setString(7, user.getRole());
            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            System.out.println("Registration error: " + e.getMessage());
            return false;
        }
    }
}
